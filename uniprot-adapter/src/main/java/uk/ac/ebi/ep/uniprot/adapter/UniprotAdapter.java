package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.uniprot.adapter.UniprotCallable.GetEntriesCaller;
import uk.ac.ebi.ep.uniprot.adapter.UniprotCallable.QueryEntryByIdCaller;
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


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeSummary getEnzymeSummary(String accession) {
       GetEntriesCaller caller = new GetEntriesCaller(accession);
        EnzymeSummary enzymeSummary = caller.getEnzymeEntry(true);
        setRelatedSpecies(enzymeSummary);
       return enzymeSummary;
    }

    public void setRelatedSpecies(EnzymeSummary enzymeSummary) {
        String uniprotIdPrefix =  enzymeSummary.getUniprotid().split(ID_SPLIT_SYMBOL)[0];
        String defaultSpecies = enzymeSummary.getSpecies().getScientificname();
        String query = LuceneQueryBuilder
                .createWildcardFieldValueQuery(IUniprotAdapter.ID_FIELD,uniprotIdPrefix);
        QueryEntryByIdCaller caller = new QueryEntryByIdCaller(query, defaultSpecies);
        enzymeSummary.setRelatedspecies( caller.getSpecies());
    }
    public List<EnzymeSummary> queryEnzymeByIdPrefixes(List<String> queries, String defaultSpecies)
            throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        try {
            for (String query:queries) {
                Callable caller = new UniprotCallable.QueryEntryByIdCaller(query, defaultSpecies);
                Future<EnzymeSummary> future = pool.submit(caller);
                EnzymeSummary enzymeSummary = null;
                try {
                    enzymeSummary = future.get(IUniprotAdapter.ENTRY_TIMEOUT, TimeUnit.SECONDS);
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
                if (enzymeSummary != null) {
                    enzymeSummaryList.add(enzymeSummary);
                }
            }
            return enzymeSummaryList;
        }
        finally {
            pool.shutdown();
        }
    }


/*
    public List<EnzymeSummary> getEnzymeEntries(List<Result> briefResultList)
            throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        try {
            for (Result result:briefResultList) {
                String primaryAccession = result.getAcc().get(0);
                Callable caller = new GetEntriesCaller(primaryAccession);
                Future<EnzymeSummary> future = pool.submit(caller);
                EnzymeSummary enzymeSummary;
                try {
                    enzymeSummary = future.get(IUniprotAdapter.ENTRY_TIMEOUT, TimeUnit.SECONDS);
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
                enzymeSummary.setUniprotid(result.getId());
                //enzymeSummary.getPdbeaccession(result.get)
                enzymeSummaryList.add(enzymeSummary);
            }
            return enzymeSummaryList;
        }
        finally {
            pool.shutdown();
        }
    }
    */
/*
    public List<EnzymeSummary> getEnzymeEntries(Map<String>,List<Result>> briefResultList)
            throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        try {
            for (Result result:briefResultList) {
                String primaryAccession = result.getAcc().get(0);
                Callable caller = new GetEntriesCaller(primaryAccession);
                Future<EnzymeSummary> future = pool.submit(caller);
                EnzymeSummary enzymeSummary;
                try {
                    enzymeSummary = future.get(IUniprotAdapter.ENTRY_TIMEOUT, TimeUnit.SECONDS);
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
                enzymeSummary.setUniprotid(result.getId());
                //enzymeSummary.getPdbeaccession(result.get)
                enzymeSummaryList.add(enzymeSummary);
            }
            return enzymeSummaryList;
        }
        finally {
            pool.shutdown();
        }
    }
 * 
 */
}
