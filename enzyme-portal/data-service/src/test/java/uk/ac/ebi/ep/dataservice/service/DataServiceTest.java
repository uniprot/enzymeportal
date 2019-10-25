package uk.ac.ebi.ep.dataservice.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import uk.ac.ebi.ep.dataservice.dto.ProteinView;

/**
 *
 * @author joseph
 */
@Slf4j
public class DataServiceTest extends DataServiceBaseIT {

    @Autowired
    private DataService dataService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataService).isNotNull();
    }

    /**
     * Test of findProteinViewByAccession method, of class DataService.
     */
    @Test
    public void testFindProteinViewByAccession() {
        log.info("findProteinViewByAccession");
        String accession = "P18545";

        String proteinName = "Retinal rod rhodopsin-sensitive cGMP 3',5'-cyclic phosphodiesterase subunit gamma";
        ProteinView result = dataService.findProteinViewByAccession(accession);

        assertNotNull(result);
        assertEquals(proteinName, result.getProteinName());

    }

    /**
     * Test of findProteinViewByAccession method, of class DataService.
     */
    @Test
    public void testFindProteinViewByAccession_experimental_evidence() {
        log.info("findProteinViewByAccession");

        String accession = "A1L167";

        String proteinName = "Ubiquitin-conjugating enzyme E2Q-like protein 1";
        ProteinView result = dataService.findProteinViewByAccession(accession);

        assertNotNull(result);
        assertEquals(proteinName, result.getProteinName());
        assertEquals(Boolean.TRUE, result.getExpEvidence());
        assertEquals(BigInteger.ONE, result.getExpEvidenceFlag());

    }

    /**
     * Test of findProteinViewByRelatedProteinId method, of class DataService.
     */
    @Test
    public void testFindProteinViewByRelatedProteinId() {
        log.info("findProteinViewByRelatedProteinId");

        Long relId = 31362217L;

        List<ProteinView> result = dataService.findProteinViewByRelatedProteinId(relId);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findEcNumbersByAccession method, of class DataService.
     */
    @Test
    public void testFindEcNumbersByAccession() {

        log.info("testFindEcNumbersByAccession");

        String accession = "P18545";
        String expResult = "3.1.4.35";
        List<String> result = dataService.findEcNumbersByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(expResult, result.stream().findFirst().get());
    }

    /**
     * Test of findPdbViewsByAccession method, of class DataService.
     */
    @Test
    public void testFindPdbViewsByAccession() {

        log.info("testFindPdbViewsByAccession");

        String accession = "P18545";

        List<PdbView> result = dataService.findPdbViewsByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findPdbCodesByAccession method, of class DataService.
     */
    @Test
    public void testFindPathwayIdsByAccession() {
        log.info("testFindPathwayIdsByAccession");

        String accession = "P18545";
        List<String> expResult = Arrays.asList("R-HSA-4086398", "R-HSA-2514859", "R-HSA-2485179");
        List<String> result = dataService.findPathwayIdsByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result).containsAnyOf("R-HSA-2514859", "R-HSA-2485179");
        assertThat(result).containsAll(expResult);

    }

    /**
     * Test of findProteinViewByAccession method, of class DataService.
     */
    @Test
    public void testFindDiseaseViewByAccession() {
        log.info("findProteinViewByAccession");
        String accession = "P18545";

        String diseaseName = "Retinitis pigmentosa 57 (RP57)";
        List<DiseaseView> result = dataService.findDiseaseViewByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(diseaseName.trim(), result.stream().findFirst().get().getDiseaseName().trim());

    }

    /**
     * Test of findCatalyticActivitiesByAccession method, of class DataService.
     */
    @Test
    public void testFindCatalyticActivitiesByAccession() {
        log.info("testFindCatalyticActivitiesByAccession");
        String accession = "P18545";
        List<String> result = dataService.findCatalyticActivitiesByAccession(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findReactionsByAccession method, of class DataService.
     */
    @Test
    public void testFindReactionsByAccession() {
        log.info("testFindReactionsByAccession");
        String accession = "P18545";
        String expResult = "RHEA:16957";
        List<EnzymeReactionView> result = dataService.findReactionsByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(expResult, result.stream().findFirst().get().getId());

    }

    /**
     * Test of findCompoundsByAccession method, of class DataService.
     */
    @Test
    public void testFindCompoundsByAccession() {
        log.info("testFindCompoundsByAccession");
        String accession = "O76074";

        List<CompoundView> result = dataService.findCompoundsByAccession(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findCompoundsByAccession method, of class DataService.
     */
    @Test
    public void testFindCompoundsByAccession_group_by_role() {
        log.info("testFindCompoundsByAccession_group_by_role");
        String accession = "O76074";

        List<CompoundView> result = dataService.findCompoundsByAccession(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

        List<CompoundView> cofactors = result.stream()
                .filter(role -> role.getRole().equalsIgnoreCase("COFACTOR"))
                .collect(Collectors.toList());
        assertThat(cofactors, hasSize(greaterThanOrEqualTo(1)));

        List<CompoundView> inhibitors = result.stream()
                .filter(role -> role.getRole().equalsIgnoreCase("INHIBITOR"))
                .collect(Collectors.toList());
        assertThat(inhibitors, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findCompoundsByAccession method, of class DataService.
     */
    @Test
    public void testFindCompoundsByAccession_group_by_role_activator() {
        log.info("testFindCompoundsByAccession_group_by_role_activator");
        String accession = "P33402";

        List<CompoundView> result = dataService.findCompoundsByAccession(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

        List<CompoundView> activators = result.stream()
                .filter(role -> role.getRole().equalsIgnoreCase("ACTIVATOR"))
                .collect(Collectors.toList());

        assertThat(activators, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findChemblTargetIdByAccession method, of class DataService.
     */
    @Test
    public void testFindChemblTargetIdByAccession() {
        log.info("testFindChemblTargetIdByAccession");
        String accession = "P33402";
        String expResult = "CHEMBL2111348";
        List<String> result = dataService.findChemblTargetIdByAccession(accession);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

        assertEquals(expResult, result.stream().distinct().findFirst().get());

    }
}
