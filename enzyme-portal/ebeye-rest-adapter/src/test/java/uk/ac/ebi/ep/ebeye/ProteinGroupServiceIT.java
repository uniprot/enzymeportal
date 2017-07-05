package uk.ac.ebi.ep.ebeye;

import java.util.List;
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
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class ProteinGroupServiceIT {

    @Autowired
    private ProteinGroupService proteinGroupService;

    /**
     * Test of findProteinGroupResultByEC method, of class ProteinGroupService.
     */
    @Test
    public void testFindProteinGroupResultByEC() {

        String ec = "1.1.1.1";
        int startPage = 0;
        int pageSize = 10;

        ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultByEC(ec, startPage, pageSize);
        int hitcount = result.getHitCount();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(pageSize));

    }

    /**
     * Test of findProteinGroupResultBySearchTermAndEC method, of class
     * ProteinGroupService.
     */
    @Test
    public void testFindProteinGroupResultBySearchTermAndEC() {

        String ec = "2.7.7.7";
        String searchTerm = "breast cancer";
        int startPage = 0;
        int pageSize = 10;

        ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultBySearchTermAndEC(ec, searchTerm, startPage, pageSize);
        int hitcount = result.getHitCount();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(pageSize)));

    }

    /**
     * Test of findUniqueProteinsByOmimIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testFindUniqueProteinsByOmimIdAndEc() {

        String omimId = "610155";
        String ec = "3.6.4.13";
        int limit = 10;

        ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByOmimIdAndEc(omimId, ec, limit);
        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of queryForPrimaryAccessionsByOmimIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testQueryForPrimaryAccessionsByOmimIdAndEc() {

        String omimId = "610155";
        String ec = "3.6.4.13";
        int limit = 10;

        List<String> result = proteinGroupService.queryForPrimaryAccessionsByOmimIdAndEc(omimId, ec, limit);

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of findUniqueProteinsByPathwayIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testFindUniqueProteinsByPathwayIdAndEc() {

        String pathwayId = "R-70171";
        String ec = "2.7.1.4";
        int limit = 10;

        ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByPathwayIdAndEc(pathwayId, ec, limit);
        int hitcount = result.getHitCount();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of queryForPrimaryAccessionsByPathwayIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testQueryForPrimaryAccessionsByPathwayIdAndEc() {

        String pathwayId = "R-70171";
        String ec = "2.7.1.4";
        int limit = 10;

        List<String> result = proteinGroupService.queryForPrimaryAccessionsByPathwayIdAndEc(pathwayId, ec, limit);
        int hitcount = result.size();

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of findUniqueProteinsByTaxIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testFindUniqueProteinsByTaxIdAndEc() {

        String taxId = "9606";
        String ec = "1.1.1.1";
        int limit = 10;

        ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByTaxIdAndEc(taxId, ec, limit);
        int hitcount = result.getHitCount();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(limit));

    }

    /**
     * Test of queryForPrimaryAccessionsByTaxIdAndEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testQueryForPrimaryAccessionsByTaxIdAndEc() {
        String taxId = "9606";
        String ec = "1.1.1.1";
        int limit = 10;

        List<String> result = proteinGroupService.queryForPrimaryAccessionsByTaxIdAndEc(taxId, ec, limit);
        int hitcount = result.size();

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result, hasSize(limit));
        assertThat(result, hasSize(greaterThanOrEqualTo(hitcount)));
    }

    /**
     * Test of queryForPrimaryAccessionsByEcAndKeyword method, of class
     * ProteinGroupService.
     */
    @Test
    public void testQueryForPrimaryAccessionsByEcAndKeyword() {

        String ec = "1.1.1.1";
        String searchTerm = "alcohol";
        int limit = 10;

        List<String> result = proteinGroupService.queryForPrimaryAccessionsByEcAndKeyword(ec, searchTerm, limit);
        int hitcount = result.size();

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result, hasSize(limit));
        assertThat(result, hasSize(greaterThanOrEqualTo(hitcount)));
    }

    /**
     * Test of queryForPrimaryAccessionsByEc method, of class
     * ProteinGroupService.
     */
    @Test
    public void testQueryForPrimaryAccessionsByEc() {

        String ec = "2.1.1.1";
        int limit = 10;

        List<String> result = proteinGroupService.queryForPrimaryAccessionsByEc(ec, limit);
        int hitcount = result.size();

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result, hasSize(limit));
        assertThat(result, hasSize(greaterThanOrEqualTo(hitcount)));
    }

}
