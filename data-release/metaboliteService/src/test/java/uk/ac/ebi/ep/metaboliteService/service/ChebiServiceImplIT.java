package uk.ac.ebi.ep.metaboliteService.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.ep.metaboliteService.MetaboliteServiceApplicationTests;

/**
 *
 * @author joseph
 */
@Slf4j
public class ChebiServiceImplIT extends MetaboliteServiceApplicationTests {

    @Autowired
    private ChebiService chebiService;

    @org.junit.jupiter.api.Test
    public void injectedComponentsAreNotNull() {
        log.info("injectedComponentsAreNotNull");
        assertThat(chebiService).isNotNull();

    }

    /**
     * Test of getCompleteChebiEntityInformation method, of class
     * ChebiServiceImpl.
     *
     */
    @Test
    public void testGetCompleteChebiEntityInformation() {
        log.info("testGetCompleteChebiEntityInformation");
        String chebiId = "CHEBI:30839";
        Entity entity = chebiService.getCompleteChebiEntityInformation(chebiId);
        assertThat(entity).isNotNull();
    }

    /**
     * Test of getChebiSynonyms method, of class ChebiServiceImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetChebiSynonyms() throws Exception {
        log.info("testGetChebiSynonyms");
        String chebiId = "CHEBI:27732";
        List<String> synonyms = chebiService.getChebiSynonyms(chebiId);
        assertThat(synonyms).isNotNull();
        assertThat(synonyms, hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testGetChebiSynonyms_with_Invalid_Id() throws Exception {
        log.info("testGetChebiSynonyms");
        String chebiId = "CHEBI:27732XX";
        List<String> synonyms = chebiService.getChebiSynonyms(chebiId);
        assertThat(synonyms).isEmpty();

    }

}
