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

public class GetRatesByPeriodTest {

	LocalDate startDay1 = LocalDate.of(2019, 01, 01);
	LocalDate endDay1 = LocalDate.of(2019, 01, 03);

	String AUD = "AUD";
	String TRY = "TRY";

	List<String> codes1 = Arrays.asList(AUD, TRY);

	@Test
	void test1() throws MalformedURLException, IOException, JAXBException {
		RatesRetriever retriever = new RatesRetriever();

		List<Currency> expectedResult1 = new ArrayList<>();
		
		expectedResult1.add(new Currency("name", AUD, 1.622, startDay1.minusDays(1)));
		expectedResult1.add(new Currency("name", AUD, 1.6273, endDay1.minusDays(1)));
		expectedResult1.add(new Currency("name", AUD, 1.6287, endDay1));
		expectedResult1.add(new Currency("name", TRY, 6.0588, startDay1.minusDays(1)));
		expectedResult1.add(new Currency("name", TRY, 6.113, endDay1.minusDays(1)));
		expectedResult1.add(new Currency("name", TRY, 6.2248, endDay1));

		List<Currency> resultList = retriever.getRatesByPeriod(startDay1, endDay1, codes1);

		for (Currency result : resultList) {
			System.out.println(result.toString());
		}

		for (Currency expected : expectedResult1) {
			assertTrue(resultList.stream().anyMatch(c -> c.equals(expected)));
		}
	}
}