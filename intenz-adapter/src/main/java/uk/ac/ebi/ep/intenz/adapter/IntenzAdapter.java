package uk.ac.ebi.ep.intenz.adapter;

import java.util.ArrayList;
import java.util.Collection;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.ep.enzyme.model.EcClass;
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

    public EnzymeModel getEnzymeDetails(EnzymeModel enzymeModel) throws MultiThreadingException {
        Set<String> ecList = DataTypeConverter.getUniprotEcs(enzymeModel);

        DataTypeConverter.getUniprotEcs(enzymeModel);
        //Synonyms are merged into one list if there are more than 1 ec number
        Set<String> synonyms = new LinkedHashSet<String>();

        List<Intenz> intenzList = getIntenz(ecList);

        List<EnzymeHierarchy> enzymeHierarchies = new ArrayList<EnzymeHierarchy>();

        for (Intenz intenz : intenzList) {
            GetSynonymsCaller synonymsCaller = new GetSynonymsCaller();
            GetEcHierarchyCaller ecCaller = new GetEcHierarchyCaller();

            //Intenz intenz = intenzCaller.getData();

            synonyms.addAll(synonymsCaller.getSynonyms(intenz));

            EnzymeHierarchy enzymeHierarchy = ecCaller.getEcHierarchy(intenz);
            if (enzymeHierarchy != null) {
                enzymeHierarchies.add(enzymeHierarchy);
            }

        }

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

        return enzymeModel;
    }


     public List<Intenz> getIntenz(Collection<String> ecList) throws MultiThreadingException {
         List<Intenz> results = new ArrayList<Intenz>();
         ExecutorService pool = Executors.newCachedThreadPool();
         try {
             for (String ec : ecList) {
                Callable callable = new GetIntenzCaller(
                    IntenzUtil.createIntenzEntryUrl(ec));
                 Future<Intenz> future = pool.submit(callable);
                 results.add(future.get());
             }
         }
        catch (InterruptedException ex) {
            throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
        } catch (ExecutionException ex) {
            throw new MultiThreadingException(IintenzAdapter.FAILED_MSG, ex);
        }
         finally {
             pool.shutdown();
         }
         return results;
     }


}
