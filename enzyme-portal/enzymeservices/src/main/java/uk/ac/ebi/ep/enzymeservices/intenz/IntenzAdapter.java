package uk.ac.ebi.ep.enzymeservices.intenz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.data.enzyme.model.Enzyme;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeHierarchy;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.Molecule;
import uk.ac.ebi.ep.data.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzCallable.GetCofactorsCaller;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzCallable.GetEcHierarchyCaller;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzCallable.GetIntenzCaller;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzCallable.GetSynonymsCaller;
import uk.ac.ebi.ep.enzymeservices.util.IntenzUtil;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;

/**
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/>
 * $LastChangedDate$ <br/>
 * $Author$
 * @author $Author$
 */
public class IntenzAdapter implements IintenzAdapter {

    private IntenzConfig config;

    private static final Logger LOGGER = Logger.getLogger(IntenzAdapter.class);

    public IntenzConfig getConfig() {
        return config;
    }

    public void setConfig(IntenzConfig config) {
        this.config = config;
    }

    public IntenzAdapter() {
        config = new IntenzConfig();
    }

    public Map<String, Set<String>> getSynonyms(Set<String> ecNumbers)
            throws MultiThreadingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        //System.out.println("adding synomn from intenz "+ config);
        CompletionService<Set<String>> ecs
                = new ExecutorCompletionService<Set<String>>(pool);
        Map<String, Set<String>> resultMap
                = new Hashtable<String, Set<String>>();
        Map<Future<Set<String>>, String> future2ec
                = new HashMap<Future<Set<String>>, String>();
        try {
            for (String ecNumber : ecNumbers) {
                // Avoid dashes, there are no IntEnzXML files for those:
                if (ecNumber.indexOf('-') > -1) {
                    continue;
                }
                Callable<Set<String>> callable = new GetSynonymsCaller(
                        IntenzUtil.createIntenzEntryUrl(
                                config.getIntenzXmlUrl(), ecNumber));
                future2ec.put(ecs.submit(callable), ecNumber);
            }
            for (int i = 0; i < future2ec.size(); i++) {
                try {
                    Future<Set<String>> future
                            = ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
                    String ec = future2ec.get(future);
                    if (future != null) {
                        Set<String> synonyms = future.get();
                        if (synonyms.size() > 0) {
                            resultMap.put(ec, synonyms);
                        }
                    } else {
                        LOGGER.warn("Job not returned getting synonyms!");
                    }
                } catch (Exception e) {
                    // Don't stop the others
                    LOGGER.error("Getting synonyms", e);
                }
            }
//        	LOGGER.debug("SEARCH synonyms after getting futures");
        } finally {
            pool.shutdown();
        }
        return resultMap;
    }

    public EnzymeModel getEnzymeDetails(EnzymeModel enzymeModel) throws MultiThreadingException {
        // Set<String> ecList = DataTypeConverter.getUniprotEcs(enzymeModel);

        Set<String> ecList = enzymeModel.getEc().stream().distinct().collect(Collectors.toSet());

        //DataTypeConverter.getUniprotEcs(enzymeModel);
        //Synonyms are merged into one list if there are more than 1 ec number
        //Set<String> synonyms = new LinkedHashSet<>();
//        LOGGER.debug("SEARCH before getIntenz");
        List<Intenz> intenzList = getIntenz(ecList);
//        LOGGER.debug("SEARCH after  getIntenz");
        List<EnzymeHierarchy> enzymeHierarchies = new ArrayList<>();
        if (intenzList.size() > 0) {
//            LOGGER.debug("SEARCH before intenzList");
            for (Intenz intenz : intenzList) {
                GetSynonymsCaller synonymsCaller = new GetSynonymsCaller();
                GetEcHierarchyCaller ecCaller = new GetEcHierarchyCaller();
//                LOGGER.debug("SEARCH before getSynonyms");
                //synonyms.addAll(synonymsCaller.getSynonyms(intenz));
//                LOGGER.debug("SEARCH before getEcHierarchy");
                EnzymeHierarchy enzymeHierarchy = ecCaller.getEcHierarchy(intenz);
                if (enzymeHierarchy != null) {
                    enzymeHierarchies.add(enzymeHierarchy);
                }
//                LOGGER.debug("SEARCH after  getEcHierarchy");
            }
//            LOGGER.debug("SEARCH after intenzList");
//            if (synonyms.size() > 0) {
//            	@SuppressWarnings("unchecked")
//				Collection<String> newSynonyms = CollectionUtils.subtract(
//						synonyms, enzymeModel.getSynonym());
//                enzymeModel.getSynonym().addAll(newSynonyms);
//            }

            //Enzyme has previously initiatized to set Sequence info
            //Enzyme enzyme = new Enzyme();
            if (enzymeModel.getEnzyme() == null) {
                Enzyme enzyme = new Enzyme();
                enzymeModel.setEnzyme(enzyme);
            }
            if (enzymeHierarchies.size() > 0) {
                enzymeModel.getEnzyme().setEchierarchies(enzymeHierarchies);
            }
        }

        return enzymeModel;
    }

    public List<Intenz> getIntenz(Collection<String> ecList) {
        List<Intenz> results = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletionService<Intenz> ecs
                = new ExecutorCompletionService<>(pool);
        try {
//        	 LOGGER.debug("SEARCH before callables loop");
            List<String> incompleteEcs = new ArrayList<>();
            for (String ec : ecList) {
                if (ec.indexOf('-') > -1) {
                    incompleteEcs.add(ec);
                    continue; // ignore incomplete ECs
                }

                Callable<Intenz> callable = new GetIntenzCaller(
                        IntenzUtil.createIntenzEntryUrl(
                                config.getIntenzXmlUrl(), ec));
                ecs.submit(callable);

            }
            if (!incompleteEcs.isEmpty()) {
                LOGGER.warn("Unable to retrieve info from IntEnz on "
                        + incompleteEcs);
            }
            int completeEcs = ecList.size() - incompleteEcs.size();
            for (int i = 0; i < completeEcs; i++) {
                try {
                    Future<Intenz> future
                            = ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
                    if (future != null) {
                        Intenz intenz = future.get();
                        if (intenz != null) {
                            results.add(intenz);
                        }
                    } else {
                        LOGGER.warn("Job not returned!");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // Don't stop the others
                    LOGGER.error("Callable " + (i + 1) + " of " + completeEcs
                            + " - " + e.getMessage(), e);
                }
            }
        } finally {
            pool.shutdown();
        }
        return results;
    }

    public Collection<Molecule> getCofactors(String ec) {
        // Incomplete EC numbers won't get anything from IntEnzXML:
        if (ec.indexOf('-') > -1) {
            return null;
        }
        Collection<Molecule> cofactors = null;
        GetCofactorsCaller cofactorsCaller = new GetCofactorsCaller(
                IntenzUtil.createIntenzEntryUrl(config.getIntenzXmlUrl(), ec));
        try {
            cofactors = cofactorsCaller.call();
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve cofactors for " + ec, e);
        }
        return cofactors;
    }

    public Collection<Molecule> getCofactors(Collection<String> ecs) {
        Collection<Molecule> cofactors = null;
        for (String ec : ecs) {
            if (ec.indexOf('-') > -1) {
                continue;
            }
            final Collection<Molecule> newCofactors = getCofactors(ec);
            if (cofactors == null) {
                cofactors = newCofactors;
            } else if (newCofactors != null) {
                cofactors.addAll(newCofactors);
            }
        }
        return cofactors;
    }

}
