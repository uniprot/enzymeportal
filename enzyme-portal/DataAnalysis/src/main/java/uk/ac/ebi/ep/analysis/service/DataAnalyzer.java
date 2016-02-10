/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biobabel.util.StringUtil;
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

    private final Logger logger = Logger.getLogger(DataAnalyzer.class);
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

        List<String> accessionList = new CopyOnWriteArrayList<>();

        try (InputStream is = file.startsWith("http://")
                ? new URL(file).openStream()
                : new FileInputStream(file)) {

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            logger.info("Parsing start");
            String line;
            while ((line = br.readLine()) != null) {
                accessionList.add(line);
            }

        } catch (IOException e) {
            logger.error("Error During parsing", e);

        }

        return accessionList;
    }

    /**
     * write enzymes (with experimental evidence) to enzyme portal database
     */
    public void populateEnzymesWithEvidences() {

        try {
            List<SpEnzymeEvidence> enzymeEvidences = computeAccessionsWithEvidences();
            logger.warn("num evidences written to enzyme portal database " + enzymeEvidences.size());

            analysisService.populateEvidences(enzymeEvidences);
            logger.warn("finished updating the evidence table. Starting to update UniprotEntry table with evidence Flag");
            enzymePortalService.updateExpEvidenceFlag();
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("InterruptedException | ExecutionException ", ex);
        }

    }

    /**
     * This writes the evidence analysis to a file. if no directory is
     * specified, file will be generated at the user home. if no filename is
     * specified, evidence.tsv will be used as filename
     *
     * @param fileDir the directory for the file to be written
     * @param filename filename
     * @param deleteFile true if file clean up is required
     */
    public void writeToFile(String fileDir, String filename, Boolean deleteFile) {
        try {
            List<SpEnzymeEvidence> enzymeEvidences = computeAccessionsWithEvidences();
            String fileName = filename;
            if (StringUtil.isNullOrEmpty(filename)) {
                fileName = "evidence.tsv";
            }
            logger.warn("num evidences written to file [" + fileName + "] " + enzymeEvidences.size());

            writeToFile(enzymeEvidences, fileDir, fileName, deleteFile);
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("InterruptedException | ExecutionException ", ex);
        }
    }

    /**
     * This writes the evidence analysis to a file. if no directory is
     * specified, file will be generated at the user home. if no filename is
     * specified, evidence.tsv will be used as filename
     *
     * @param enzymeEvidences SwissProt enzymes with evidences code 269
     * @param fileDir the directory for the file to be written
     * @param filename filename
     * @param deleteFile true if file clean up is required
     */
    public void writeToFile(List<SpEnzymeEvidence> enzymeEvidences, String fileDir, String filename, Boolean deleteFile) {

        List<String> dataList = new CopyOnWriteArrayList<>();

        enzymeEvidences.stream().map(ev -> {
            String acc = ev.getAccession();
            String evidence = ev.getEvidenceLine();
            String data = "Accession : " + acc + " : EvidenType : " + evidence;
            return data;
        }).forEach(data -> {
            dataList.add(data);
        });

        if (!StringUtil.isNullOrEmpty(fileDir)) {
            createDirAndFile(dataList, fileDir, filename, deleteFile);
        } else {
            createFile(dataList, filename, deleteFile);
        }

    }

    /**
     *This writes file to ${user.home}
     * @param dataList data to write
     * @param filename filename
     * @param deleteFile set to true if needs to delete file after creation
     */
    private void createFile(List<String> dataList, String filename, Boolean deleteFile) {
        try {
            String userHome = System.getProperty("user.home");

            String filePath = String.format("%s/%s", userHome, filename);
            bufferedWrite(dataList, filePath);
            if (deleteFile) {
                Path path = Paths.get(filePath);

                Files.deleteIfExists(path);
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     *
     * @param dataList data
     * @param fileLocation where file will be writen
     * @param filename filename
     * @param deleteFile true if file is to be deleted (use afterwards)
     */
    private void createDirAndFile(List<String> dataList, String fileLocation, String filename, Boolean deleteFile) {
        try {

            String fileDir = fileLocation;
            Set<PosixFilePermission> perms
                    = PosixFilePermissions.fromString("rwxr-x---");
            FileAttribute<Set<PosixFilePermission>> attr
                    = PosixFilePermissions.asFileAttribute(perms);
            Files.createDirectories(Paths.get(fileDir), attr);

            String filePath = String.format("%s/%s", fileLocation, filename);

            bufferedWrite(dataList, filePath);
            //bufferedStream(dataList, filePath);

            if (deleteFile) {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    void bufferedStream(List<String> content, String filePath) {
        Path p = Paths.get(filePath);

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, TRUNCATE_EXISTING))) {
            for (String element : content) {
                out.write(element.getBytes(), 0, element.length());

            }

        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * <note>adaptation from Diego code</note>
     * 048 Write a big list of Strings to a file - Use a BufferedWriter 049
     *
     * @param content
     * @param filePath
     */
    private void bufferedWrite(List<String> content, String filePath) throws IOException {

        Path fileP = Paths.get(filePath);
        Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {

            for (String line : content) {

                writer.write(line, 0, line.length());

                writer.newLine();

            }

        } catch (IOException ex) {

            logger.error(ex.getMessage(), ex);

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
        return splitOperation(accessions, evidenceType, enzymes);

    }

    private SpEnzymeEvidence createSpEnzymeEvidence(String accession, String evidenceType) {
        SpEnzymeEvidence evidence = new SpEnzymeEvidence();
        evidence.setAccession(accession);
        evidence.setEvidenceLine(evidenceType);

        return evidence;
    }

    private List<SpEnzymeEvidence> splitOperation(List<String> accessions, String evidenceType, List<String> enzymes) {
        List<SpEnzymeEvidence> evidences = new CopyOnWriteArrayList<>();

        Stream<String> existingStream = accessions.stream();
        Stream<List<String>> partitioned = partition(existingStream, 100, 1);

        partitioned.parallel().forEach(chunk -> {

            chunk.stream()
                    .filter(accession -> (enzymes.contains(accession)))
                    .map(accession -> createSpEnzymeEvidence(accession, evidenceType))
                    .forEach(evidence -> {
                        evidences.add(evidence);
                    });

        });

        return evidences;
    }

    private List<SpEnzymeEvidence> computeAccessionsWithEvidences() throws InterruptedException, ExecutionException {
        List<String> enzymes = enzymePortalService.findAllSwissProtAccessions();
        logger.info("num swissprot enzymes from enzyme portal database " + enzymes.size());

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

        logger.info("Number of Accessions with Evidences found :: " + evidences.size());

        return evidences;

    }

    private List<SpEnzymeEvidence> combineList(Boolean allowDuplicate, List<SpEnzymeEvidence>... parts) {

        List<SpEnzymeEvidence> data = new CopyOnWriteArrayList<>();
        for (List<SpEnzymeEvidence> part : parts) {
            data.addAll(part);
        }

        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

    List<String> combineString(List<String> part1, List<String> part2, Boolean allowDuplicate) {

        List<String> data = new CopyOnWriteArrayList<>();
        data.addAll(part1);
        data.addAll(part2);
        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

    List<SpEnzymeEvidence> combine(List<SpEnzymeEvidence> part1, List<SpEnzymeEvidence> part2, Boolean allowDuplicate) {

        List<SpEnzymeEvidence> data = new CopyOnWriteArrayList<>();
        data.addAll(part1);
        data.addAll(part2);
        if (!allowDuplicate) {
            return data.stream().distinct().collect(Collectors.toList());
        }

        return data;
    }

}
