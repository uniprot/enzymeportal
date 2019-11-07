package uk.ac.ebi.ep.literatureservice.config;

import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.literatureservice.service.PmcRestService;

/**
 *
 * @author joseph
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PmcConfig.class})
public class PmcConfigTest {

    @Autowired
    private LiteratureService literatureService;
    @Autowired
    private PmcServiceUrl pmcServiceUrl;
    @Autowired
    private PmcRestService pmcRestService;

    @Test
    public void injectedComponentsAreNotNull() {
        log.info("injectedComponentsAreNotNull");
        assertNotNull(literatureService);
        assertNotNull(pmcServiceUrl);
        assertNotNull(pmcRestService);

    }

    /**
     * Test of pmcServiceUrl method, of class PmcConfig.
     */
    @Test
    public void testPmcServiceUrl() {
        assertNotNull(pmcServiceUrl.getGenericUrl());
        assertNotNull(pmcServiceUrl.getSpecificUrl());

    }

}
