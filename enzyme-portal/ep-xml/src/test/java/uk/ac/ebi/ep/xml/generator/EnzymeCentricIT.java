package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.JAXBException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricIT extends BaseTest {

    private XmlGenerator enzymeCentricInstance;
    @Before
    @Override
    public void setUp() {
        enzymeCentricInstance = new EnzymeCentric(xmlService, mockXmlConfigParams);
    }

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
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
        printGeneratedXML(output);
    }

    private String generateXml(File output) {

        try {
            enzymeCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
        String release = mockXmlConfigParams.getReleaseNumber();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release>" + release + "</release>\n"
                + "    <release_date>" + releaseDate + "</release_date>\n"
                + "    <entry_count>3</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>Alcohol dehydrogenase</name>\n"
                + "            <description>(1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. (2) A secondary alcohol + NAD(+) = a ketone + NADH.</description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"enzyme_family\">Oxidoreductases</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references/>\n"
                + "        </entry>\n"
                + "        <entry id=\"3.4.24.85\">\n"
                + "            <name>S2P endopeptidase</name>\n"
                + "            <description>Cleaves several transcription factors that are type-2 transmembraneproteins within membrane-spanning domains. Known substrates includesterol regulatory element-binding protein (SREBP) -1, SREBP-2 and formsof the transcriptional activator ATF6. SREBP-2 is cleaved at the site477-DRSRILL-|-CVLTFLCLSFNPLTSLLQWGGA-505. The residues Asn-Pro,11 residues distal to the site of cleavage in the membrane-spanningdomain, are important for cleavage by S2P endopeptidase. Replacement ofeither of these residues does not prevent cleavage, but there is nocleavage if both of these residues are replaced.</description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"uniprot_name\">Q1CFF4_YERPN</field>\n"
                + "                <field name=\"scientific_name\">Yersinia pestis bv. Antiqua (strain Nepal516)</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"scientific_name\">Cronobacter turicensis (strain DSM 18703 / LMG 23827 / z3032)</field>\n"
                + "                <field name=\"common_name\">Mouse</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"uniprot_name\">MBTP2_BOVIN</field>\n"
                + "                <field name=\"enzyme_family\">Hydrolases</field>\n"
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
                + "                <field name=\"common_name\">Bovine</field>\n"
                + "                <field name=\"protein_name\">Membrane-bound transcription factor site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"R-381033\" dbname=\"REACTOME\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "                <ref dbkey=\"Q0III2\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"198628\" dbname=\"TAXONOMY\"/>\n"
                + "                <ref dbkey=\"R-1655829\" dbname=\"REACTOME\"/>\n"
                + "                <ref dbkey=\"E0SCD6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"693216\" dbname=\"TAXONOMY\"/>\n"
                + "                <ref dbkey=\"Q1CFF4\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"9913\" dbname=\"TAXONOMY\"/>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"C9XWA9\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"10090\" dbname=\"TAXONOMY\"/>\n"
                + "                <ref dbkey=\"377628\" dbname=\"TAXONOMY\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>Tyrosine--tRNA ligase</name>\n"
                + "            <description>ATP + L-tyrosine + tRNA(Tyr) = AMP + diphosphate + L-tyrosyl-tRNA(Tyr).</description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"enzyme_family\">Ligases</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references/>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        return xml;

    }

}
