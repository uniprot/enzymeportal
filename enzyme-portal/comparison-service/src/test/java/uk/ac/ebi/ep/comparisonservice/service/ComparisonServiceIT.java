package uk.ac.ebi.ep.comparisonservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;
import uk.ac.ebi.ep.comparisonservice.model.Compound;
import uk.ac.ebi.ep.comparisonservice.model.Disease;
import uk.ac.ebi.ep.comparisonservice.model.Molecule;
import uk.ac.ebi.ep.comparisonservice.model.ReactionPathway;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ComparisonServiceIT {

    @Autowired
    private ComparisonService comparisonService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(comparisonService).isNotNull();
    }

    /**
     * Test of getComparisonProteinModel method, of class ComparisonService.
     */
    @Test
    public void testGetComparisonProteinModel() {
        log.info("testGetComparisonProteinModel");
        String accession = "O76074";
        String proteinName = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        ComparisonProteinModel result = comparisonService.getComparisonProteinModel(accession);
        assertNotNull(result);
        assertThat(result.getProteinName()).isEqualTo(proteinName);
        assertEquals(result.getProteinName(), proteinName);

    }

    /**
     * Test of getCompareEnzymeMolecule method, of class ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeMolecule() {
        log.info("testGetComparisonProteinModel");
        String accession = "P51160";
        Molecule result = comparisonService.getCompareEnzymeMolecule(accession);
        assertNotNull(result);
        List<Compound> cofactors = result.getCofactors();

        assertThat(cofactors, hasSize(greaterThanOrEqualTo(1)));

        List<Compound> inhibitors = result.getInhibitors();

        assertThat(inhibitors, hasSize(greaterThanOrEqualTo(1)));
        List<Compound> activators = result.getActivators();

        assertThat(activators, hasSize(0));
    }

    /**
     * Test of getCompareEnzymeReactionPathay method, of class
     * ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeReactionPathay() {
        log.info("testGetCompareEnzymeReactionPathay");
        String accession = "O76074";
        ReactionPathway result = comparisonService.getCompareEnzymeReactionPathay(accession);
        assertNotNull(result);
        assertNotNull(result.getReaction());
        assertNotNull(result.getPathways());

        assertThat(result.getReaction().getId()).isEqualTo("RHEA:16957");
        assertThat(result.getPathways(), hasSize(greaterThanOrEqualTo(1)));
        
        assertThat(result.getPathways().stream().findFirst().get()).isEqualToIgnoringCase("R-HSA-418457");
    }

    /**
     * Test of getCompareEnzymeDisease method, of class ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeDisease() {
        log.info("testGetCompareEnzymeDisease");
        String accession = "P51160";
        String diseaseName = "Achromatopsia 5 (ACHM5)";
        List<Disease> result = comparisonService.getCompareEnzymeDisease(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(diseaseName.trim(), result.stream().findFirst().get().getName().trim());
        assertThat(result.stream().findFirst().get().getEvidences()).isNotEmpty();
    }

}
