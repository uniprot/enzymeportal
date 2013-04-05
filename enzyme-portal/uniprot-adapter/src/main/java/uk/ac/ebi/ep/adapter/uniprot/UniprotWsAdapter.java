package uk.ac.ebi.ep.adapter.uniprot;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import uk.ac.ebi.ep.adapter.uniprot.UniprotWsSummaryCallable.IdType;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.util.EPUtil;

/**
 * UniProt proxy using UniProt web services.
 * @author rafa
 *
 */
public class UniprotWsAdapter extends AbstractUniprotAdapter {
	
	/**
	 * Columns retrieved from the web service (tab format).
	 * @author rafa
	 */
	enum QueryColumn {
		/** UniProt accession. */
		id,
		/** UniProt entry name (formerly known as id). */
		entry_name,
		/** Species. */
		organism
	}

	/**
	 * Fields used in queries to the UniProt web service.
	 * @author rafa
	 */
	enum QueryField {
		/** UniProt accession. */
		accession,
		/** UniProt entry name (formerly known as id). */
		mnemonic
	}

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
	protected static final Pattern SPECIES_PATTERN =
			Pattern.compile("([^()]+)" +
					"( \\((?:strain|isolate) [^()]+(?:\\([^()]+\\))?[^()]*\\))?" +
					"(?: \\(([^()]+)\\))?" +
					"(?: \\(([^()]+(?:\\([^()]+\\))?)\\))*");

