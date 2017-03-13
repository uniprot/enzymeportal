/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.literatureservice.service;

import java.util.Optional;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.literatureservice.config.PmcConfig;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PmcConfig.class})
public class PmcRestServiceIT {

    @Autowired
    private PmcRestService pmcRestService;

    /**
     * Test of findPublicationsByKeyword method, of class PmcRestService.
     */
    @Test
    public void testFindPublicationsByKeyword() {

        String keyword = "cancer";
        int expectedResultSize = 2;

        Optional<EuropePMC> result = pmcRestService.findPublicationsByKeyword(keyword);

        assertNotNull(result);
        assertThat(result.get().getResultList().getResult(), hasSize(greaterThan(expectedResultSize)));
        assertThat(result.get().getResultList().getResult(), hasSize(greaterThanOrEqualTo(expectedResultSize)));

    }

    /**
     * Test of findPublicationsByAccession method, of class PmcRestService.
     */
    @Test
    public void testFindPublicationsByAccession() {

        String accession = "O76074";
        int limit = 100;

        Optional<EuropePMC> result = pmcRestService.findPublicationsByAccession(accession, limit);

        assertNotNull(result);
        assertThat(result.get().getResultList().getResult(), hasSize(greaterThan(1)));
        assertThat(result.get().getResultList().getResult(), hasSize(lessThanOrEqualTo(limit)));

    }

    @Test
    public void testFindPublicationsByInvalidAccession() {

        String accession = "XHDHDHDHDHDDHDHDHDHD";
        int limit = 100;

        Optional<EuropePMC> result = pmcRestService.findPublicationsByAccession(accession, limit);

        assertNotNull(result);
        assertThat(result.get().getResultList().getResult(), hasSize(equalTo(0)));
        assertThat(result.get().getResultList().getResult(), hasSize(lessThan(limit)));

    }

}
