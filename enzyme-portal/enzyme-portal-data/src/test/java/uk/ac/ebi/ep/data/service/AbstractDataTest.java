/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import javax.sql.DataSource;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class})
public abstract class AbstractDataTest extends TestCase {
protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractDataTest.class);
    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected UniprotEntryRepository uniprotEntryRepository;

    @Autowired
    protected DiseaseService diseaseService;
    
    
    @Autowired
    protected UniprotEntryService uniprotEntryService;

    @Autowired
    protected DataSource dataSource;


}
