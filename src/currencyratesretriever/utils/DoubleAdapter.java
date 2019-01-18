/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Dovile
 */


public class DoubleAdapter extends XmlAdapter<String, Double> {


    @Override
    public Double unmarshal(String v) throws Exception {
         return Double.parseDouble(v.replace(",", "."));
    
    }

    @Override
    public String marshal(Double v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
