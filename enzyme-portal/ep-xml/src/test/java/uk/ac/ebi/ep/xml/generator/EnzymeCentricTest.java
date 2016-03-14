/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricTest extends BaseTest {

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
        enzymes.clear();
    }

    /**
     * Test of validateXML method, of class EnzymeCentric.
     *
     * @throws java.io.IOException
     */
    @org.junit.Test
    public void testValidateXML() throws IOException {
        logger.info("testDefaultValidateXML with parameters");

        String filename = "enzymes.xml";
        File output = temporaryFolder.newFile(filename);

        try {
            enzymeCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        String fileDir = output.getParent();
        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release_date>" + releaseDate + "</release_date>\n"
                + "    <entry_count>3</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>alcohol dehydrogenase</name>\n"
                + "            <description> (1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. (2) A secondary alcohol + NAD(+) = a ketone + NADH.</description>\n"
                + "        </entry>\n"
                + "        <entry id=\"3.4.24.85\">\n"
                + "            <name>S2P endopeptidase</name>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"uniprot_name\">MBTP2_BOVIN</field>\n"
                + "                <field name=\"protein_name\">Membrane-bound transcription factor site-2 protease</field>\n"
                + "                <field name=\"scientific_name\">Bos taurus</field>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_MOUSE</field>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "                <ref dbkey=\"Q0III2\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>tyrosine—tRNA ligase</name>\n"
                + "            <description>ATP + L-tyrosine + tRNA(Tyr) = AMP + diphosphate + L-tyrosyl-tRNA(Tyr).</description>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        assertThat(output).hasExtension("xml")
                .hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));
        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        Boolean validate = enzymeCentricInstance.validateXML(output.getAbsolutePath(), xsd);

        assertThat(validate).isTrue();

//  peek the generated file here <note>Delete later</note>              
        try (Stream<String> data = Files.lines(output.toPath(), StandardCharsets.UTF_8)) {
            data.forEach(d -> System.out.println(d));
        }
    }

    /**
     * Test of generateXmL method, of class EnzymeCentric.
     *
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testGenerateXmL() throws Exception {
        logger.info("testGenerateXmL");

        String filename = "enzymes.xml";
        File output = temporaryFolder.newFile(filename);

        try {
            enzymeCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        String fileDir = output.getParent();
        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release_date>" + releaseDate + "</release_date>\n"
                + "    <entry_count>3</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>alcohol dehydrogenase</name>\n"
                + "            <description> (1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. (2) A secondary alcohol + NAD(+) = a ketone + NADH.</description>\n"
                + "        </entry>\n"
                + "        <entry id=\"3.4.24.85\">\n"
                + "            <name>S2P endopeptidase</name>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"uniprot_name\">MBTP2_BOVIN</field>\n"
                + "                <field name=\"protein_name\">Membrane-bound transcription factor site-2 protease</field>\n"
                + "                <field name=\"scientific_name\">Bos taurus</field>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_MOUSE</field>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "                <ref dbkey=\"Q0III2\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>tyrosine—tRNA ligase</name>\n"
                + "            <description>ATP + L-tyrosine + tRNA(Tyr) = AMP + diphosphate + L-tyrosyl-tRNA(Tyr).</description>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        assertThat(output).hasExtension("xml")
                .hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));
    }

}
