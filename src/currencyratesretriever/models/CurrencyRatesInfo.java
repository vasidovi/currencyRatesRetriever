package currencyratesretriever.models;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class CurrencyRatesInfo {
    @Getter
    @Setter
	Map<LocalDate, Double> rates;

	public List<LocalDate> getSortedDays() {
		List<LocalDate> sortedDays = (List) rates.keySet().stream().collect(Collectors.toList());
		Collections.sort(sortedDays);
		return sortedDays;
	}

	public LocalDate getLastAnnouncedDay() {
		LocalDate day = null;
		
		day = rates.keySet().stream().filter(key -> rates.get(key) != null).max(LocalDate::compareTo).get();

		return day;

	}

	public LocalDate getFirstAnnouncedDay() {
		LocalDate day = null;

		day = rates.keySet().stream().filter(key -> rates.get(key) != null).min(LocalDate::compareTo).get();

		return day;
	}

	public Double getRatesChange(LocalDate firstAnnouncedDay, LocalDate lastAnnouncedDay) {
		if (firstAnnouncedDay == null || firstAnnouncedDay.equals(lastAnnouncedDay))
			return null;
     
			return (rates.get(lastAnnouncedDay) / rates.get(firstAnnouncedDay)) * 100 - 100;
	}

}
