
package uk.ac.ebi.ep.enzymes;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry;

/**
 *
 * @author Joseph
 */
public class EnzymeData {
    
    private List<uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry> entries;

    public EnzymeData(List<EnzymeEntry> entries) {
        this.entries = entries;
    }
    
    

    public List<EnzymeEntry> getEntries() {
        if(entries == null){
            entries = new ArrayList<>();
        }
        return entries;
    }

    public void setEntries(List<EnzymeEntry> entries) {
        this.entries = entries;
    }
    
    
}
