package uk.ac.ebi.ep.core.search.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import uk.ac.ebi.ep.adapter.intenz.IintenzAdapter;
import uk.ac.ebi.ep.adapter.intenz.IntenzAdapter;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 * Processor to add IntEnz synonyms to enzyme summaries.
 * @author rafa
 *
 */
public class SynonymsProcessor implements EnzymeSummaryProcessor {

	private Map<String, Set<String>> intenzSynonyms;

	/**
	 * @param enzymeSummaries the whole collection of summaries to process. This
	 * 		is needed in order to get the complete map of EC numbers to
	 * 		synonyms.
	 * @param intenzAdapter an IntEnz proxy which can be used to get the
	 * 		synonyms.
	 * @throws MultiThreadingException
	 */
	public SynonymsProcessor(List<EnzymeSummary> enzymeSummaries,
			IintenzAdapter intenzAdapter) throws MultiThreadingException{
        Set<String> ecSet = DataTypeConverter.getUniprotEcs(enzymeSummaries);
        intenzSynonyms = intenzAdapter.getSynonyms(ecSet);
	}
	
	public void process(EnzymeSummary summary) {
        // Merge UniProt's and IntEnz' synonyms:
        Set<String> uniqueSyns = new TreeSet<String>(summary.getSynonym());
        for (String ec : summary.getEc()) {
            Set<String> ecSynonyms = intenzSynonyms.get(ec);
            if (ecSynonyms != null) {
                uniqueSyns.addAll(ecSynonyms);
            }
        }
        summary.setSynonym(new ArrayList<String>(uniqueSyns));
	}

}
