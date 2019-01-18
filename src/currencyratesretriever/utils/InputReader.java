/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.utils;

import currencyratesretriever.models.QueryParameters;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Dovile
 */
public class InputReader {

	private static final String GEN_INFO = "Enter information about currencies you wish to recieve info about ('exit' - to exit)";
	private static final String GET_CODES = "Currency code or codes sepparated by ',' (default USD)";
	private static final String INVALID_INPUT = "Invalid input, please try again, or exit";
	private static final String TYPE = "'P' - for period, 'D' - for specific dates (delfault P)";
	private static final String DATES_INFO_GEN = "Enter info about dates in pattern ";
	private static final String DATES_INFO_P = "start date : end date (YYYY-MM-dd)(default today : today)";
	private static final String DATES_INFO_D = "date1 , date2 , dateN (YYYD-MM-dd)(default today)";

	public static QueryParameters getQueryParameters() {
		System.out.println(GEN_INFO);
		Scanner in = new Scanner(System.in);

		QueryParameters parameters = new QueryParameters();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<LocalDate> dates = new ArrayList<>();
		LocalDate transferToEurDate = LocalDate.parse("2014-09-30");

		List<String> currencyCodes = getCurrencyCodes(GET_CODES, in);
		if (currencyCodes == null)
			return null;

		String type = getType(TYPE, in);

		if (type.isEmpty())
			return null;
		
		if (type.equals("period")) {
			dates = getDatesForPeriod(DATES_INFO_GEN + DATES_INFO_P, in, formatter);
			if (dates == null)
				return null;
		}
		if (type.equals("dates")) {
			dates = getDates(DATES_INFO_GEN + DATES_INFO_D, in, formatter);
			if (dates == null)
				return null;
		}
	
	parameters.setCodes(currencyCodes);
	parameters.setDays(dates);
	parameters.setType(type);

	return parameters;
	}

	private static String getParameters(String infoMessage, Scanner in) {
		System.out.println(infoMessage);
		String parameterStr = in.nextLine();

		return parameterStr;
	}

	private static List<LocalDate> getDates(String infoMessage, Scanner in, DateTimeFormatter formatter) {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate transferToEurDate = LocalDate.parse("2014-09-30");

		while (dates.isEmpty()) {
			String datesStr = getParameters(infoMessage, in);
			if (datesStr.trim().equalsIgnoreCase("exit")) {
				in.close();
				return null;
			} else if (datesStr.isEmpty()) {
				dates.add(LocalDate.now());
			} else {
				List<String> datesListStr = Arrays.asList(datesStr.split(","));
				for (String dateStr : datesListStr) {					
					LocalDate date;
					try {
						date = LocalDate.parse(dateStr.trim(), formatter);
						if (date.isBefore(transferToEurDate)) {
							date = transferToEurDate;
						}
						dates.add(date);
					} catch (Exception e) {
						System.out.println("failed parsing date " + dateStr);
					}
				}
			}
			if (dates.isEmpty()) {
				System.out.println(INVALID_INPUT);
			}
		}
		return dates;
	}

	private static List<LocalDate> getDatesForPeriod(String infoMessage, Scanner in, DateTimeFormatter formatter) {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate transferToEurDate = LocalDate.parse("2014-09-30");

		LocalDate startDate = null;
		LocalDate endDate = null;

		while (startDate == null && endDate == null) {
			String periodStr = getParameters(infoMessage, in);
			if (periodStr.trim().equalsIgnoreCase("exit")) {
				in.close();
				return null;
			} else if (periodStr.isEmpty()) {
				startDate = LocalDate.now();
				endDate = LocalDate.now();
			} else {

				String[] periodArr = periodStr.split(":");
				if (periodArr.length == 2) {
				try {
					startDate = LocalDate.parse(periodArr[0].trim(), formatter);
						if (startDate.isBefore(transferToEurDate)) {
							startDate = transferToEurDate;
						}
		
						endDate = LocalDate.parse(periodArr[1].trim(), formatter);
						if (endDate.isBefore(transferToEurDate)) {
							endDate = transferToEurDate;
						}
						if (endDate.isBefore(startDate)) {
							LocalDate temp = endDate;
							endDate = startDate;
							startDate = temp;
						}
					
				} catch (Exception e) {
					System.out.println(INVALID_INPUT + " " + periodArr[0] + " " + periodArr[1]);
				}
			}
		}
		}	
		dates.add(startDate);
		dates.add(endDate);

		return dates;
	}

	private static String getType(String infoMessage, Scanner in) {
		String type = "";

		while (type.isEmpty()) {
			type = getParameters(infoMessage, in);
			if (type.trim().equalsIgnoreCase("exit")) {
				in.close();
				return null;
			} else if (type.trim().equalsIgnoreCase("P") || type.isEmpty()) {
				type = "period";
			} else if (type.trim().equalsIgnoreCase("D")) {
				type = "dates";
			} else {
				type = "";
				System.out.println(INVALID_INPUT);
			}
		}
		return type;
	}

	private static List<String> getCurrencyCodes(String infoMessage, Scanner in) {

		List<String> currencyCodes = new ArrayList<>();

		while (currencyCodes.isEmpty()) {
			String codesStr = getParameters(infoMessage, in);
			if (codesStr.trim().equalsIgnoreCase("exit")) {
				in.close();
				return null;
			} else if (codesStr.isEmpty()) {
				currencyCodes.add("USD");
			}

			String[] codesArray = codesStr.split(",");
			for (String codeStr : codesArray) {
				codeStr = codeStr.toUpperCase().trim();
				if (codeStr.length() > 0) {
					currencyCodes.add(codeStr);
				}
			}
		}
		return currencyCodes;
	}

}
