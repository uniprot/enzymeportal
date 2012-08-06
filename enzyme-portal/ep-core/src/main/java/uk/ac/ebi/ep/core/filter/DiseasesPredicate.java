package uk.ac.ebi.ep.core.filter;

import java.util.Collection;

import org.apache.commons.collections.Predicate;

import uk.ac.ebi.ep.search.model.Disease;
//import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

public class DiseasesPredicate implements Predicate {

    /**
     * Collection of unique diseases IDs, which will act as a filter.
     */
    private Collection<String> diseasesFilter;

    public DiseasesPredicate(Collection<String> diseasesFilter) {
        this.diseasesFilter = diseasesFilter;
    }

    /**
     * Evaluates whether an enzyme summary contains any of the diseases in the
     * filter.
     *
     * @param obj an EnzymeSummary object.
     * @return
     * <code>true</code> if the enzyme summary contains any of the diseases in
     * the filter,
     * <code>false</code> otherwise.
     */
//	public boolean evaluate(Object obj) {
//		if (diseasesFilter == null || diseasesFilter.isEmpty()){
//			return true;
//		}
//		boolean eval = false;
//		if (obj instanceof EnzymeSummary){
//			EnzymeSummary es = (EnzymeSummary) obj;
//			for (EnzymeAccession ea : es.getRelatedspecies()){
//				for (Disease disease : ea.getDiseases()) {
//					String id = disease.getId();
//					if (diseasesFilter.contains(id)){
//						eval = true;
//						break;
//					}
//				}
//			}
//		}
//		return eval;
//	}

    
    	public boolean evaluate(Object obj) {
		if (diseasesFilter == null || diseasesFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof EnzymeSummary){
			EnzymeSummary es = (EnzymeSummary) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				//String sciName = ea.getSpecies().getScientificname();
                                for(Disease disease : ea.getSpecies().getDiseases()){
				if (diseasesFilter.contains(disease.getName())){
					eval = true;
					break;
				}
                                }
			}
		}
		return eval;
	}
}
