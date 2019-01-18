/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever;

import currencyratesretriever.utils.RatesRetriever;
import currencyratesretriever.utils.InputReader;
import currencyratesretriever.models.Currency;
import currencyratesretriever.models.CurrencyRatesInfo;
import currencyratesretriever.models.QueryParameters;
import currencyratesretriever.utils.FormatCurrenciesInfo;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Dovile
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws JAXBException, IOException {

		Scanner in = new Scanner(System.in);
		Boolean isActive = true;

		while (isActive) {
			QueryParameters parameters = InputReader.getQueryParameters();
			RatesRetriever retriever = new RatesRetriever();
			List<Currency> currencies = new ArrayList<>();

			if (parameters == null) {
				System.out.println("Good bye");
				in.close();
				System.exit(0);
			}

			String type = parameters.getType();
			List<String> codes = parameters.getCodes();

			if (type.equals("period")) {
				LocalDate startDay = parameters.getDays().get(0);
				LocalDate endDay = parameters.getDays().get(1);
				currencies = retriever.getRatesByPeriod(startDay, endDay, codes);
			} else if (type.contentEquals("dates")) {
				currencies = retriever.getRatesByDates(parameters.getDays(), codes);
			}
			Map<String, CurrencyRatesInfo> currenciesRates;
			String out;
			if (currencies != null) {
				currenciesRates = RatesRetriever.groupByCurrency(currencies, parameters.getDays());
				out = FormatCurrenciesInfo.formatCurrenciesInfo(currenciesRates);

			} else {
				out = "Failed to retrieve data from bank";
			}

			System.out.println(out);
			System.out.println("To get more rates enter 'next'");

			if (!(in.nextLine().trim().equalsIgnoreCase("next"))) {
				isActive = false;
			}

		}
	}
}
