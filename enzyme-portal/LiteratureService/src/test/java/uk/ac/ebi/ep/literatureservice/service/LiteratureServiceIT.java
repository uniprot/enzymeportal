
package uk.ac.ebi.ep.literatureservice.service;

import java.util.List;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
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
import uk.ac.ebi.ep.uniprotservice.transferObjects.LabelledCitation;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PmcConfig.class})
public class LiteratureServiceIT {

    @Autowired
    private LiteratureService literatureService;

    /**
     * Test of getCitationsByAccession method, of class LiteratureService.
     */
    @Test
    public void testGetCitationsByAccession() {

        String accession = "P42346";
        int limit = 20;

        List<LabelledCitation> result = literatureService.getCitationsByAccession(accession, limit);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThan(1)));
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of getCitationsBySearchTerm method, of class LiteratureService.
     */
    @Test
    public void testGetCitationsBySearchTerm() {
        String term = "cGMP-specific 3',5'-cyclic phosphodiesterase";

        int estimatedResultSize = 2;

        EuropePMC result = literatureService.getCitationsBySearchTerm(term);

        assertNotNull(result);
        assertThat(result.getResultList().getResult(), hasSize(greaterThan(1)));
        assertThat(result.getResultList().getResult(), hasSize(greaterThanOrEqualTo(estimatedResultSize)));
    }

}