    public EnzymeSummary getEnzymeSummary(String accession)
	throws UniprotWsException {
		return getEnzymeSummary(accession, Field.enzyme);
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * <b>WARNING:</b>This implementation does not currently populate properly
     * the model, but just adds Reactome IDs (Pathway objects within one single
	 * ReactionPathway object).
	 * @throws UniprotWsException 
	 * @see UniprotWsSummaryCallable#parseReactionPathways implementation.
	 */
	public EnzymeSummary getEnzymeSummaryWithReactionPathway(String accession)
	throws UniprotWsException {
		return getEnzymeSummary(accession, Field.reactionsPathways);
	}

	public EnzymeSummary getEnzymeSummaryWithMolecules(String accession)
	throws UniprotWsException {
		return getEnzymeSummary(accession, Field.molecules);
	}

	public EnzymeSummary getEnzymeSummaryWithProteinStructure(String accession)
	throws UniprotWsException {
		return getEnzymeSummary(accession, Field.proteinStructure);
	}
	
	private EnzymeSummary getEnzymeSummary(String accession, Field field)
	throws UniprotWsException{
		return new UniprotWsSummaryCallable(accession, IdType.ACCESSION, field,
				null, config)
			.getEnzymeSummary();
	}

	public List<EnzymeSummary> getEnzymesByIdPrefixes(List<String> idPrefixes,
			String defaultSpecies, Collection<String> speciesFilter)
    throws MultiThreadingException {
	    ExecutorService pool =
	    		Executors.newFixedThreadPool(config.getMaxThreads());
	    CompletionService<EnzymeSummary> ecs =
	    		new ExecutorCompletionService<EnzymeSummary>(pool);
	    // We must keep the order of the summaries, the same as the ID prefixes:
	    Map<Future<EnzymeSummary>, EnzymeSummary> future2summary =
	    		new LinkedHashMap<Future<EnzymeSummary>, EnzymeSummary>();
	    Connection mmConnection = getMmConnection();
	    try {
	    	LOGGER.debug("Submitting summary callables...");
	    	for (String query : idPrefixes) {
                UniprotWsSummaryCallable callable =
                        new UniprotWsSummaryCallable(query+"_*",
                        IdType.ENTRY_NAME, Field.brief,
						defaultSpecies, config);
                callable.setMmConnection(mmConnection);
                future2summary.put(ecs.submit(callable), null);
			}
			LOGGER.debug("Polling EnzymeSummary Futures...");
	    	for (int i = 0; i < idPrefixes.size(); i++){
	    		Future<EnzymeSummary> future = null;
	    		try {
	    			LOGGER.debug("Polling EnzymeSummary Future...");
	    			future = ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
	    			if (future != null){
	    				LOGGER.debug("Getting summary from future...");
	    				final EnzymeSummary summary = future.get();
	    				LOGGER.debug("Got summary from future");
	    				if (summary != null){
	    					future2summary.put(future, summary);
	    				} else {
		    				LOGGER.warn("SEARCH null summary!");
			            	future2summary.remove(future);
	    				}
	    			} else {
	    				LOGGER.warn("SEARCH job result not retrieved!");
	    			}
	    		} catch (Exception e){
	            	// Don't stop the others
	            	LOGGER.error("Callable " + (i+1) + " of " + idPrefixes.size()
	            			+ " - " + e.getMessage(), e);
	            	if (future != null) future2summary.remove(future);
	    		}
	    	}
	    	LOGGER.debug("Polled all futures");
	    	return new ArrayList<EnzymeSummary>(future2summary.values());
	    } finally {
	        if (mmConnection != null) try {
                mmConnection.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close connection to mega-map", e);
            }
            pool.shutdown();
	    }
	}
	
	public List<String> getUniprotAccessions(String query) {
		return new UniprotWsSearchAccessionsCallable(query, config).getAccessions();
	}

	public List<String> getUniprotIds(String query){
		return new UniprotWsSearchIdsCallable(query, config).getIds();
	}

	public Collection<Species> getSpecies(Collection<String> idPrefixes) {
		HashMap<String,Species> species = null;
		List<String> wIds = EPUtil.getWildcardIds(idPrefixes);
		// Distribute the query in chunks:
		List<String> queries = prepareManyQueries(wIds, QueryField.mnemonic);
	    ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<Map<String,Species>> ecs =
	    		new ExecutorCompletionService<Map<String,Species>>(pool);
	    try {
	    	for (String query : queries) {
				UniprotWsSearchAccessionsCallable callable =
						new UniprotWsSearchAccessionsCallable(query, config);
				ecs.submit(callable);
			}
	    	for (int i = 0; i < queries.size(); i++) {
				try {
					Future<Map<String,Species>> future =
							ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
	    			if (future != null){
	    				final Map<String, Species> acc2sp = future.get();
	    				if (acc2sp != null){
	    					if (species == null){
	    						// Species is auto-generated from XML schema,
	    						// not suitable for Set.
	    						species = new HashMap<String,Species>();
	    					}
	    					for (Species sp : acc2sp.values()) {
								species.put(sp.getScientificname(), sp);
							}
	    				}
	    			} else {
	    				LOGGER.warn("SEARCH job result not retrieved!");
	    			}
				} catch (Exception e){
	    			// Don't stop the others:
	    			LOGGER.error("Callable " + (i+1) + " of " + queries.size()
	            			+ " - " + e.getMessage(), e);
				}
			}
		} finally {
			pool.shutdown();
		}
		return species == null? null : new HashSet<Species>(species.values());
	}

	public Map<String, Species> getIdsAndSpecies(Collection<String> accs){
		Map<String, Species> id2species = new HashMap<String, Species>();
		// Distribute the query in chunks:
		List<String> queries = prepareManyQueries(accs, QueryField.accession);
	    ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<Map<String, Species>> ecs =
	    		new ExecutorCompletionService<Map<String, Species>>(pool);
	    try {
	    	for (String query : queries){
	    		Callable<Map<String, Species>> callable =
	    				new UniprotWsSearchIdsCallable(query, config);
	    		ecs.submit(callable);
	    	}
	    	for (int i = 0; i < queries.size(); i++){
	    		try {
	    			Future<Map<String, Species>> future =
	    					ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
	    			if (future != null){
	    				final Map<String, Species> more = future.get();
	    				if (more != null) id2species.putAll(more);
	    			} else {
	    				LOGGER.warn("SEARCH job result not retrieved!");
	    			}
	    		} catch (Exception e){
	    			// Don't stop the others:
	    			LOGGER.error("Callable " + (i+1) + " of " + queries.size()
	            			+ " - " + e.getMessage(), e);
	    		}
	    	}
	    } finally {
	    	pool.shutdown();
	    }
		return id2species;
	}

	/**
	 * Prepares queries for the web service. If the number of query terms is
	 * too big (see {@link UniprotConfig#setMaxTermsPerQuery(int)} it
	 * distributes the load in more than one query.
	 * @param terms the query terms.
	 * @param field the queried field.
	 * @return a list of queries ready to be sent to the web service.
	 */
	private List<String> prepareManyQueries(Collection<String> terms, QueryField field) {
		List<String> queries = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int j = 0;
		for (String acc : terms) {
			if (sb.length() > 0) sb.append("+OR+");
			sb.append(field.name()).append(':').append(acc);
			if ((j+1) % config.getMaxTermsPerQuery() == 0 || (j+1) == terms.size()){
				// End this query:
				sb.insert(0, '(');
				sb.append(')');
				queries.add(sb.toString());
				if ((j+1) < terms.size()){
					// Start another one:
					sb = new StringBuilder("(");
				}
			}
			j++;
		}
		return queries;
	}

	/**
	 * Parses the string returned by the UniProt web service for the
	 * organism column.
	 * @param speciesTxt
	 * @return a Species object.
	 */
	protected static Species parseSpecies(String speciesTxt){
		Species species = null;
		Matcher m = SPECIES_PATTERN.matcher(speciesTxt);
        species = new Species();
		if (m.matches()){
	        String strain = "";
	        if (m.group(2) != null){
	        	strain = m.group(2);
	        }
	        species.setScientificname(m.group(1) + strain);
	        if (m.group(3) != null){
		        species.setCommonname(m.group(3));
	        }
	        if (m.group(4) != null){
	        	// What can we do with another scientific name?
	        }
		} else {
			LOGGER.warn("Species text does not match pattern: " + speciesTxt);
			species.setScientificname(speciesTxt);
		}
		return species;
	}
}
