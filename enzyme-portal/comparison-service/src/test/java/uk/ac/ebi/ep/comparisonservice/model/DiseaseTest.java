package uk.ac.ebi.ep.comparisonservice.model;

import java.util.Arrays;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class DiseaseTest {

    private Disease getDisease() {

        return new Disease("114480", "Breast cancer (BC)", "https://omim.org/entry/114480", Arrays.asList("disease evidence from Uniprot"));

    }

    /**
     * Test of getId method, of class Disease.
     */
    @Test
    public void testGetId() {
        Disease disease = getDisease();
        assertNotNull(disease.getId());
    }

    /**
     * Test of getName method, of class Disease.
     */
    @Test
    public void testGetName() {
        Disease disease = getDisease();
        assertNotNull(disease.getName());
    }

    /**
     * Test of getUrl method, of class Disease.
     */
    @Test
    public void testGetUrl() {
        Disease disease = getDisease();
        assertNotNull(disease.getUrl());
    }

    /**
     * Test of getEvidences method, of class Disease.
     */
    @Test
    public void testGetEvidences() {
        Disease disease = getDisease();
        assertNotNull(disease.getEvidences());
        assertThat(disease.getEvidences(), hasSize(greaterThanOrEqualTo(1)));
    }

}
