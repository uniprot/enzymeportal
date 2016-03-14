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
import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.xml.service.XmlService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class})
public abstract class BaseTest extends TestCase {
    
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected EnzymePortalXmlService xmlService;
    
    @Autowired
    protected DataSource dataSource;
    
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
