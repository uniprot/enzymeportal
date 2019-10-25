package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamilyView;

/**
 *
 * @author joseph
 */

@Slf4j
public class EnzymePortalFamilyNameServiceIT extends DataServiceBaseIT {

    @Autowired
    EnzymePortalFamilyNameService enzymePortalFamilyNameService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(enzymePortalFamilyNameService).isNotNull();
    }

    /**
     * Test of findUniprotFamilies method, of class
     * EnzymePortalFamilyNameService.
     */
    @Test
    public void testFindProteinFamilies() {
        log.info("findProteinFamilies");

        List<ProteinFamilyView> result = enzymePortalFamilyNameService.findProteinFamilies();
        assertNotNull(result);
        log.info("Total NUmber of Uniprot families found : " + result.size());
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findFamiliesLike method, of class EnzymePortalFamilyNameService.
     */
    @Test
    public void testFindProteinFamiliesWithNamesLike() {
        log.info("findProteinFamiliesWithNamesLike");
        String name = "BMT fa";
        String family = "BMT family";

        List<ProteinFamily> result = enzymePortalFamilyNameService.findProteinFamiliesWithNamesLike(name);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.stream().findAny().get().getFamilyName()).isEqualTo(family);
    }

}
