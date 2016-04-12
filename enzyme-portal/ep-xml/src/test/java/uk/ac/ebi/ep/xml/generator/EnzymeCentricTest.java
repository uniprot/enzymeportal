package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import org.junit.After;
import org.junit.Before;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricTest extends BaseTest {

    private XmlGenerator enzymeCentricInstance;
    private List<IntenzEnzymes> enzymes;

    @Before
    @Override
    public void setUp() {
        enzymeCentricInstance = new EnzymeCentric(xmlService, xmlConfigParams);
        enzymes = new ArrayList<>();
        IntenzEnzymes enzyme_1 = new IntenzEnzymes();
        enzyme_1.setInternalId(BigDecimal.ONE);
        enzyme_1.setEcNumber("1.1.1.1");
        enzyme_1.setEnzymeName("alcohol dehydrogenase");
        enzyme_1.setCatalyticActivity(" (1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. (2) A secondary alcohol + NAD(+) = a ketone + NADH.");

        IntenzEnzymes enzyme_2 = new IntenzEnzymes(BigDecimal.valueOf(2L));
        enzyme_2.setEcNumber("6.1.1.1");
        enzyme_2.setEnzymeName("tyrosine—tRNA ligase");
        enzyme_2.setCatalyticActivity("ATP + L-tyrosine + tRNA(Tyr) = AMP + diphosphate + L-tyrosyl-tRNA(Tyr).");

        IntenzEnzymes enzyme_3 = new IntenzEnzymes(BigDecimal.valueOf(3L));

        enzyme_3.setEcNumber("3.4.24.85");
        enzyme_3.setEnzymeName("S2P endopeptidase");

        enzymes.add(enzyme_1);
        enzymes.add(enzyme_2);
        enzymes.add(enzyme_3);

        xmlService.addIntenzEnzymes(enzymes);
    }

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
        enzymes.clear();
    }

    @org.junit.Test//(expected = IllegalArgumentException.class)
    public void testWhenXMLDoesNotExist() throws IOException {
        logger.info("testWhenXMLDoesNotExist with parameters");

        String filename = "enzymes.xml";
        File output = temporaryFolder.newFile(filename);
        generateXml(output);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("XML file does not exist");
        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        enzymeCentricInstance.validateXML(output.getAbsolutePath() + "BLA", xsd);

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
        generateXml(output);
        assertThat(output).exists().isFile();
        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        Boolean validate = enzymeCentricInstance.validateXML(output.getAbsolutePath(), xsd);
        assertThat(validate).isTrue();

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

        String xml = generateXml(output);
        String fileDir = output.getParent();

        assertThat(output).exists().isFile();
        assertThat(output).hasExtension("xml")
                .exists()
                .hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));
        assertThat(contentOf(output)).containsIgnoringCase(xml.trim());

        //  peek the generated file              
        try (Stream<String> data = Files.lines(output.toPath(), StandardCharsets.UTF_8)) {
            data.forEach(d -> System.out.println(d));
        }
    }

    private String generateXml(File output) {

        try {
            enzymeCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
        String release = xmlConfigParams.getReleaseNumber();

        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release>" + release + "</release>\n"
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
                + "                <field name=\"synonym\">-</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"uniprot_name\">C9XWA9_CROTZ</field>\n"
                + "                <field name=\"protein_name\">Zinc metalloprotease</field>\n"
                + "                <field name=\"scientific_name\">Cronobacter turicensis (strain DSM 18703 / LMG 23827 / z3032)</field>\n"
                + "                <field name=\"uniprot_name\">E0SCD6_DICD3</field>\n"
                + "                <field name=\"scientific_name\">Dickeya dadantii (strain 3937)</field>\n"
                + "                <field name=\"synonym\">Inner membrane zinc RIP metalloprotease</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_MOUSE</field>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "                <field name=\"uniprot_name\">Q1CFF4_YERPN</field>\n"
                + "                <field name=\"scientific_name\">Yersinia pestis bv. Antiqua (strain Nepal516)</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"E0SCD6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"Q1CFF4\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"C9XWA9\" dbname=\"UNIPROTKB\"/>\n"
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

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release>" + release + "</release>\n"
                + "    <release_date>" + releaseDate + "</release_date>\n"
                + "    <entry_count>3</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>alcohol dehydrogenase</name>\n"
                + "            <description> (1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. (2) A secondary alcohol + NAD(+) = a ketone + NADH.</description>\n"
                + "            <additional_fields/>\n"
                + "            <cross_references/>\n"
                + "        </entry>\n"
                + "        <entry id=\"3.4.24.85\">\n"
                + "            <name>S2P endopeptidase</name>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"uniprot_name\">Q1CFF4_YERPN</field>\n"
                + "                <field name=\"scientific_name\">Yersinia pestis bv. Antiqua (strain Nepal516)</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"scientific_name\">Cronobacter turicensis (strain DSM 18703 / LMG 23827 / z3032)</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_BOVIN</field>\n"
                + "                <field name=\"uniprot_name\">C9XWA9_CROTZ</field>\n"
                + "                <field name=\"synonym\">Inner membrane zinc RIP metalloprotease</field>\n"
                + "                <field name=\"protein_name\">Zinc metalloprotease</field>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"scientific_name\">Dickeya dadantii (strain 3937)</field>\n"
                + "                <field name=\"uniprot_name\">E0SCD6_DICD3</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_MOUSE</field>\n"
                + "                <field name=\"scientific_name\">Bos taurus</field>\n"
                + "                <field name=\"protein_name\">Membrane-bound transcription factor site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"E0SCD6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"Q1CFF4\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"C9XWA9\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "                <ref dbkey=\"Q0III2\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>tyrosine—tRNA ligase</name>\n"
                + "            <description>ATP + L-tyrosine + tRNA(Tyr) = AMP + diphosphate + L-tyrosyl-tRNA(Tyr).</description>\n"
                + "            <additional_fields/>\n"
                + "            <cross_references/>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        return xml;

    }

}
