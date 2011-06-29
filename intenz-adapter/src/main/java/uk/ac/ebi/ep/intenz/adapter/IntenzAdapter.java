package uk.ac.ebi.ep.intenz.adapter;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import uk.ac.ebi.ep.intenz.adapter.IntenzCallable.GetSynonymsCaller;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.intenz.adapter.util.IntenzUtil;

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


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public Map<String, Set<String>> getSynonyms(
            Set<String> ecNumbers) throws MultiThreadingException {
        ExecutorService pool =  pool = Executors.newCachedThreadPool();
        Map<String, Set<String>> resultMap = new Hashtable<String, Set<String>>();
        for (String ecNumber:ecNumbers) {
            Callable<Set<String>> callable = new GetSynonymsCaller(
                    IntenzUtil.createIntenzEntryUrl(ecNumber));
            Future<Set<String>>future = pool.submit(callable);
            try {
                Set<String> synonyms = (Set<String>) future.get();
                if (synonyms.size()>0) {
                    resultMap.put(ecNumber, (Set<String>) future.get());
                }
            } catch (InterruptedException ex) {
                throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
            } catch (ExecutionException ex) {
                throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
            }
        }
        return resultMap;
    }
}
