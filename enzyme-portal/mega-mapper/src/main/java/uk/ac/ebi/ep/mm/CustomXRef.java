/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joseph
 */
public class CustomXRef extends XRef {
    
    private Integer result_count;
    private List<String> idList;

    public CustomXRef() {
        super();
        idList = new ArrayList<String>();
    }

    public Integer getResult_count() {
        return result_count;
    }

    public void setResult_count(Integer result_count) {
        this.result_count = result_count;
    }

    public List<String> getIdList() {
        return idList;
    }
    
}
