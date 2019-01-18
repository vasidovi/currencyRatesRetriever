/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.models;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Dovile
 */
@Setter
@Getter
@XmlRootElement(name = "items")
@XmlAccessorType (XmlAccessType.FIELD)
public class Currencies {
    @XmlElement(name = "item")
    private List<Currency> currencies = null;
    
}
