package uk.ac.ebi.ep.adapter.uniprot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.adapter.uniprot.UniprotJapiCallable.GetEntryCaller;
import uk.ac.ebi.ep.adapter.uniprot.UniprotJapiCallable.QueryEntryByIdCaller;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class UniprotJapiAdapter extends AbstractUniprotAdapter{

	private static final Logger LOGGER = Logger.getLogger(UniprotJapiAdapter.class);

	public List<EnzymeSummary> getEnzymesByIdPrefixes(List<String> queries,
			String defaultSpecies)
	throws MultiThreadingException {
	    ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<EnzymeSummary> ecs =
	    		new ExecutorCompletionService<EnzymeSummary>(pool);
	    List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
	    try {
	        List<Future<EnzymeSummary>> futures =
	        		new ArrayList<Future<EnzymeSummary>>();
			LOGGER.debug("SEARCH before uniprot callables loop");
	        for (String query:queries) {
	            Callable<EnzymeSummary> caller =
	            		new QueryEntryByIdCaller(query, defaultSpecies, config.isReviewed());
	            futures.add(ecs.submit(caller));
	        }
	        for (int i = 0; i < futures.size(); i++){
				try {
	                Future<EnzymeSummary> future =
	                		ecs.poll(config.getTimeout(), TimeUnit.SECONDS);
	                if (future != null){
	                	enzymeSummaryList.add(future.get());
	                } else {
	                	LOGGER.warn("SEACH job result not retrieved!");
	                }
				} catch (Exception e) {
	            	// Don't stop the others
	            	LOGGER.error("Callable " + (i+1) + " of " + futures.size()
	            			+ " - " + e.getMessage(), e);
				}
	        }
	        return enzymeSummaryList;
	    } finally {
	        pool.shutdown();
	    }
	}

	public EnzymeSummary getEnzymeSummary(String accession) {
       GetEntryCaller caller = new GetEntryCaller(accession);
        EnzymeSummary enzymeSummary = caller.getEnzymeTabData();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary;
    }
	
    public EnzymeSummary getEnzymeSummaryWithReactionPathway(String accession) {
       GetEntryCaller caller = new GetEntryCaller(accession);
        EnzymeSummary enzymeSummary = caller.getReactionPathwayByAccession();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary;

    }

    public EnzymeSummary getEnzymeSummaryWithMolecules(String accession) {
       GetEntryCaller caller = new GetEntryCaller(accession);
        EnzymeSummary enzymeSummary = caller.getSmallMoleculesByAccession();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary;
    }

    public EnzymeSummary getEnzymeSummaryWithProteinStructure(String accession) {
       GetEntryCaller caller = new GetEntryCaller(accession);
        EnzymeSummary enzymeSummary = caller.getProteinStructureByAccession();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary;
    }

    public List<String> getUniprotIds(String query) {
		return new UniprotJapiCallable().getUniprotIds(query, config.isReviewed());
	}

	/*
    public EnzymeSummary getDrugSummary(String accession) {
       GetEntriesCaller caller = new GetEntriesCaller(accession);
        EnzymeSummary enzymeSummary = caller.getEnzymePathwayByAccession();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary
    }
*/
    private void setRelatedSpecies(EnzymeSummary enzymeSummary) {
    	LOGGER.debug("SEARCH start setRelatedSpecies");
        String uniprotIdPrefix =  enzymeSummary.getUniprotid().split(ID_SPLIT_SYMBOL)[0];
        String defaultSpecies = enzymeSummary.getSpecies().getScientificname();
        String query = LuceneQueryBuilder
                .createWildcardFieldValueQuery(ID_FIELD, uniprotIdPrefix);
        QueryEntryByIdCaller caller =
        		new QueryEntryByIdCaller(query, defaultSpecies, config.isReviewed());
        enzymeSummary.setRelatedspecies(caller.getSpecies());
    	LOGGER.debug("SEARCH end setRelatedSpecies");
    }


}
