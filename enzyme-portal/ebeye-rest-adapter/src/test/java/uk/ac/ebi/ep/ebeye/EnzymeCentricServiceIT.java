package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.ebeye.model.enzyme.FacetValue;

/**
 * Tests the behaviour of the {@link EnzymeCentricService}.
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class EnzymeCentricServiceIT {

    @Autowired
    private EnzymeCentricService enzymeCentricService;

    /**
     * Test of getQuerySearchResult method, of class EnzymeCentricService.
     */
    @Test
    public void testGetSearchResult_with_query_and_zeroStartPage_and_pageSize_of_10_and_filter_for_human_with_facetCount_of_5() {

        String query = "kinase";
        int startPage = 0;
        int pageSize = 10;
        String filter = "TAXONOMY:9606";
        List<String> filters = new ArrayList<>();
        filters.add(filter);
        String facets = filters.stream().collect(Collectors.joining(","));
        int facetCount = 5;

        EnzymeSearchResult result = enzymeCentricService.getQuerySearchResult(query, startPage, pageSize, facets, facetCount);
        int hitcount = result.getHitCount();
        List<FacetValue> facetValue = result.getFacets().stream().findAny().get().getFacetValues();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(pageSize));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(greaterThanOrEqualTo(facetCount)));

    }

    /**
     * Test of findEbiSearchResultsByOmimId method, of class
     * EnzymeCentricService.
     */
    @Test
    public void testFindEbiSearchResultsByOmimId() {

        String omimId = "615522";
        int startPage = 0;
        int pageSize = 10;
        String filter = "TAXONOMY:9606";
        List<String> filters = new ArrayList<>();
        filters.add(filter);
        String facets = filters.stream().collect(Collectors.joining(","));
        int facetCount = 6;

        EnzymeSearchResult result = enzymeCentricService.findEbiSearchResultsByOmimId(omimId, startPage, pageSize, facets, facetCount);
        int hitcount = result.getHitCount();
        List<FacetValue> facetValue = result.getFacets().stream().findAny().get().getFacetValues();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(2)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(greaterThanOrEqualTo(facetCount)));
    }

    /**
     * Test of findEbiSearchResultsByPathwayId method, of class
     * EnzymeCentricService.
     */
    @Test
    public void testFindEbiSearchResultsByPathwayId() {

        String pathwayId = "R-189451";
        int startPage = 0;
        int pageSize = 10;
        String filter = "TAXONOMY:9606";
        List<String> filters = new ArrayList<>();
        filters.add(filter);
        String facets = filters.stream().collect(Collectors.joining(","));
        int facetCount = 5;

        EnzymeSearchResult result = enzymeCentricService.findEbiSearchResultsByPathwayId(pathwayId, startPage, pageSize, facets, facetCount);
        int hitcount = result.getHitCount();
        List<FacetValue> facetValue = result.getFacets().stream().findAny().get().getFacetValues();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(10)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(greaterThanOrEqualTo(facetCount)));
    }

    /**
     * Test of findEbiSearchResultsByEC method, of class EnzymeCentricService.
     */
    @Test
    public void testFindEbiSearchResultsByEC() {

        String ec = "6.1.1.1";
        int startPage = 0;
        int pageSize = 10;
        String filter = "TAXONOMY:9606";
        List<String> filters = new ArrayList<>();
        filters.add(filter);
        String facets = filters.stream().collect(Collectors.joining(","));
        int facetCount = 5;

        EnzymeSearchResult result = enzymeCentricService.findEbiSearchResultsByEC(ec, startPage, pageSize, facets, facetCount);
        int hitcount = result.getHitCount();
        List<FacetValue> facetValue = result.getFacets().stream().findAny().get().getFacetValues();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(1));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(greaterThanOrEqualTo(facetCount)));
    }

    /**
     * Test of findEbiSearchResultsByTaxId method, of class
     * EnzymeCentricService.
     */
    @Test
    public void testFindEbiSearchResultsByTaxId() {

        String taxId = "9606";
        int startPage = 0;
        int pageSize = 10;
        String filter = "TAXONOMY:9606";
        List<String> filters = new ArrayList<>();
        filters.add(filter);
        String facets = filters.stream().collect(Collectors.joining(","));
        int facetCount = 5;

        EnzymeSearchResult result = enzymeCentricService.findEbiSearchResultsByTaxId(taxId, startPage, pageSize, facets, facetCount);
        int hitcount = result.getHitCount();
        List<FacetValue> facetValue = result.getFacets().stream().findAny().get().getFacetValues();

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(lessThanOrEqualTo(hitcount)));
        assertThat(result.getEntries(), hasSize(pageSize));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(facetCount));
    }
}
