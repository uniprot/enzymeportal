package uk.ac.ebi.ep.reaction.mechanism.model;

import uk.ac.ebi.ep.reaction.mechanism.model.Domain;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.reaction.mechanism.config.McsaConfig;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {McsaConfig.class})
public class DomainTest {

    private Domain getDomain() {
        Domain d = new Domain();
        d.setCathId("C1234");
        d.setName("MCSA");
        return d;
    }

    /**
     * Test of getName method, of class Domain.
     */
    @Test
    public void testGetName() {
        Domain d = getDomain();
        assertNotNull(d.getName());

    }

    /**
     * Test of getCathId method, of class Domain.
     */
    @Test
    public void testGetCathId() {
        Domain d = getDomain();
        assertNotNull(d.getCathId());
    }

    /**
     * Test of toString method, of class Domain.
     */
    @Test
    public void testToString() {
        Domain d = getDomain();
        assertNotNull(d.toString());
    }

}
