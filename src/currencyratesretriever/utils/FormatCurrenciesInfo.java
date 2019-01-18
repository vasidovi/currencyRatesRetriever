/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.utils;

import currencyratesretriever.models.CurrencyRatesInfo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Dovile
 */
public class FormatCurrenciesInfo {

	public static String formatCurrenciesInfo(Map<String, CurrencyRatesInfo> currenciesRates) {

		String out = "";
		for (String code : currenciesRates.keySet()) {
			out += "Currency " + code + "\n";
			CurrencyRatesInfo ratesInfo = currenciesRates.get(code);

			LocalDate lastAnnouncedDay = ratesInfo.getLastAnnouncedDay();
			if (lastAnnouncedDay == null) {
				out += "No rates for selected days retrieved";
				continue;
			}
			out += "Date of latest currency rate anouncement " + lastAnnouncedDay + "\n";
			out += "Latest rate " + ratesInfo.getRates().get(lastAnnouncedDay) + "\n";

			LocalDate firstAnnouncedDay = ratesInfo.getFirstAnnouncedDay();

			Double ratesChange = ratesInfo.getRatesChange(firstAnnouncedDay, lastAnnouncedDay);

			if (ratesChange != null) {
				DecimalFormat df = new DecimalFormat("#.####");
				out += "Rate change since " + firstAnnouncedDay + " " + df.format(ratesChange) + "% \n";
				List<LocalDate> sortedDays = ratesInfo.getSortedDays();
				Collections.reverse(sortedDays);
				for (LocalDate day : sortedDays) {
					out += day + " : " + ratesInfo.getRates().get(day) + "\n";
				}
			}
		}

		return out;
	}

}