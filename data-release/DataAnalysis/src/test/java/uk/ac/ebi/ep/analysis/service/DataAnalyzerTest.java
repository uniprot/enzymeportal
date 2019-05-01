/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.model.EnzymeSpExpEvidence;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class DataAnalyzerTest {

    private final Logger logger = Logger.getLogger(DataAnalyzerTest.class);
    private DataAnalyzer instance = null;
    private final List<EnzymeSpExpEvidence> evidences = new CopyOnWriteArrayList<>();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    private String resolvePath(String folder) {
        return temporaryFolder
                .getRoot().toPath()
                .resolve(folder)
                .toString();
    }

    @Before
    public void setUp() {
        instance = new DataAnalyzer();
        EnzymeSpExpEvidence evidence = new EnzymeSpExpEvidence(BigDecimal.ONE);
        evidence.setAccession("Acc123");
        evidence.setEvidenceLine("FUNCTION");
        EnzymeSpExpEvidence evidence1 = new EnzymeSpExpEvidence(BigDecimal.TEN);
        evidence1.setAccession("Acc456");
        evidence1.setEvidenceLine("COFACTOR");
        evidences.add(evidence);
        evidences.add(evidence1);
    }

    /**
     * Test of writeToFile method, of class DataAnalyzer.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testWriteFileToTempFolder() throws IOException {
        logger.warn("Test testWriteFileToTempFolder ..");

        String filename = "evidence.txt";
        Boolean deleteFile = false;// must be false as the file will be deleted before the assertThat condition
        File output = temporaryFolder.newFile(filename);

        // thrown.expect(IOException.class);
        //thrown.expectMessage("folder already exists");
        String fileDir = output.getParent();
        List<EnzymeSpExpEvidence> data = evidences.stream().limit(1).collect(Collectors.toList());
        instance.writeToFile(data, fileDir, filename, deleteFile);

        assertThat(output).hasExtension("txt").
                hasContent("Accession : Acc123 : EvidenType : FUNCTION".trim())
                .hasParent(resolvePath(fileDir));

    }

    /**
     * Test of writeToFile method, of class DataAnalyzer.
     */
    @Test
    public void testWriteToFile() {
        logger.warn("Test writeToFile ..");
        String fileDir = null;
        String filename = "evidence.txt";
        Boolean deleteFile = false;

        instance.writeToFile(evidences, fileDir, filename, deleteFile);
        String userHome = System.getProperty("user.home");

        String filePath = fileDir + "/" + filename;
        if (StringUtils.isEmpty(fileDir)) {
            filePath = userHome + "/" + filename;
        }

        File file = new File(filePath);

        Assert.assertTrue(file.isFile());

        try {
            if (file.isFile()) {
                Path path = Paths.get(filePath);

                Files.deleteIfExists(path);
            }
        } catch (IOException ex) {
            logger.error(ex);
        }

    }

    /**
     * Test of combineString method, of class DataAnalyzer.
     */
    @Test
    public void testCombineString() {
        logger.warn("test combineString");
        List<String> part1 = new ArrayList<>();
        part1.add("Swiss");
        List<String> part2 = new ArrayList<>();
        part2.add("Prot");
        Boolean allowDuplicate = false;

        List<String> expResult = new ArrayList<>();
        expResult.add("Swiss");
        expResult.add("Prot");
        List<String> result = instance.combineString(part1, part2, allowDuplicate);

        assertEquals(expResult, result);
        Assert.assertSame(expResult.stream().findFirst().get(), result.stream().findFirst().get());

    }

    /**
     * Test of combine method, of class DataAnalyzer.
     */
    @Test
    public void testCombine() {
        logger.warn("test combine");

        EnzymeSpExpEvidence evidence = new EnzymeSpExpEvidence(BigDecimal.ONE);
        evidence.setAccession("Acc123");
        evidence.setEvidenceLine("FUNCTION");
        EnzymeSpExpEvidence evidence1 = new EnzymeSpExpEvidence(BigDecimal.TEN);
        evidence1.setAccession("Acc456");
        evidence1.setEvidenceLine("COFACTOR");

        List<EnzymeSpExpEvidence> part1 = new ArrayList<>();
        part1.add(evidence);
        List<EnzymeSpExpEvidence> part2 = new ArrayList<>();
        part2.add(evidence1);
        Boolean allowDuplicate = true;

        List<EnzymeSpExpEvidence> expResult = new ArrayList<>();
        expResult.add(evidence);
        expResult.add(evidence1);

        List<EnzymeSpExpEvidence> result = instance.combine(part1, part2, allowDuplicate);
        assertEquals(expResult, result);

    }

}
