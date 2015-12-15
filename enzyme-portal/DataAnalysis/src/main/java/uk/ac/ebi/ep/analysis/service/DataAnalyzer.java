/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.analysis.config.ServiceUrl;
import static uk.ac.ebi.ep.data.batch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.data.domain.SpEnzymeEvidence;
import uk.ac.ebi.ep.data.service.AnalysisService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Service
public class DataAnalyzer {

    private final Logger LOGGER = Logger.getLogger(DataAnalyzer.class);
    @Autowired
    private ServiceUrl serviceUrl;
    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private EnzymePortalService enzymePortalService;

    /**
     *
     * @param file resource location (http://www.uniprot.org/uniprot/)
     * @return evidences
     */
    private List<String> downloadAccessionList(String file) {

        List<String> accessionList = new ArrayList<>();

        try (InputStream is = file.startsWith("http://")
                ? new URL(file).openStream()
                : new FileInputStream(file)) {

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            LOGGER.info("Parsing start");
            String line;
            while ((line = br.readLine()) != null) {
                accessionList.add(line);
            }

        } catch (IOException e) {
            LOGGER.error("Error During parsing", e);

        }

        return accessionList;
    }

    /**
     * write enzymes (with experimental evidence) to enzyme portal database
     */
    public void populateEnzymesWithEvidences() {

        try {
            List<SpEnzymeEvidence> enzymeEvidences = computeAccessionsWithEvidences();
         
             ///analysisService.populateEvidences(enzymeEvidences);
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.error("InterruptedException | ExecutionException ", ex);
        }

    }

    /**
     *
     * @param url uniprot website
     * @param evidenceType type of evidence (see EvidenceType Class)
     * @param enzymes enzymes from enzyme portal database
     * @return enzymes with evidence tags
     */
    private List<SpEnzymeEvidence> tagEvidences(String url, String evidenceType, List<String> enzymes) {

        List<String> accessions = downloadAccessionList(url);
        List<SpEnzymeEvidence> evidences = splitOperation(accessions, evidenceType, enzymes);
        return evidences;
    }

    private SpEnzymeEvidence createSpEnzymeEvidence(String accession, String evidenceType) {
        SpEnzymeEvidence evidence = new SpEnzymeEvidence();
        evidence.setAccession(accession);
        evidence.setEvidenceLine(evidenceType);

        return evidence;
    }

    private List<SpEnzymeEvidence> splitOperation(List<String> accessions, String evidenceType, List<String> enzymes) {
        List<SpEnzymeEvidence> evidences = new ArrayList<>();

        Stream<String> existingStream = accessions.stream();
        Stream<List<String>> partitioned = partition(existingStream, 100, 1);

        partitioned.parallel().forEach((chunk) -> {

            chunk.stream()
                    .filter((accession) -> (enzymes.contains(accession)))
                    .map((accession) -> createSpEnzymeEvidence(accession, evidenceType))
                    .forEach((evidence) -> {
                        evidences.add(evidence);
                    });

        });

        return evidences;
    }

    private List<SpEnzymeEvidence> computeAccessionsWithEvidences() throws InterruptedException, ExecutionException {
        List<String> enzymes = enzymePortalService.findAllSwissProtAccessions();
        LOGGER.warn("num swissprot enzymes from enzyme portal database " + enzymes.size());
        
        //ExecutorService pool = Executors.newFixedThreadPool(10);

        CompletableFuture<List<SpEnzymeEvidence>> functionFuture = CompletableFuture
                .supplyAsync(() -> tagEvidences(serviceUrl.getFunctionUrl(), EvidenceType.FUNCTION.getEvidenceName(), enzymes));

        CompletableFuture<List<SpEnzymeEvidence>> cofactorFuture = CompletableFuture
                .supplyAsync(() -> tagEvidences(serviceUrl.getCofactorUrl(), EvidenceType.COFACTOR.getEvidenceName(), enzymes));

        CompletableFuture<List<SpEnzymeEvidence>> activityFuture = CompletableFuture
                .supplyAsync(() -> tagEvidences(serviceUrl.getActivityUrl(), EvidenceType.CATALYTIC_ACTIVITY.getEvidenceName(), enzymes));

        CompletableFuture<List<SpEnzymeEvidence>> regulationFuture = CompletableFuture
                .supplyAsync(() -> tagEvidences(serviceUrl.getRegulationUrl(), EvidenceType.ENZYME_REGULATION.getEvidenceName(), enzymes));

        CompletableFuture<List<SpEnzymeEvidence>> biophysioFuture = CompletableFuture
                .supplyAsync(() -> tagEvidences(serviceUrl.getBioPhysioUrl(), EvidenceType.BIOPHYSICOCHEMICAL_PROPERTIES.getEvidenceName(), enzymes));

        CompletableFuture<List<SpEnzymeEvidence>> futures = functionFuture
                .thenCombineAsync(cofactorFuture, (functions, cofactors) -> combineList(true, functions, cofactors))
                .thenCombineAsync(activityFuture, (entries, activity) -> combineList(true, entries, activity))
                .thenCombineAsync(regulationFuture, (entries, regulation) -> combineList(true, entries, regulation))
                .thenCombineAsync(biophysioFuture, (entries, bio) -> combineList(true, entries, bio));

        List<SpEnzymeEvidence> evidences = futures.get().stream().collect(Collectors.toList());

        LOGGER.warn("Number of Accessions with Evidences found :: " + evidences.size());

        return evidences;

    }

    private List<SpEnzymeEvidence> combineList(Boolean allowDuplicate, List<SpEnzymeEvidence>... parts) {

        List<SpEnzymeEvidence> data = new LinkedList<>();
        for (List<SpEnzymeEvidence> part : parts) {
            data.addAll(part);
        }

        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

    private List<String> combineString(List<String> part1, List<String> part2, Boolean allowDuplicate) {

        List<String> data = new LinkedList<>();
        data.addAll(part1);
        data.addAll(part2);
        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

    private List<SpEnzymeEvidence> combine(List<SpEnzymeEvidence> part1, List<SpEnzymeEvidence> part2, Boolean allowDuplicate) {

        List<SpEnzymeEvidence> data = new LinkedList<>();
        data.addAll(part1);
        data.addAll(part2);
        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

}
