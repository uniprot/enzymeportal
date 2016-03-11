/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.data.testConfig.EnzymeMockDataConfig;
import uk.ac.ebi.ep.xml.service.XmlService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EnzymeMockDataConfig.class, GlobalConfig.class})
public abstract class BaseTest extends TestCase {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected EnzymePortalXmlService xmlService;

    @Autowired
    protected DataSource mockDataSource;

    protected XmlService enzymeCentricServiceInstance;
    protected XmlGenerator enzymeCentricInstance;
    protected XmlService proteinCentricInstance;
    protected List<IntenzEnzymes> enzymes = new ArrayList<>();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    @Override
    public void setUp() {

        IntenzEnzymes enzyme_1 = new IntenzEnzymes();
        enzyme_1.setInternalId(BigDecimal.ONE);
        enzyme_1.setEcNumber("1.1.1.1");
        enzyme_1.setEnzymeName("alcohol dehydrogenase");

        IntenzEnzymes enzyme_2 = new IntenzEnzymes(BigDecimal.valueOf(2L));
        enzyme_2.setEcNumber("6.1.1.1");
        enzyme_2.setEnzymeName("tyrosine—tRNA ligase");

        enzymes.add(enzyme_1);
        enzymes.add(enzyme_2);

        xmlService.addIntenzEnzymes(enzymes);

        enzymeCentricInstance = new EnzymeCentric(enzymePortalService, xmlService);
        proteinCentricInstance = new ProteinCentric(enzymePortalService, xmlService);
    }

    protected String resolvePath(String folder) {
        return temporaryFolder
                .getRoot().toPath()
                .resolve(folder)
                .toString();
    }

}
