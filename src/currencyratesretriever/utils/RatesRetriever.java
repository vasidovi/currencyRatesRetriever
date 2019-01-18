/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.utils;

import currencyratesretriever.models.Currencies;
import currencyratesretriever.models.Currency;
import currencyratesretriever.models.CurrencyRatesInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Dovile
 */
public class RatesRetriever {

	private static final String SRC = "https://www.lb.lt/lt/currency/exportlist/?xml=1";

	public List<Currency> getRatesByDates(List<LocalDate> days, List<String> codes)
			throws MalformedURLException, IOException, JAXBException {

		List<Currency> currenciesAll = new ArrayList<>();

		for (LocalDate day : days) {
			day = shiftFixedHollidayDate(day);

			for (String code : codes) {

				List<Currency> currencies = null;

				int i = 0;
				while (currencies == null || currencies.isEmpty()) {
					LocalDate newDate = day.minusDays(i);
					String dayQuery = "&date_from_day=" + newDate + "&date_to_day=" + newDate;
					String currQuery = "&currency=" + code;
					String urlString = SRC + dayQuery + currQuery;
					currencies = getCurrencyRates(urlString);

					if (currencies == null)
						return null;
					i++;
				}
				currenciesAll.addAll(currencies);
			}
		}

		return currenciesAll;
	}

	public List<Currency> getRatesByPeriod(LocalDate startDay, LocalDate endDay, List<String> codes)
			throws MalformedURLException, IOException, JAXBException {

		List<Currency> currenciesAll = new ArrayList<>();

		endDay = shiftFixedHollidayDate(endDay);
		startDay = shiftFixedHollidayDate(startDay);

		String startDayQuery = "&date_from_day=" + startDay;
		String endDayQuery = "&date_to_day=" + endDay;

		for (String code : codes) {

			String currQuery = "&currency=" + code;
			String urlString = SRC + startDayQuery + endDayQuery + currQuery;
			List<Currency> currencies = getCurrencyRates(urlString);

			if (currencies == null)
				return null;
			
			LocalDate firstDay = startDay;
			
			if (!currencies.stream().anyMatch(c-> c.getDate().equals(firstDay))) {
				Currency currency = getRatesByDates(Arrays.asList(startDay.minusDays(1)), Arrays.asList(code)).get(0); 
			    currenciesAll.add(currency);
			}			
				currenciesAll.addAll(currencies);
			}
		
		return currenciesAll;
	}

	private List<Currency> getCurrencyRates(String urlString) throws MalformedURLException, IOException, JAXBException {
		HttpURLConnection http = fetchURL(urlString);

		if (http == null)
			return null;

		InputStream is = http.getInputStream();

		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);

		String xmlStr = br.lines().collect(Collectors.joining("\n"));
		StreamSource sb = new StreamSource(new StringReader(xmlStr));

		JAXBContext jaxbContext = JAXBContext.newInstance(Currencies.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		List<Currency> currencies = ((Currencies) jaxbUnmarshaller.unmarshal(sb)).getCurrencies();

		currencies.remove(0);

		return currencies;
	}

	public static Map<String, CurrencyRatesInfo> groupByCurrency(List<Currency> currencies, List<LocalDate> dates) {

		Map<String, CurrencyRatesInfo> currenciesRates = new HashMap<>();

		currencies.forEach((curr) -> {
			String code = curr.getCode();
			CurrencyRatesInfo currencyRates;
			if (!currenciesRates.containsKey(code)) {
				currencyRates = new CurrencyRatesInfo();
				currenciesRates.put(code, currencyRates);
			} else {
				currencyRates = currenciesRates.get(code);
			}
			Map<LocalDate, Double> rates = currencyRates.getRates();
			if (rates == null)
				rates = new HashMap<>();
			rates.put(curr.getDate(), curr.getRate());
			currencyRates.setRates(rates);
		});
	
		return currenciesRates;
	}

	private HttpURLConnection fetchURL(String url) throws IOException {
		URL dest = new URL(url);
		HttpURLConnection yc = (HttpURLConnection) dest.openConnection();
		yc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		yc.setInstanceFollowRedirects(false);
		yc.setUseCaches(false);

		int responseCode = yc.getResponseCode();
		if (responseCode >= 300) {
			return null;
		}
		return yc;
	}

	private LocalDate shiftFixedHollidayDate(LocalDate date) {
		if ((date.getMonthValue() == 1 || date.getMonthValue() == 5) && date.getDayOfMonth() == 1)
			shiftFixedHollidayDate(date.minusDays(1));

		if (date.getMonthValue() == 12 && (date.getDayOfMonth() == 25 || date.getDayOfMonth() == 26))
			shiftFixedHollidayDate(date.minusDays(1));

		if (date.getDayOfWeek().getValue() == 6) {
			date = date.minusDays(1);
		} else if (date.getDayOfWeek().getValue() == 7) {
			date = date.minusDays(2);
		}

		return date;
	}

}
