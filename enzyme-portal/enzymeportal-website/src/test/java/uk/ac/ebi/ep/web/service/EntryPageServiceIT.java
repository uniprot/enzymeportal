package uk.ac.ebi.ep.web.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.web.EnzymeportalWebsiteApplicationTests;
import uk.ac.ebi.ep.web.model.EnzymeEntryPage;
import uk.ac.ebi.ep.web.model.EnzymeModel;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;

/**
 *
 * @author joseph
 */
@Slf4j
public class EntryPageServiceIT extends EnzymeportalWebsiteApplicationTests {

    @Autowired
    private EntryPageService entryPageService;

    @Test
    void injectedComponentsAreNotNull() {

        assertThat(entryPageService).isNotNull();
    }

    /**
     * Test of getDefaultEnzymeModel method, of class EntryPageService.
     */
    @Test
    public void testGetDefaultEnzymeModel() {
        log.info("testGetDefaultEnzymeModel");
        String accession = "O76074";
        String proteinName = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        EnzymeModel result = entryPageService.getDefaultEnzymeModel(accession);
        assertNotNull(result);
        assertThat(result.getProteinName()).isEqualTo(proteinName);
        assertEquals(result.getProteinName(), proteinName);
        assertThat(result.getRelatedspecies()).isNotEmpty();
        assertThat(result.getRelatedspecies(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getEntryType()).isZero();
        assertThat(result.getSpecies().getCommonname()).isEqualToIgnoringCase("Human");
    }

    /**
     * Test of showEntryEnzymePage method, of class EntryPageService.
     */
    @Test
    public void testShowEntryEnzymePage() {
        log.info("testShowEntryEnzymePage");
        String accession = "O76074";
        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeEntryPage result = entryPageService.showEntryEnzymePage(model).getEnzyme();
        assertNotNull(result);

        assertThat(result.getAccession()).isEqualTo(accession);

        assertThat(result.getSynonyms()).isNotEmpty();
        assertThat(result.getFunction()).isNotEmpty();
        assertThat(result.getEnzymeHierarchies(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getSequence()).isEqualTo(875);

    }

    /**
     * Test of showProteinStructurePage method, of class EntryPageService.
     */
    @Test
    public void testShowProteinStructurePage() {
        log.info("testShowProteinStructurePage");
        String accession = "O76074";
        String pdb = "Crystal structure of PDE5A1-IBMX";
        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();

        List<PdbView> result = entryPageService.showProteinStructurePage(model).getProteinstructure();
        assertNotNull(result);

        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
       // assertEquals(pdb.trim(), result.stream().findFirst().get().getName().trim());
        assertThat(result.stream().findFirst().get().getId()).isNotEmpty();

    }

    /**
     * Test of showRheaReactionsAndMechanisms method, of class EntryPageService.
     */
    @Test
    public void testShowRheaReactionsAndMechanisms() {
        log.info("testShowRheaReactionsAndMechanisms");

        String accession = "P27830";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showRheaReactionsAndMechanisms(model);

        assertNotNull(result);
        List<String> catalyticActivities = result.getCatalyticActivities();
        assertNotNull(catalyticActivities);

        assertThat(catalyticActivities, hasSize(greaterThanOrEqualTo(1)));
        MechanismResult mechanismResult = result.getReactionMechanism();
        assertNotNull(mechanismResult);
        assertThat(mechanismResult.getCount()).isGreaterThan(0);

        List<EnzymeReactionView> reactions = result.getEnzymeReactions();
        assertNotNull(reactions);

        assertThat(reactions, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of showPathwaysPage method, of class EntryPageService.
     */
    @Test
    public void testShowPathwaysPage() {
        log.info("testShowPathwaysPage");
        String accession = "O76074";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showPathwaysPage(model);

        assertNotNull(result);

        List<String> pathways = result.getPathways();
        assertNotNull(pathways);
        assertThat(pathways).isNotEmpty();

        assertThat(pathways, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of showLiteraturePage method, of class EntryPageService.
     */
    @Test
    public void testShowLiteraturePage() {
        log.info("testShowLiteraturePage");
        String accession = "O76074";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showLiteraturePage(model, 10);

        assertNotNull(result);
        List<LabelledCitation> citations = result.getLiterature();
        assertNotNull(citations);
        assertThat(citations).isNotEmpty();

        assertThat(citations, hasSize(greaterThanOrEqualTo(10)));
    }

    /**
     * Test of showDiseasePage method, of class EntryPageService.
     */
    @Test
    public void testShowDiseasePage() {
        log.info("testShowDiseasePage");

        String accession = "P51160";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showDiseasePage(model);

        assertNotNull(result);

        List<DiseaseView> diseases = result.getDisease();
        assertNotNull(diseases);
        assertThat(diseases).isNotEmpty();

        assertThat(diseases, hasSize(greaterThanOrEqualTo(1)));
        assertEquals("Achromatopsia 5 (ACHM5)", diseases.stream().findFirst().get().getDiseaseName().trim());
        assertThat(diseases.stream().findFirst().get().getEvidence()).isNotEmpty();
    }

    /**
     * Test of showCompoundPage method, of class EntryPageService.
     */
    @Test
    public void testShowCompoundPage() {
        log.info("testShowCompoundPage");
        String accession = "P51160";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showCompoundPage(model);

        assertNotNull(result);

        List<CompoundView> cofactors = result.getCofactors();

        assertThat(cofactors, hasSize(greaterThanOrEqualTo(1)));

        List<CompoundView> inhibitors = result.getInhibitors();

        assertThat(inhibitors, hasSize(greaterThanOrEqualTo(1)));
        List<CompoundView> activators = result.getActivators();

        assertThat(activators, hasSize(0));
    }

    @Test
    public void testShowCompoundPage_Activators_Only() {
        log.info("testShowCompoundPage");

        String accession = "P33402";

        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeModel result = entryPageService.showCompoundPage(model);

        assertNotNull(result);

        List<CompoundView> cofactors = result.getCofactors();

        assertThat(cofactors, hasSize(0));

        List<CompoundView> inhibitors = result.getInhibitors();

        assertThat(inhibitors, hasSize(0));
        List<CompoundView> activators = result.getActivators();

        assertThat(activators, hasSize(greaterThanOrEqualTo(1)));
    }

}
