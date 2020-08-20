package uk.ac.ebi.ep.reaction.mechanism.model;

import uk.ac.ebi.ep.reaction.mechanism.model.Result;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.reaction.mechanism.config.McsaConfig;
import uk.ac.ebi.ep.reaction.mechanism.service.ReactionMechanismService;

/**
 *
 * @author joseph
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {McsaConfig.class})
public class MechanismResultTest {

    @Autowired
    private ReactionMechanismService reactionMechanismService;

    private MechanismResult getMechanismResult() {

        int pageSize = 1;
        String accession = "P00334";
        return reactionMechanismService.findMechanismResultByAccession(accession, pageSize);

    }

    /**
     * Test of getFirstResult method, of class MechanismResultTest.
     */
    @Test
    public void testGetFirstResult() {
        log.info("testGetFirstResult");
        String ec = "1.1.1.1";
        int pageSize = 2;
        List<Result> result = reactionMechanismService.findReactionMechanismByEc(ec, pageSize);
        assertNotNull(result);
        MechanismResult mechanismResult = new MechanismResult();
        mechanismResult.setResults(result);
        mechanismResult.setCount(pageSize);

        assertNotNull(mechanismResult);
        assertNotNull(mechanismResult.getResults());
        assertNotNull(mechanismResult.getCount());
        assertEquals(2, mechanismResult.getCount().intValue());
        Result firstResult = mechanismResult.getFirstResult();
        assertNotNull(firstResult);
        Assert.assertEquals(firstResult, mechanismResult.getResults().stream().findFirst().get());

    }

    /**
     * Test of getCount method, of class MechanismResultTest.
     */
    @Test
    public void testGetCount() {

        MechanismResult result = getMechanismResult();
        int count = result.getCount();

        assertThat(result.getResults(), hasSize(greaterThanOrEqualTo(1)));
        Assert.assertEquals(1, count);
    }

    /**
     * Test of getResults method, of class MechanismResultTest.
     */
    @Test
    public void testGetResults() {
        MechanismResult result = getMechanismResult();
        assertNotNull(result);
    }

    /**
     * Test of toString method, of class MechanismResultTest.
     */
    @Test
    public void testToString() {
        MechanismResult result = getMechanismResult();
        assertNotNull(result.toString());
    }

}
