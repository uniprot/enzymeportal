package uk.ac.ebi.ep.xml.dao;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author joseph
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EcNumberTest {

    private final EcNumber ecNumber = new EcNumber();

    /**
     * Test of computeEcToFamilyName method, of class EcNumber.
     */
    @Test
    public void testComputeEcToFamilyName() {

        String familyName = ecNumber.computeEcToFamilyName(4);
        String expResult = "Lyases";
        assertThat(expResult).isEqualTo(familyName);
    }

    /**
     * Test of computeFamilyNameToEc method, of class EcNumber.
     */
    @Test
    public void testComputeFamilyNameToEc() {

        int ec = ecNumber.computeFamilyNameToEc("Oxidoreductases");
        int expResult = 1;
        assertThat(ec).isEqualTo(expResult);
    }

    /**
     * Test of computeFamily method, of class EcNumber.
     */
    @Test
    public void testComputeFamily() {
        String familyName = ecNumber.computeFamily("2.1.2.1");
        String expResult = "Transferases";
        assertThat(familyName).isEqualTo(expResult);
    }

    @Test
    public void testGetFamilies() {
        List<String> families = new ArrayList<>();
        String family = ecNumber.computeEcToFamilyName(4);
        families.add(family);
        assertThat(families).containsAnyOf("Lyases");
    }

    /**
     * Test of computeEc method, of class EcNumber.
     */
    @Test
    public void testComputeEc() {
        String familyName = ecNumber.computeEc("Ligases");
        String expResult = "6";
        assertThat(expResult).isEqualTo(familyName);
    }

}
