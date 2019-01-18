/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.models;

import currencyratesretriever.utils.DateAdapter;
import currencyratesretriever.utils.DoubleAdapter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Dovile
 */

@Getter
@Setter
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)

public class Currency {

	@XmlElement(name = "pavadinimas")
	private String name;
	@XmlElement(name = "valiutos_kodas")
	private String code;

	@XmlElement(name = "santykis")
	@XmlJavaTypeAdapter(DoubleAdapter.class)
	private Double rate;

	@XmlElement(name = "data")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate date;

	public Currency() {
	}

	public Currency(String name, String code, Double rate, LocalDate date) {
		this.name = name;
		this.code = code;
		this.rate = rate;
		this.date = date;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (!(that instanceof Currency))
			return false;
		Currency thatCurrency = (Currency) that;

		return this.code.equals(thatCurrency.code) && this.rate.equals(thatCurrency.rate)
				&& this.date.equals(thatCurrency.date);
	}

	@Override
	public String toString() {
		return "Currency{" + "name=" + name + ", code=" + code + ", rate=" + rate + ", date=" + date + '}';
	}

}
