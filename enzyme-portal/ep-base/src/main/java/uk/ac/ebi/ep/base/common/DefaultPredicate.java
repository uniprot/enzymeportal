/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;




/**
 *
 * @author joseph
 */
public class DefaultPredicate implements Predicate{
    
        /**
     * Collection of unique  IDs, which will act as a filter.
     */
    private Collection<String> defaultFilter;

    public DefaultPredicate(Collection<String> defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public boolean evaluate(Object obj) {
        		if (defaultFilter == null || defaultFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof EnzymeSummary){
			EnzymeSummary es = (EnzymeSummary) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				String sciName = ea.getSpecies().getScientificname();
                                
                                if (defaultFilter.contains(sciName)){
					eval = true;
					break;
				}
                                System.out.println("species "+ ea.getSpecies());
                                System.out.println("disease filter "+ ea.getSpecies().getDiseases().size() );
                                for(Disease disease : ea.getSpecies().getDiseases()){
				if (defaultFilter.contains(disease.getName() )){
					eval = true;
					break;
				}
                                }
                                
                                for(Compound compounds : ea.getSpecies().getCompounds()){
                                    	if (defaultFilter.contains(compounds.getName())){
						eval = true;
						break;
					}
                                }
			}
		}
		return eval;
    }
    
}
