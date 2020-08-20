package uk.ac.ebi.ep.reaction.mechanism.model;

import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertFalse;
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
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {McsaConfig.class})
public class MechanismTest {

    private Mechanism getMechanism() {
        Mechanism m = new Mechanism();

        m.setMechanismId(255);
        m.setIsDetailed(false);
        m.setRating(3);
        m.setComponentsSummary("cofactor used, overall product formed, proton transfer, hydride transfer, aromatic bimolecular nucleophilic addition");
        m.setMechanismText("An NAD+-dependent enzyme that catalyzes the oxidation of alcohols to aldehydes/ketones");

        return m;
    }

    /**
     * Test of getMechanismId method, of class Mechanism.
     */
    @Test
    public void testGetMechanismId() {
        Mechanism m = getMechanism();
        assertNotNull(m.getMechanismId());
    }

    /**
     * Test of getIsDetailed method, of class Mechanism.
     */
    @Test
    public void testGetIsDetailed() {
        Mechanism m = getMechanism();
        assertFalse(m.getIsDetailed());

    }

    /**
     * Test of getMechanismText method, of class Mechanism.
     */
    @Test
    public void testGetMechanismText() {
        Mechanism m = getMechanism();
        assertNotNull(m.getMechanismText());
    }

    /**
     * Test of getRating method, of class Mechanism.
     */
    @Test
    public void testGetRating() {
        Mechanism m = getMechanism();
        assertNotNull(m.getRating());
    }

    /**
     * Test of getComponentsSummary method, of class Mechanism.
     */
    @Test
    public void testGetComponentsSummary() {
        Mechanism m = getMechanism();
        assertNotNull(m.getComponentsSummary());
    }

    /**
     * Test of toString method, of class Mechanism.
     */
    @Test
    public void testToString() {
        Mechanism m = getMechanism();
        assertNotNull(m.toString());
    }

}
