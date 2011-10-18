package uk.ac.ebi.ep.adapter.uniprot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.adapter.uniprot.UniprotWsSummaryCallable.IdType;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * UniProt proxy using UniProt web services.
 * @author rafa
 *
 */
public class UniprotWsAdapter extends AbstractUniprotAdapter {
	
	private static final Logger LOGGER = Logger.getLogger(UniprotWsAdapter.class);

	/** Pattern of species name(s) returned by the web service. It can get the
	 * following groups:
	 * <ol>
	 * 	<li>scientific name</li>
	 * 	<li>strain/isolate info (when available)</li>
	 * 	<li>common name (when available)</li>
	 * 	<li>alternative scientific name(s) (when available). Might include
	 * 		strain info. If more than one, only the last one is taken.</li>
	 * </ol>
	 */
	protected static final Pattern speciesPattern =
			Pattern.compile("([^()]+)" +
					"( \\((?:strain|isolate) [^()]+(?:\\([^()]+\\))?[^()]*\\))?" +
					"(?: \\(([^()]+)\\))?" +
					"(?: \\(([^()]+(?:\\([^()]+\\))?)\\))*");
	
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
		return new UniprotWsSummaryCallable(accession, IdType.ACCESSION, field,
				null, config)
			.getEnzymeSummary();
	}

	public List<EnzymeSummary> getEnzymesByIdPrefixes(List<String> idPrefixes,
			String defaultSpecies, Collection<String> speciesFilter)
	throws MultiThreadingException {
		// TODO use speciesFilter
	    ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<EnzymeSummary> ecs =
	    		new ExecutorCompletionService<EnzymeSummary>(pool);
	    List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
	    try {
	    	for (String query : idPrefixes) {
				Callable<EnzymeSummary> callable = new UniprotWsSummaryCallable(
						query+"_*", IdType.ENTRY_NAME, Field.brief,
						defaultSpecies, config);
				ecs.submit(callable);
			}
	    	for (int i = 0; i < idPrefixes.size(); i++){
	    		try {
	    			Future<EnzymeSummary> future =
	    					ecs.poll(config.getTimeout(), TimeUnit.SECONDS);
	    			if (future != null){
	    				enzymeSummaryList.add(future.get());
	    			} else {
	    				LOGGER.warn("SEARCH job result not retrieved!");
	    			}
	    		} catch (Exception e){
	            	// Don't stop the others
	            	LOGGER.error("Callable " + (i+1) + " of " + idPrefixes.size()
	            			+ " - " + e.getMessage(), e);
	    		}
	    	}
	    	return enzymeSummaryList;
	    } finally {
	    	pool.shutdown();
	    }
	}
	
	public List<String> getUniprotIds(String query){
		return new UniprotWsSearchCallable(query, config)
			.getUniprotIds();
	}

}
