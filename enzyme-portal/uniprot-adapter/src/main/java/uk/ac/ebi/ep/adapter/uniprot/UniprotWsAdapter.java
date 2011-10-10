package uk.ac.ebi.ep.adapter.uniprot;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * UniProt proxy using UniProt web services.
 * @author rafa
 *
 */
public class UniprotWsAdapter extends AbstractUniprotAdapter {

	/**
	 * URL to query the web service. It should include two spaceholders:
	 * <ul>
	 * 	<li>{0} for the query</li>
	 * 	<li>{1} for the
	 * <a href="http://www.uniprot.org/faq/28#retrieving_entries_via_queries">columns</a>
	 * to retrieve.</li>
	 * </ul>
	 */
	protected static final String UNIPROT_WS_URL =
			"http://www.uniprot.org/uniprot/?format=tab&sort=score" +
			"&query={0}&columns={1}";
	
	private static final Logger LOGGER = Logger.getLogger(UniprotWsAdapter.class);

	/** Pattern of species name(s) returned by the web service. */
	protected static final Pattern speciesPattern =
			Pattern.compile("([^()]+)(?: \\((.*)\\))?");
	
	public EnzymeSummary getEnzymeSummary(String accession) {
		return getEnzymeSummary(accession, Field.enzyme);
	}

	public EnzymeSummary getEnzymeSummaryWithReactionPathway(String accession) {
		return getEnzymeSummary(accession, Field.reactionsPathways);
	}

	public EnzymeSummary getEnzymeSummaryWithMolecules(String accession) {
		return getEnzymeSummary(accession, Field.molecules);
	}

	public EnzymeSummary getEnzymeSummaryWithProteinStructure(String accession) {
		return getEnzymeSummary(accession, Field.proteinStructure);
	}
	
	private EnzymeSummary getEnzymeSummary(String accession, Field field){
		return new UniprotWsSummaryCallable(accession, field, config.getTimeout())
			.getEnzymeSummary();
	}

	public List<EnzymeSummary> getEnzymesByIdPrefixes(List<String> queries,
			String defaultSpecies) throws MultiThreadingException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<String> getUniprotIds(String query){
		return new UniprotWsSearchCallable(query, config.isReviewed(), config.getTimeout())
			.getUniprotIds();
	}

}
