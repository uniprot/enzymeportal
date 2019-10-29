package uk.ac.ebi.ep.enzymeservice.intenz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.enzymeservice.intenz.config.IntenzProperties;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EnzymeHierarchy;
import uk.ac.ebi.ep.enzymeservice.intenz.model.Intenz;
import uk.ac.ebi.ep.enzymeservice.intenz.service.IntenzCallable.GetEcHierarchyCaller;
import uk.ac.ebi.ep.enzymeservice.intenz.service.IntenzCallable.GetIntenzCaller;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class IntenzServiceImpl implements IntenzService {

    private final IntenzProperties intenzProperties;

    @Autowired
    public IntenzServiceImpl(IntenzProperties intenzProperties) {
        this.intenzProperties = intenzProperties;
    }

      @Override
    public List<EnzymeHierarchy> getEnzymeHierarchies(List<String> ecList) {

        List<Intenz> intenzList = getIntenz(ecList);

        List<EnzymeHierarchy> enzymeHierarchies = new ArrayList<>();
        if (!intenzList.isEmpty()) {

            intenzList.forEach(intenz -> processEnzymeHierarchy(intenz, enzymeHierarchies));

        }

        return enzymeHierarchies;
    }

    private void processEnzymeHierarchy(Intenz intenz, List<EnzymeHierarchy> enzymeHierarchies) {
        GetEcHierarchyCaller ecCaller = new GetEcHierarchyCaller();

        EnzymeHierarchy enzymeHierarchy = ecCaller.getEcHierarchy(intenz);
        if (enzymeHierarchy != null) {
            enzymeHierarchies.add(enzymeHierarchy);
        }
    }

    @Override
    public List<Intenz> getIntenz(Collection<String> ecList) {
        List<Intenz> results = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletionService<Intenz> ecs = new ExecutorCompletionService<>(pool);
        try {

            List<String> incompleteEcs = new ArrayList<>();
            for (String ec : ecList) {
                if (ec.indexOf('-') > -1) {
                    incompleteEcs.add(ec);
                    continue; // ignore incomplete ECs
                }

                String entryUrl = IntenzUtil.createIntenzEntryUrl(intenzProperties.getIntenzXmlUrl(), ec);

                Callable<Intenz> callable = new GetIntenzCaller(entryUrl);
                ecs.submit(callable);

            }
            if (!incompleteEcs.isEmpty()) {
                log.warn("Unable to retrieve info from IntEnz for incomplete EC : " + incompleteEcs);
            }
            int completeEcs = ecList.size() - incompleteEcs.size();
            for (int i = 0; i < completeEcs; i++) {
                try {
                    Future<Intenz> future = ecs.poll(intenzProperties.getTimeout(), TimeUnit.MILLISECONDS);
                    if (future != null) {
                        Intenz intenz = future.get();
                        if (intenz != null) {
                            results.add(intenz);
                        }
                    } else {
                        log.warn("Job not returned!");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // Don't stop the others
                    log.error("Callable " + (i + 1) + " of " + completeEcs + " - " + e.getMessage(), e);
                }
            }
        } finally {
            pool.shutdown();
        }
        return results;
    }

}
