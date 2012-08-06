/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.core.filter;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.Disease;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 *
 * @author joseph
 */
public class SpecialFilterPredicate  implements Predicate{

     private Collection<String> specialFilter;

    public SpecialFilterPredicate(Collection<String> defaultFilter) {
        this.specialFilter = defaultFilter;
    }

    public boolean evaluate(Object obj) {
        		if (specialFilter == null || specialFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof EnzymeSummary){
			EnzymeSummary es = (EnzymeSummary) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				String sciName = ea.getSpecies().getScientificname();
                                if (specialFilter.contains(sciName)){
					eval = true;
					break;
				}
                                for(Disease disease : ea.getSpecies().getDiseases()){
				if (specialFilter.contains(disease.getName()) && specialFilter.contains(sciName) ){
					eval = true;
					break;
				}
                                }
                                
                                for(Compound compounds : ea.getSpecies().getCompounds()){
                                    	if (specialFilter.contains(compounds.getName()) && specialFilter.contains(sciName)){
						eval = true;
						break;
					}
                                }
			}
		}
		return eval;
    }
    
}
