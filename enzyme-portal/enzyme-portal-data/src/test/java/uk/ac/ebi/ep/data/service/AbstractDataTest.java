/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import uk.ac.ebi.ep.data.dataconfig.SpringDataMockConfig;
import javax.sql.DataSource;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class})
//@ActiveProfiles("uzpdev")
//@Dev
public abstract class AbstractDataTest {
protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractDataTest.class);
    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected UniprotEntryRepository uniprotEntryRepository;

    @Autowired
    protected DiseaseService diseaseService;

    @Autowired
    protected DataSource dataSource;


}
