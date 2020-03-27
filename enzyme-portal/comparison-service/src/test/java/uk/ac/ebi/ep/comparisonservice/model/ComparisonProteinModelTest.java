package uk.ac.ebi.ep.comparisonservice.model;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class ComparisonProteinModelTest {

    private ComparisonProteinModel getComparisonProteinModel() {
        ComparisonProteinModel model = new ComparisonProteinModel();
        model.setEc(Arrays.asList("1.1.1.1", "7.1.1.1"));
        List<String> pathways = Arrays.asList("Stabilization of p53");

        ReactionPathway reactionpathway = new ReactionPathway();
        reactionpathway.setPathways(pathways);
        model.setReactionpathway(Arrays.asList(reactionpathway));
        Disease disease = new Disease("214480", "Breast cancer", "https://omim.org/entry/214480", Arrays.asList("disease evidence from Uniprot"));

        model.setDiseases(Arrays.asList(disease));
        model.setMolecule(new Molecule());
        return model;
    }

    /**
     * Test of getEc method, of class ComparisonProteinModel.
     */
    @Test
    public void testGetEc() {
        ComparisonProteinModel result = getComparisonProteinModel();
        assertNotNull(result);
        assertThat(result.getEc(), hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of getReactionpathway method, of class ComparisonProteinModel.
     */
    @Test
    public void testGetReactionpathway() {
        ComparisonProteinModel result = getComparisonProteinModel();
        assertNotNull(result);
        assertThat(result.getReactionpathway(), hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of getDiseases method, of class ComparisonProteinModel.
     */
    @Test
    public void testGetDiseases() {
        ComparisonProteinModel result = getComparisonProteinModel();
        assertNotNull(result);
        assertThat(result.getDiseases(), hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of getMolecule method, of class ComparisonProteinModel.
     */
    @Test
    public void testGetMolecule() {
        ComparisonProteinModel result = getComparisonProteinModel();
        assertNotNull(result.getMolecule());
        assertThat(result.getMolecule().getActivators(), hasSize(0));
    }

}
