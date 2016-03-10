/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import javax.sql.DataSource;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.xml.config.XmlConfig;
import uk.ac.ebi.ep.xml.service.XmlService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class, XmlConfig.class})
public abstract class BaseTest extends TestCase {

    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    private EnzymePortalXmlService xmlService;

    @Autowired
    protected DataSource dataSource;

    protected XmlService enzymeCentricInstance;
    protected XmlService proteinCentricInstance;

    @Before
    @Override
    public void setUp() {
        enzymeCentricInstance = new EnzymeCentric(enzymePortalService, xmlService);
        proteinCentricInstance = new ProteinCentric(enzymePortalService, xmlService);
    }
    

}
