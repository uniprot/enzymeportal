package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;

public class DiseasesPredicate implements Predicate {

    /**
     * Collection of unique diseases IDs, which will act as a filter.
     */
    private final Collection<String> diseasesFilter;

    public DiseasesPredicate(Collection<String> diseasesFilter) {
        this.diseasesFilter = diseasesFilter;
    }

    /**
     * Evaluates whether an enzyme summary contains any of the diseases in the
     * filter.
     *
     * @param obj an EnzymeSummary object.
     * @return <code>true</code> if the enzyme summary contains any of the
     * diseases in the filter, <code>false</code> otherwise.
     */
    @Override
	public boolean evaluate(Object obj) {
		if (diseasesFilter == null || diseasesFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof UniprotEntry){
			UniprotEntry es = (UniprotEntry) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				for (Disease disease : ea.getDiseases()) {
					//String name = disease.getName().replaceAll(",", "").split("\\(")[0];
                                        String id = disease.getId();
					if (diseasesFilter.contains(id)){
						eval = true;
						break;
					}
				}
			}
		}
		return eval;
	}
//    @Override
//    public boolean evaluate(Object obj) {
//        if (diseasesFilter == null || diseasesFilter.isEmpty()) {
//            return true;
//        }
//   
//        boolean eval = false;
//        if (obj instanceof EnzymeSummary) {
//            EnzymeSummary es = (EnzymeSummary) obj;
//
//            for (Disease disease : es.getDiseases()) {
//          
//                if (diseasesFilter.contains(disease.getName())) {
//                    eval = true;
//                    break;
//                }
//            }
//
//        }
//        return eval;
//    }
}
