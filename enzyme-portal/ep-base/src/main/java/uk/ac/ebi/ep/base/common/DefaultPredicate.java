/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.domain.UniprotEntry;




/**
 *
 * @author joseph
 */@Deprecated
public class DefaultPredicate implements Predicate{
    
        /**
     * Collection of unique  IDs, which will act as a filter.
     */
    private final Collection<String> defaultFilter;

    public DefaultPredicate(Collection<String> defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    @Override
    public boolean evaluate(Object obj) {
        		if (defaultFilter == null || defaultFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof UniprotEntry){
			UniprotEntry es = (UniprotEntry) obj;
//			for (EnzymeAccession ea : es.getRelatedspecies()){
//				String sciName = ea.getSpecies().getScientificname();
//                                
//                                if (defaultFilter.contains(sciName)){
//					eval = true;
//					break;
//				}
//                             
//                                for(Disease disease : ea.getDiseases()){
//				if (defaultFilter.contains(disease.getName() )){
//					eval = true;
//					break;
//				}
//                                }
//                                
//                                for(Compound compounds : ea.getCompounds()){
//                                    	if (defaultFilter.contains(compounds.getName())){
//						eval = true;
//						break;
//					}
//                                }
//			}
		}
		return eval;
    }
    
}
