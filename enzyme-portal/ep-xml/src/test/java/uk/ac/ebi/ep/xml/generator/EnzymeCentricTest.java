/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.sql.SQLException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Ignore
public class EnzymeCentricTest extends BaseTest {

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

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
    }

    /**
     * Test of validateXML method, of class EnzymeCentric.
     */
    @org.junit.Test
    public void testValidateXML() {
        System.out.println("validateXML");
        EnzymeCentric instance = null;
        instance.validateXML();

    }

    /**
     * Test of generateXmL method, of class EnzymeCentric.
     *
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testGenerateXmL() throws Exception {
        System.out.println("generateXmL");
        EnzymeCentric instance = null;
        instance.generateXmL();

    }

}
