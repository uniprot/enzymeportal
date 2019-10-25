package uk.ac.ebi.reaction.mechanism.service;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.reaction.mechanism.config.McsaConfig;
import uk.ac.ebi.reaction.mechanism.model.Mechanism;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.reaction.mechanism.model.Result;

/**
 *
 * @author joseph
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {McsaConfig.class})
public class ReactionMechanismServiceIT {

    @Autowired
    private ReactionMechanismService reactionMechanismService;

    /**
     * Test of findMechanismResultByEc method, of class
     * ReactionMechanismService.
     */
    @Test
    public void testFindMechanismResultByEc() {
        log.info("findMechanismResultByEc");
        String ec = "1.1.1.1";

        MechanismResult result = reactionMechanismService.findMechanismResultByEc(ec);
        assertNotNull(result);
        assertThat(result.getResults(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findMechanismResultByAccession method, of class
     * ReactionMechanismService.
     */
    @Test
    public void testFindMechanismResultByAccession() {
        log.info("findMechanismResultByAccession");
        String accession = "P00334";

        MechanismResult result = reactionMechanismService.findMechanismResultByAccession(accession);

        assertNotNull(result);
        assertThat(result.getResults(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findMechanismsByEc method, of class ReactionMechanismService.
     */
    @Test
    public void testFindMechanismsByEc() {
        log.info("findMechanismsByEc");
        String ec = "1.1.1.1";

        List<Mechanism> result = reactionMechanismService.findMechanismsByEc(ec);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findMechanismsByAccession method, of class
     * ReactionMechanismService.
     */
    @Test
    public void testFindMechanismsByAccession() {
        log.info("findMechanismsByAccession");
        String accession = "P00334";

        List<Mechanism> result = reactionMechanismService.findMechanismsByAccession(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findReactionMechanismByEc method, of class
     * ReactionMechanismService.
     */
    @Test
    public void testFindReactionMechanismByEc() {
        log.info("findReactionMechanismByEc");
        String ec = "1.1.1.1";

        List<Result> result = reactionMechanismService.findReactionMechanismByEc(ec);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findReactionMechanismByAccession method, of class
     * ReactionMechanismService.
     */
    @Test
    public void testFindReactionMechanismByAccession() {
        log.info("findReactionMechanismByAccession");
        String accession = "P00334";

        Result expResult = new Result();
        expResult.setMcsaId(255);
        expResult.setEnzymeName("alcohol dehydrogenase (SDR type)");
        expResult.setDescription("An NAD+-dependent enzyme that catalyzes the oxidation of alcohols to aldehydes/ketones and that is also able to further oxidize aldehydes to their corresponding carboxylic acids. It is a zinc-independent protein which is a member of the short-chain dehydrogenases/reductases (SDR) family. It is known to be inhibited by 2,2,2-trifluoroethanol and pyrazole.");
        expResult.setUrl("www.ebi.ac.uk/thornton-srv/m-csa/entry/255/");
        expResult.setAllEcs(Arrays.asList("1.1.1.1"));

        Result result = reactionMechanismService.findReactionMechanismByAccession(accession);
        assertEquals(expResult.getEnzymeName(), result.getEnzymeName());

    }

}
