package uk.ac.ebi.ep.intenz.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeHierarchy;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.intenz.adapter.IntenzCallable.GetEcHierarchyCaller;
import uk.ac.ebi.ep.intenz.adapter.IntenzCallable.GetIntenzCaller;
import uk.ac.ebi.ep.intenz.adapter.IntenzCallable.GetSynonymsCaller;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.intenz.adapter.util.IntenzUtil;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class IntenzAdapter implements IintenzAdapter{


//********************************* VARIABLES ********************************//
	private static final Logger LOGGER = Logger.getLogger(IntenzAdapter.class);

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public Map<String, Set<String>> getSynonyms(
            Set<String> ecNumbers) throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Map<String, Set<String>> resultMap =
        		new Hashtable<String, Set<String>>();
        Map<String, Future<Set<String>>> futures =
        		new HashMap<String, Future<Set<String>>>();
        try {
        	LOGGER.debug("SEARCH synonyms before submitting callables");
	        for (String ecNumber:ecNumbers) {
	            Callable<Set<String>> callable = new GetSynonymsCaller(
	                    IntenzUtil.createIntenzEntryUrl(ecNumber));
	            Future<Set<String>> future = pool.submit(callable);
	            futures.put(ecNumber, future);
	        }
        	LOGGER.debug("SEARCH synonyms before getting futures");
	        for (String ecNumber : futures.keySet()) {
				try {
		            Set<String> synonyms = (Set<String>) futures.get(ecNumber).get();
	                if (synonyms.size() > 0) {
	                    resultMap.put(ecNumber, synonyms);
	                }
		        } catch (InterruptedException ex) {
		            throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
		        } catch (ExecutionException ex) {
		            throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
				}
			}
        	LOGGER.debug("SEARCH synonyms after getting futures");
        } finally {
        	pool.shutdown();
        }
        return resultMap;
    }

    public EnzymeModel getEnzymeDetails(EnzymeModel enzymeModel) throws MultiThreadingException {
        Set<String> ecList = DataTypeConverter.getUniprotEcs(enzymeModel);
        //DataTypeConverter.getUniprotEcs(enzymeModel);
        //Synonyms are merged into one list if there are more than 1 ec number
        Set<String> synonyms = new LinkedHashSet<String>();
        LOGGER.debug("SEARCH before getIntenz");
        List<Intenz> intenzList = getIntenz(ecList);
        LOGGER.debug("SEARCH after  getIntenz");
        List<EnzymeHierarchy> enzymeHierarchies = new ArrayList<EnzymeHierarchy>();
        if (intenzList.size() > 0) {
            LOGGER.debug("SEARCH before intenzList");
            for (Intenz intenz : intenzList) {
                GetSynonymsCaller synonymsCaller = new GetSynonymsCaller();
                GetEcHierarchyCaller ecCaller = new GetEcHierarchyCaller();
                LOGGER.debug("SEARCH before getSynonyms");
                synonyms.addAll(synonymsCaller.getSynonyms(intenz));
                LOGGER.debug("SEARCH before getEcHierarchy");
                EnzymeHierarchy enzymeHierarchy = ecCaller.getEcHierarchy(intenz);
                if (enzymeHierarchy != null) {
                    enzymeHierarchies.add(enzymeHierarchy);
                }
                LOGGER.debug("SEARCH after  getEcHierarchy");
            }
            LOGGER.debug("SEARCH after intenzList");
            if (synonyms.size() > 0) {
                enzymeModel.getSynonym().addAll(synonyms);
            }

            //Enzyme has previously initiatized to set Sequence info
            //Enzyme enzyme = new Enzyme();

            if (enzymeModel.getEnzyme()  == null) {
                Enzyme enzyme = new Enzyme();
                enzymeModel.setEnzyme(enzyme);
            }
            if (enzymeHierarchies.size() > 0) {
                enzymeModel.getEnzyme().setEchierarchies(enzymeHierarchies);
            }        
        }

        return enzymeModel;
    }


     public List<Intenz> getIntenz(Collection<String> ecList)
	 throws MultiThreadingException {
         List<Intenz> results = new ArrayList<Intenz>();
         ExecutorService pool = Executors.newCachedThreadPool();
         List<Future<Intenz>> futures = new ArrayList<Future<Intenz>>();
         try {
        	 LOGGER.debug("SEARCH before callables loop");
             for (String ec : ecList) {
                Callable<Intenz> callable = new GetIntenzCaller(
                    IntenzUtil.createIntenzEntryUrl(ec));
                 futures.add(pool.submit(callable));
             }
        	 LOGGER.debug("SEARCH before futures loop");
             for (Future<Intenz> future : futures) {
                 Intenz intenz = future.get();
                 if (intenz != null) {
                     results.add(intenz);
                 }
             }
        	 LOGGER.debug("SEARCH after  futures loop");
         } catch (InterruptedException ex) {
        	 throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
         } catch (ExecutionException ex) {
        	 throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
         } finally {
        	 pool.shutdown();
         }
         return results;
     }


}
