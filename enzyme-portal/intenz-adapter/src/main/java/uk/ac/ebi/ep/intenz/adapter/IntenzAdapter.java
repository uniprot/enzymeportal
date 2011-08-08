package uk.ac.ebi.ep.intenz.adapter;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import uk.ac.ebi.ep.enzyme.model.EcClass;
import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.intenz.adapter.IntenzCallable.GetSynonymsCaller;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.intenz.adapter.util.IntenzUtil;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;

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
        Map<String, Set<String>> resultMap = null;
        try {
            resultMap = new Hashtable<String, Set<String>>();
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
        }
        finally {
            pool.shutdown();
        }
        return resultMap;
    }

    public EnzymeModel getEcDetails(EnzymeModel enzymeModel) {
        String ec = enzymeModel.getEc().get(0);
        GetSynonymsCaller callable = new GetSynonymsCaller(
                 IntenzUtil.createIntenzEntryUrl(ec));
        Intenz intenz = callable.getData();
        Set<String> synonym = callable.getSynonyms(intenz);
        if (synonym.size() > 0) {
            enzymeModel.getSynonym().addAll(synonym);
        }

        List<EcClass> ecClassList = callable.getEcClass(intenz);

        Enzyme enzyme = new Enzyme();
        if (ecClassList.size() > 0) {
            enzyme.setEcclasshierarchy(ecClassList);
        }
        enzymeModel.setEnzyme(enzyme);

        return enzymeModel;
    }
}
