/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.service.EnzymePortalService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeFinderIT extends BaseTest {

    /**
     * Test of getService method, of class EnzymeFinder.
     */
    @Test
    public void testGetEnzymePortalService() {

        EnzymePortalService result = enzymeFinder.getService();
        assertNotNull(result);

    }

    /**
     * Test of findAccessionsforSearchTerm method, of class EnzymeFinder.
     */
    @Test
    public void testFindAccessionsforSearchTerm() {
        String searchTerm = "sildenafil";
        int limit = 800;
        int expectedResult = 3;

        List<String> result = enzymeFinder.findAccessionsforSearchTerm(searchTerm, limit);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of getAssociatedProteinsByEc method, of class EnzymeFinder.
     */
    @Test
    public void testGetAssociatedProteinsByEc() {
        String ec = "3.4.24.85";
        int limit = 800;
        int expectedResult = 1;

        SearchResults result = enzymeFinder.getAssociatedProteinsByEc(ec, limit);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));

    }

    /**
     * Test of getAssociatedProteinsByTaxIdAndEc method, of class EnzymeFinder.
     */
    @Test
    public void testGetAssociatedProteinsByTaxIdAndEc() {

        String taxId = "9606";
        String ec = "3.4.24.85";
        int limit = 30;
        int expectedResult = 1;

        SearchResults result = enzymeFinder.getAssociatedProteinsByTaxIdAndEc(taxId, ec, limit);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of getAssociatedProteinsByEcAndFulltextSearch method, of class
     * EnzymeFinder.
     */
    @Test
    public void testGetAssociatedProteinsByEcAndFulltextSearch() {

        String ec = "3.4.24.85";
        String searchTerm = "Membrane-bound transcription factor site-2 protease";
        int limit = 100;
        int expectedResult = 1;

        SearchResults result = enzymeFinder.getAssociatedProteinsByEcAndFulltextSearch(ec, searchTerm, limit);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of getEnzymes method, of class EnzymeFinder.
     */
    @Test
    public void testGetEnzymes() {
        int expectedResult = 1;
        SearchParams searchParams = new SearchParams();
        searchParams.setCompound("Q64441");
        searchParams.setText("sildenafil");
        searchParams.setPrevioustext("Q64441");
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setSpecies(new ArrayList<>());

        SearchResults result = enzymeFinder.getEnzymes(searchParams);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of findDiseases method, of class EnzymeFinder.
     */
    @Test
    public void testFindDiseases() {
        int expectedResult = 3;
        List<EnzymePortalDisease> result = enzymeFinder.findDiseases();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of computeEnzymeSummariesByOmimNumber method, of class EnzymeFinder.
     */
    @Test
    public void testComputeEnzymeSummariesByOmimNumber() {
        int expectedResult = 1;
        String omimNumber = "300918";

        SearchResults result = enzymeFinder.computeEnzymeSummariesByOmimNumber(omimNumber);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of computeEnzymeSummariesByPathwayId method, of class EnzymeFinder.
     */
    @Test
    public void testComputeEnzymeSummariesByPathwayId() {
        int expectedResult = 1;
        String pathwayId = "REACT_11048";

        SearchResults result = enzymeFinder.computeEnzymeSummariesByPathwayId(pathwayId);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of computeEnzymeSummariesByEc method, of class EnzymeFinder.
     */
    @Test
    public void testComputeEnzymeSummariesByEc() {
        int expectedResult = 1;
        String ec = "3.4.24.85";
        SearchResults result = enzymeFinder.computeEnzymeSummariesByEc(ec);
        assertNotNull(result);
        assertThat(result.getSummaryentries(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of findAllReactions method, of class EnzymeFinder.
     */
    @Test
    public void testFindAllReactions() {
        int expectedResult = 8;
        List<EnzymePortalReaction> result = enzymeFinder.findAllReactions();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(expectedResult)));
    }

    /**
     * Test of findAllPathways method, of class EnzymeFinder.
     */
    @Test
    public void testFindAllPathways() {
        int expectedResult = 8;
        List<EnzymePortalPathways> result = enzymeFinder.findAllPathways();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(expectedResult)));
    }

}
