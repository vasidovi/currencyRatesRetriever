package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Test;

import currencyratesretriever.models.Currency;
import currencyratesretriever.utils.RatesRetriever;

public class GetRatesByDatesTest {

	LocalDate holliday = LocalDate.of(2019, 01, 01);
	LocalDate workday = LocalDate.of(2019, 01, 02);
	LocalDate sunday = LocalDate.of(2019, 01, 13);

	String AUD = "AUD";
	String TRY = "TRY";

	List<LocalDate> days1 = Arrays.asList(holliday, workday, sunday);
	List<String> codes1 = Arrays.asList(AUD, TRY);

	@Test
	void test1() throws MalformedURLException, IOException, JAXBException {
		RatesRetriever retriever = new RatesRetriever();

		List<Currency> expectedResult = new ArrayList<>();
		expectedResult.add(new Currency("name", AUD, 1.622, holliday.minusDays(1)));
		expectedResult.add(new Currency("name", AUD, 1.6273, workday));
		expectedResult.add(new Currency("name", AUD, 1.597, sunday.minusDays(2)));
		expectedResult.add(new Currency("name", TRY, 6.0588, holliday.minusDays(1)));
		expectedResult.add(new Currency("name", TRY, 6.113, workday));
		expectedResult.add(new Currency("name", TRY, 6.3024, sunday.minusDays(2)));

		List<Currency> resultList = retriever.getRatesByDates(days1, codes1);

		for (Currency result : resultList) {
			System.out.println(result.toString());
		}

		for (Currency expected : expectedResult) {
			assertTrue(resultList.stream().anyMatch(c -> c.equals(expected)));
		}
	}
}