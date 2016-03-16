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
import org.junit.Before;
import org.junit.Test;
import static uk.ac.ebi.ep.xml.generator.BaseTest.logger;
import uk.ac.ebi.ep.xml.service.XmlService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentricTest extends BaseTest {

    private XmlService proteinCentricInstance;

    @Before
    @Override
    public void setUp() {
        proteinCentricInstance = new ProteinCentric(enzymePortalService, xmlService);
    }

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
        enzymes.clear();
    }

    /**
     * Test of generateXmL method, of class ProteinCentric.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGenerateXmL() throws Exception {
        String filename = "protein.xml";
        File output = temporaryFolder.newFile(filename);

        String xml = generateXml(output);
        String fileDir = output.getParent();

        assertThat(output).hasExtension("xml")
                // .hasContent(xml.trim())//parallel does not guarantee order
                .hasParent(resolvePath(fileDir));

        //  peek the generated file              
        try (Stream<String> data = Files.lines(output.toPath(), StandardCharsets.UTF_8)) {
            data.forEach(d -> System.out.println(d));
        }

    }

    /**
     * Test of validateXML method, of class ProteinCentric.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXML() throws IOException {
        logger.info("testValidateXML");
        String filename = "enzymes.xml";
        File output = temporaryFolder.newFile(filename);

        generateXml(output);

        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        Boolean validate = proteinCentricInstance.validateXML(output.getAbsolutePath(), xsd);

        assertThat(validate).isTrue();
    }

    private String generateXml(File output) {
        try {
            proteinCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        LocalDate date = LocalDate.now();
        String releaseDate = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release_date>" + releaseDate + "</release_date>\n"
                + "    <entry_count>17</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"CP24A_MOUSE\" acc=\"Q64441\">\n"
                + "            <name>MOCK-1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">calcitriol,NADPH:oxygen oxidoreductase (24-hydroxylating)</field>\n"
                + "                <field name=\"synonym\">CYP24A1</field>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q64441\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"1.14.13.126\" dbname=\"INTENZ\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"CP24A_RAT\" acc=\"Q09128\">\n"
                + "            <name>1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">calcitriol,NADPH:oxygen oxidoreductase (24-hydroxylating)</field>\n"
                + "                <field name=\"synonym\">CYP24A1</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"scientific_name\">Rattus norvegicus</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q09128\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"CP24A_HUMAN\" acc=\"Q07973\">\n"
                + "            <name>1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">calcitriol,NADPH:oxygen oxidoreductase (24-hydroxylating)</field>\n"
                + "                <field name=\"synonym\">CYP24A1</field>\n"
                + "                <field name=\"disease_name\">hypercalcemia</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q07973\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MBTP2_CRIGR\" acc=\"O54862\">\n"
                + "            <name>Membrane-bound transcription factor site-2 protease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"scientific_name\">Cricetulus griseus</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"O54862\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MBTP2_BOVIN\" acc=\"Q0III2\">\n"
                + "            <name>Membrane-bound transcription factor site-2 protease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"scientific_name\">Bos taurus</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "                <ref dbkey=\"Q0III2\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MBTP2_PONAB\" acc=\"Q5RAC8\">\n"
                + "            <name>Membrane-bound transcription factor site-2 protease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"scientific_name\">Pongo abelii</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q5RAC8\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MBTP2_MOUSE\" acc=\"Q8CHX6\">\n"
                + "            <name>Membrane-bound transcription factor site-2 protease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q8CHX6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MBTP2_HUMAN\" acc=\"O43462\">\n"
                + "            <name>Membrane-bound transcription factor site-2 protease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">membrane-bound transcription factor site 2 protease</field>\n"
                + "                <field name=\"synonym\">sterol regulatory element-binding protein site 2 protease</field>\n"
                + "                <field name=\"synonym\">S2P endopeptidase</field>\n"
                + "                <field name=\"synonym\">sterol-regulatory element-binding proteins intramembrane protease</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">site-2 protease</field>\n"
                + "                <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "                <field name=\"synonym\">-</field>\n"
                + "                <field name=\"disease_name\">x-linked combined immunodeficiency diseases</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"O43462\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"CP7B1_HUMAN\" acc=\"O75881\">\n"
                + "            <name>25-hydroxycholesterol 7-alpha-hydroxylase</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">27-hydroxycholesterol 7-monooxygenase</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "                <field name=\"synonym\">CYP7B1</field>\n"
                + "                <field name=\"disease_name\">spastic paraplegia hereditary</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"O75881\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"CP7B1_RAT\" acc=\"Q63688\">\n"
                + "            <name>25-hydroxycholesterol 7-alpha-hydroxylase</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">27-hydroxycholesterol 7-monooxygenase</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"scientific_name\">Rattus norvegicus</field>\n"
                + "                <field name=\"synonym\">CYP7B1</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q63688\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"CP7B1_MOUSE\" acc=\"Q60991\">\n"
                + "            <name>25-hydroxycholesterol 7-alpha-hydroxylase</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"scientific_name\">Mus musculus</field>\n"
                + "                <field name=\"synonym\">27-hydroxycholesterol 7-monooxygenase</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"synonym\">CYP7B1</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q60991\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"PDE5A_HUMAN\" acc=\"O76074\">\n"
                + "            <name>cGMP-specific 3',5'-cyclic phosphodiesterase</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"compound_name\">Sildenafil</field>\n"
                + "                <field name=\"synonym\">cGMP-binding cGMP-specific phosphodiesterase</field>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"O76074\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"CHEMBL192\" dbname=\"ChEMBL\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"MDR1_HUMAN\" acc=\"P08183\">\n"
                + "            <name>Multidrug resistance protein 1</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"status\">reviewed</field>\n"
                + "                <field name=\"compound_name\">IVERMECTIN</field>\n"
                + "                <field name=\"compound_name\">MICONAZOLE</field>\n"
                + "                <field name=\"scientific_name\">Homo sapiens</field>\n"
                + "                <field name=\"compound_name\">AMANTADINE</field>\n"
                + "                <field name=\"synonym\">empty</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"CHEMBL1200633\" dbname=\"ChEMBL\"/>\n"
                + "                <ref dbkey=\"CHEMBL91\" dbname=\"ChEMBL\"/>\n"
                + "                <ref dbkey=\"CHEMBL660\" dbname=\"ChEMBL\"/>\n"
                + "                <ref dbkey=\"P08183\" dbname=\"UNIPROTKB\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"D5CT29_SIDLE\" acc=\"D5CT29\">\n"
                + "            <name>CTP synthase</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"scientific_name\">Sideroxydans lithotrophicus (strain ES-1)</field>\n"
                + "                <field name=\"synonym\">CTP synthetase</field>\n"
                + "                <field name=\"compound_name\">GTP</field>\n"
                + "                <field name=\"status\">unreviewed</field>\n"
                + "                <field name=\"synonym\">UTP--ammonia ligase</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"D5CT29\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"CHEBI:37565\" dbname=\"ChEBI\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"Q1CFF4_YERPN\" acc=\"Q1CFF4\">\n"
                + "            <name>Zinc metalloprotease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">Inner membrane zinc RIP metalloprotease</field>\n"
                + "                <field name=\"scientific_name\">Yersinia pestis bv. Antiqua (strain Nepal516)</field>\n"
                + "                <field name=\"status\">unreviewed</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"Q1CFF4\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"E0SCD6_DICD3\" acc=\"E0SCD6\">\n"
                + "            <name>Zinc metalloprotease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"synonym\">Inner membrane zinc RIP metalloprotease</field>\n"
                + "                <field name=\"scientific_name\">Dickeya dadantii (strain 3937)</field>\n"
                + "                <field name=\"status\">unreviewed</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"E0SCD6\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "        <entry id=\"C9XWA9_CROTZ\" acc=\"C9XWA9\">\n"
                + "            <name>Zinc metalloprotease</name>\n"
                + "            <description></description>\n"
                + "            <additional_fields>\n"
                + "                <field name=\"scientific_name\">Cronobacter turicensis (strain DSM 18703 / LMG 23827 / z3032)</field>\n"
                + "                <field name=\"status\">unreviewed</field>\n"
                + "            </additional_fields>\n"
                + "            <cross_references>\n"
                + "                <ref dbkey=\"C9XWA9\" dbname=\"UNIPROTKB\"/>\n"
                + "                <ref dbkey=\"3.4.24.85\" dbname=\"INTENZ\"/>\n"
                + "            </cross_references>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";
        return xml;
    }

}
