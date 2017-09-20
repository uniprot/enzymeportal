package uk.ac.ebi.ep.model.service;

import uk.ac.ebi.ep.model.service.EnzymePortalXmlService;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.testConfig.SpringDataMockConfig;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class})
public abstract class AbstractDataTest extends TestCase {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractDataTest.class);
//    @Autowired
//    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected UniprotEntryRepository uniprotEntryRepository;

//    @Autowired
//    protected DiseaseService diseaseService;
//
//    @Autowired
//    protected UniprotEntryService uniprotEntryService;
    @Autowired
    protected EnzymePortalXmlService enzymePortalXmlService;

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SessionFactory sessionFactory;

}
