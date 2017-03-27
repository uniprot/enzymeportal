package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static uk.ac.ebi.ep.xml.generator.BaseTest.logger;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Entry;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentricIT extends BaseTest {

    private XmlGenerator proteinCentricInstance;

    @Before
    @Override
    public void setUp() {
        proteinCentricInstance = new ProteinCentric(xmlService, mockXmlConfigParams, sessionFactory);
    }

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
    }

    /**
     * Test of validateXML method, of class ProteinCentric.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXML_With_Params() throws IOException {

        logger.info("test DefaultValidateXML with parameters");

        String filename = "protein.xml";
        File output = temporaryFolder.newFile(filename);
        generateXml(output.getAbsolutePath());
        assertThat(output).exists().isFile();
        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        Boolean validate = proteinCentricInstance.validateXML(output.getAbsolutePath(), xsd);
        assertThat(validate).isTrue();

    }

    /**
     * Test of generateXmL method, of class ProteinCentric.
     *
     * @throws javax.xml.bind.JAXBException
     * @throws java.io.IOException
     */
    @Test
    public void testGenerateXmL() throws JAXBException, IOException {
        logger.info("testGenerateProteinXmL");

        String filename = "protein.xml";

        File output = temporaryFolder.newFile(filename);

        String xml = generateXml(output.getPath());
        String fileDir = output.getParent();
        assertThat(output).exists().isFile();
        assertThat(output).hasExtension("xml")
                .exists()
                //.hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));
        //no need to check content as we are processing in parallel
        //assertThat(contentOf(output)).containsIgnoringCase(xml.trim());

        //  peek the generated file 
        printGeneratedXML(output);

    }

    /**
     * Test of createPathToWriteXML method, of class XmlGenerator.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testCreatePathToWriteXML() throws IOException {
        System.out.println("createPathToWriteXML");

        String filename = "protein.xml";

        File output = temporaryFolder.newFile(filename);

        String xmlDir = output.getParent();
        String xmlFile = filename;

        Writer result = proteinCentricInstance.createPathToWriteXML(xmlDir, xmlFile);

        assertNotNull(result);
        assertThat(output).exists().isFile();
        assertThat(output).hasExtension("xml")
                .exists()
                .hasParent(resolvePath(xmlDir));

    }

    /**
     * Test of xmlMarshaller method, of class XmlGenerator.
     *
     * @throws javax.xml.bind.JAXBException
     */
    @Test
    public void testXmlMarshaller() throws JAXBException {
        logger.info("testXmlMarshaller");

        Marshaller marshaller = proteinCentricInstance.xmlMarshaller(Database.class);
        assertNotNull(marshaller);

    }

    /**
     * Test of springXmlMarshaller method, of class XmlGenerator.
     *
     * @throws javax.xml.bind.JAXBException
     */
    @Test
    public void testSpringXmlMarshaller() throws JAXBException {
        logger.info("testSpringXmlMarshaller");

        org.springframework.oxm.Marshaller marshaller = proteinCentricInstance.springXmlMarshaller(Entry.class);

        assertNotNull(marshaller);
        assertTrue(marshaller.supports(Entry.class));
        assertFalse(marshaller.supports(Database.class));

    }

    private String generateXml(String output) {

        try {
            proteinCentricInstance.generateXmL(output);
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
        String release = mockXmlConfigParams.getReleaseNumber();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<database>\n"
                + "  <name>Enzyme Portal</name>\n"
                + "  <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "  <release>" + release + "</release>\n"
                + "  <release_date>" + releaseDate + "</release_date>\n"
                + "  <entry_count>2</entry_count>\n"
                + "  <entries>\n"
                + "    <entry id=\"E76XC1\">\n"
                + "      <name>cGMP-specific 3',5'-cyclic phosphodiesterase</name>\n"
                + "      <description>cGMP-specific 3',5'-cyclic phosphodiesterase</description>\n"
                + "      <additional_fields>\n"
                + "        <field name=\"synonym\">CGB-PDE</field>\n"
                + "        <field name=\"common_name\">Human</field>\n"
                + "        <field name=\"compound_name\">Sildenafil</field>\n"
                + "        <field name=\"uniprot_name\">PDE5A_HUMAN</field>\n"
                + "        <field name=\"synonym\">cGMP-binding cGMP-specific phosphodiesterase</field>\n"
                + "        <field name=\"status\">reviewed</field>\n"
                + "        <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "        <field name=\"compound_type\">INHIBITOR</field>\n"
                + "      </additional_fields>\n"
                + "      <cross_references>\n"
                + "        <ref dbkey=\"9606\" dbname=\"TAXONOMY\"></ref>\n"
                + "        <ref dbkey=\"O76074\" dbname=\"UNIPROTKB\"></ref>\n"
                + "        <ref dbkey=\"CHEMBL192\" dbname=\"ChEMBL\"></ref>\n"
                + "      </cross_references>\n"
                + "    </entry>\n"
                + "    <entry id=\"EUN8I9\">\n"
                + "      <name>1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</name>\n"
                + "      <description>1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</description>\n"
                + "      <additional_fields>\n"
                + "        <field name=\"synonym\">24-OHase</field>\n"
                + "        <field name=\"uniprot_name\">PDE5A_HUMAN</field>\n"
                + "        <field name=\"status\">reviewed</field>\n"
                + "        <field name=\"synonym\">Vitamin D(3) 24-hydroxylase</field>\n"
                + "        <field name=\"common_name\">Mouse</field>\n"
                + "        <field name=\"compound_type\">INHIBITOR</field>\n"
                + "        <field name=\"synonym\">CGB-PDE</field>\n"
                + "        <field name=\"uniprot_name\">CP24A_RAT</field>\n"
                + "        <field name=\"common_name\">Human</field>\n"
                + "        <field name=\"scientific_name\">Mus musculus</field>\n"
                + "        <field name=\"compound_name\">Sildenafil</field>\n"
                + "        <field name=\"synonym\">cGMP-binding cGMP-specific phosphodiesterase</field>\n"
                + "        <field name=\"uniprot_name\">CP24A_MOUSE</field>\n"
                + "        <field name=\"scientific_name\">Rattus norvegicus</field>\n"
                + "        <field name=\"common_name\">Rat</field>\n"
                + "        <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "        <field name=\"synonym\">1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</field>\n"
                + "      </additional_fields>\n"
                + "      <cross_references>\n"
                + "        <ref dbkey=\"9606\" dbname=\"TAXONOMY\"></ref>\n"
                + "        <ref dbkey=\"Q64441\" dbname=\"UNIPROTKB\"></ref>\n"
                + "        <ref dbkey=\"O76074\" dbname=\"UNIPROTKB\"></ref>\n"
                + "        <ref dbkey=\"CHEMBL192\" dbname=\"ChEMBL\"></ref>\n"
                + "        <ref dbkey=\"10090\" dbname=\"TAXONOMY\"></ref>\n"
                + "        <ref dbkey=\"10116\" dbname=\"TAXONOMY\"></ref>\n"
                + "        <ref dbkey=\"1.14.13.126\" dbname=\"INTENZ\"></ref>\n"
                + "        <ref dbkey=\"Q09128\" dbname=\"UNIPROTKB\"></ref>\n"
                + "      </cross_references>\n"
                + "    </entry>\n"
                + "  </entries>\n"
                + "</database>";

        return xml;

    }

}
