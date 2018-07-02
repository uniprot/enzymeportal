/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.download;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph
 */
@XmlRootElement (name="enzymes")
public class EnzymeCentricList {
    
    private List<EnzymeCentric> enzymes = new ArrayList<>();

    public List<EnzymeCentric> getEnzymes() {
        return enzymes;
    }

    public void setEnzymes(List<EnzymeCentric> enzymes) {
        this.enzymes = enzymes;
    }
    
    
}
