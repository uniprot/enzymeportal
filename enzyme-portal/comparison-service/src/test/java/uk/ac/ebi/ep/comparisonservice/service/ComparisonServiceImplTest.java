package uk.ac.ebi.ep.comparisonservice.service;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Lists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;
import uk.ac.ebi.ep.comparisonservice.model.Compound;
import uk.ac.ebi.ep.comparisonservice.model.Disease;
import uk.ac.ebi.ep.comparisonservice.model.Molecule;
import uk.ac.ebi.ep.comparisonservice.model.Reaction;
import uk.ac.ebi.ep.comparisonservice.model.ReactionPathway;
import uk.ac.ebi.ep.dataservice.common.CompoundRole;

/**
 *
 * @author joseph
 */
@Slf4j
public class ComparisonServiceImplTest {

    /**
     * Test of getComparisonProteinModel method, of class ComparisonServiceImpl.
     */
    @Test
    public void testGetComparisonProteinModel() {

        log.info("testGetComparisonProteinModel");
        String accession = "O76074";
        String proteinName = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        ComparisonProteinModel cpm = new ComparisonProteinModel();
        cpm.setAccession(accession);
        cpm.setProteinName(proteinName);

        assertNotNull(cpm);
        assertNotNull(cpm.getProteinName());
        assertNotNull(cpm.getAccession());

    }

    /**
     * Test of getCompareEnzymeMolecule method, of class ComparisonServiceImpl.
     */
    @Test
    public void testGetCompareEnzymeMolecule() {
        log.info("testGetComparisonProteinModel");

        Compound cofactor = new Compound("CHEBI:18420", "Mg(2+)", "CHEBI", "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:18420", CompoundRole.COFACTOR.getName());
        Compound inhibitor = new Compound("CHEMBL1737", "SILDENAFIL CITRATE", "CHEMBL", "https://www.ebi.ac.uk/chembl/compound_report_card/CHEMBL1737/", CompoundRole.INHIBITOR.getName());

        List<Compound> cp = Arrays.asList(cofactor, inhibitor);
        Molecule molecule = new Molecule();
        molecule.setActivators(Lists.emptyList());
        molecule.setCofactors(Arrays.asList(cofactor));
        molecule.setInhibitors(Arrays.asList(inhibitor));

        assertNotNull(molecule);
        List<Compound> cofactors = molecule.getCofactors();

        assertThat(cofactors, hasSize(greaterThanOrEqualTo(1)));

        List<Compound> inhibitors = molecule.getInhibitors();

        assertThat(inhibitors, hasSize(greaterThanOrEqualTo(1)));
        List<Compound> activators = molecule.getActivators();

        assertThat(activators, hasSize(0));
    }

    /**
     * Test of getCompareEnzymeReactionPathay method, of class
     * ComparisonServiceImpl.
     */
    @Test
    public void testGetCompareEnzymeReactionPathay() {
        log.info("testGetCompareEnzymeReactionPathay");

        List<String> pathways = Arrays.asList("G2/M DNA damage checkpoint", "Stabilization of p53", "Chk1/Chk2(Cds1) mediated inactivation of Cyclin B:Cdk1 complex");
        Reaction r = new Reaction("RHEA:17989", "ATP + L-seryl-[protein] = ADP + H(+) + O-phospho-L-seryl-[protein]");
        ReactionPathway result = new ReactionPathway();
        result.setPathways(pathways);
        result.setReaction(r);

        assertNotNull(result);
        assertNotNull(result.getReaction());
        assertNotNull(result.getPathways());

        assertThat(result.getReaction().getId()).isEqualTo("RHEA:17989");
        assertThat(result.getPathways(), hasSize(greaterThanOrEqualTo(1)));

        assertThat(result.getPathways().stream().findFirst().get()).isEqualToIgnoringCase("G2/M DNA damage checkpoint");
    }

    /**
     * Test of getCompareEnzymeDisease method, of class ComparisonServiceImpl.
     */
    @Test
    public void testGetCompareEnzymeDisease() {
        log.info("testGetCompareEnzymeDisease");

        String diseaseName = "Breast cancer (BC)";

        Disease disease = new Disease("114480", diseaseName, "https://omim.org/entry/114480", Arrays.asList("disease evidence from Uniprot"));

        List<Disease> result = Arrays.asList(disease);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(diseaseName.trim(), result.stream().findFirst().get().getName().trim());
        assertThat(result.stream().findFirst().get().getEvidences()).isNotEmpty();
    }

}
