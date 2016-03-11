/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.bind.JAXBException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import static uk.ac.ebi.ep.xml.generator.BaseTest.logger;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Ignore
public class ProteinCentricTest extends BaseTest {

    @After
    @Override
    public void tearDown() throws SQLException {
        mockDataSource.getConnection().close();
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

        try {
            proteinCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        String fileDir = output.getParent();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release_date>11-Mar-2016</release_date>\n"
                + "    <entry_count>2</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>alcohol dehydrogenase</name>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>tyrosine—tRNA ligase</name>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        assertThat(output).hasExtension("xml")
                .hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));

    }

    /**
     * Test of validateXML method, of class ProteinCentric.
     */
    @Test
    public void testValidateXML() throws IOException {
        logger.info("testValidateXML");
        String filename = "enzymes.xml";
        File output = temporaryFolder.newFile(filename);

        try {
            proteinCentricInstance.generateXmL(output.getAbsolutePath());
        } catch (JAXBException ex) {
            logger.error(ex.getMessage(), ex);
        }

        String fileDir = output.getParent();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<database>\n"
                + "    <name>Enzyme Portal</name>\n"
                + "    <description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</description>\n"
                + "    <release_date>11-Mar-2016</release_date>\n"
                + "    <entry_count>2</entry_count>\n"
                + "    <entries>\n"
                + "        <entry id=\"1.1.1.1\">\n"
                + "            <name>alcohol dehydrogenase</name>\n"
                + "        </entry>\n"
                + "        <entry id=\"6.1.1.1\">\n"
                + "            <name>tyrosine—tRNA ligase</name>\n"
                + "        </entry>\n"
                + "    </entries>\n"
                + "</database>";

        assertThat(output).hasExtension("xml")
                .hasContent(xml.trim())
                .hasParent(resolvePath(fileDir));
        String[] xsd = {"http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd"};
        Boolean validate = proteinCentricInstance.validateXML(output.getAbsolutePath(), xsd);

        assertThat(validate).isTrue();
    }

}
