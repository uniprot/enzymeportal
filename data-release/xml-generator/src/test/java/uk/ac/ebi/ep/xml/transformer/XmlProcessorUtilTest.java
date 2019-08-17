package uk.ac.ebi.ep.xml.transformer;

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
public class XmlProcessorUtilTest {

    private final XmlProcessorUtil util = new XmlProcessorUtil();

    /**
     * Test of computeEcToFamilyName method, of class XmlProcessorUtil.
     */
    @Test
    public void testComputeEcToFamilyName() {

        String familyName = util.computeEcToFamilyName(4);
        String expResult = "Lyases";
        org.junit.Assert.assertEquals(familyName, expResult);
    }

    /**
     * Test of computeFamily method, of class XmlProcessorUtil.
     */
    @Test
    public void testComputeFamily() {

        String ecClass = util.computeFamily("1.1.1.1");
        String expResult = "Oxidoreductases";
        org.junit.Assert.assertEquals(ecClass, expResult);
    }

}
