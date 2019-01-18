/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyratesretriever.models;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Dovile
 */
@Getter
@Setter
public class QueryParameters {
    private List<String> codes;
    private List<LocalDate> days;
    private String type;  
}
