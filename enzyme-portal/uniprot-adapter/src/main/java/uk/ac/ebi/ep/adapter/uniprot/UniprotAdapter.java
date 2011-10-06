package uk.ac.ebi.ep.adapter.uniprot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.adapter.uniprot.UniprotCallable.GetEntryCaller;
import uk.ac.ebi.ep.adapter.uniprot.UniprotCallable.QueryEntryByIdCaller;
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
public class UniprotAdapter implements IUniprotAdapter{


//********************************* VARIABLES ********************************//
	private static final Logger LOGGER = Logger.getLogger(UniprotAdapter.class);

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

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

/*
    public EnzymeSummary getDrugSummary(String accession) {
       GetEntriesCaller caller = new GetEntriesCaller(accession);
        EnzymeSummary enzymeSummary = caller.getEnzymePathwayByAccession();
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary
    }
*/
    public void setRelatedSpecies(EnzymeSummary enzymeSummary) {
    	LOGGER.debug("SEARCH start setRelatedSpecies");
        String uniprotIdPrefix =  enzymeSummary.getUniprotid().split(ID_SPLIT_SYMBOL)[0];
        String defaultSpecies = enzymeSummary.getSpecies().getScientificname();
        String query = LuceneQueryBuilder
                .createWildcardFieldValueQuery(IUniprotAdapter.ID_FIELD,uniprotIdPrefix);
        QueryEntryByIdCaller caller = new QueryEntryByIdCaller(query, defaultSpecies);
        enzymeSummary.setRelatedspecies( caller.getSpecies());
    	LOGGER.debug("SEARCH end setRelatedSpecies");
    }
    
    public List<EnzymeSummary> queryEnzymeByIdPrefixes(List<String> queries,
    		String defaultSpecies)
    throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        try {
            List<Future<EnzymeSummary>> futures =
            		new ArrayList<Future<EnzymeSummary>>();
			LOGGER.debug("SEARCH before uniprot callables loop");
            for (String query:queries) {
                Callable<EnzymeSummary> caller =
                		new UniprotCallable.QueryEntryByIdCaller(query, defaultSpecies);
                futures.add(pool.submit(caller));
            }
			try {
				LOGGER.debug("SEARCH before uniprot futures loop");
	            for (Future<EnzymeSummary> future : futures) {
	                EnzymeSummary enzymeSummary = future.get(
	                		IUniprotAdapter.ENTRY_TIMEOUT, TimeUnit.SECONDS);
	                if (enzymeSummary != null) {
	                    enzymeSummaryList.add(enzymeSummary);
	                }
	            }
	            LOGGER.debug("SEARCH after  uniprot futures loop");
            } catch (InterruptedException ex) {
                throw new MultiThreadingException(
                        "One of Uniprot get entry thread was interupted! ", ex);
            } catch (ExecutionException ex) {
                throw new MultiThreadingException(
                        "One of Uniprot get entry thread was not executed! ", ex);
            } catch (TimeoutException ex) {
                throw new MultiThreadingException(
                        "One of Uniprot get entry thread did not return the result" +
                        " before timeout! ", ex);
            }
            return enzymeSummaryList;
        } finally {
            pool.shutdown();
        }
    }


}
