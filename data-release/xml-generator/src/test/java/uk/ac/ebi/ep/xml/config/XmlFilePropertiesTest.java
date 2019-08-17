package uk.ac.ebi.ep.xml.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author joseph
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlFilePropertiesTest {

    @Autowired
    private XmlFileProperties xmlFileProperties;

    /**
     * Test of getReleaseNumber method, of class XmlFileProperties.
     */
    @Test
    public void testGetReleaseNumber() {
        assertThat(xmlFileProperties.getReleaseNumber()).isNotNull();
        
        
    }

    /**
     * Test of getEnzymeCentric method, of class XmlFileProperties.
     */
    @Test
    public void testGetEnzymeCentric() {
        assertThat(xmlFileProperties.getEnzymeCentric()).isNotNull();
    }

    /**
     * Test of getProteinCentric method, of class XmlFileProperties.
     */
    @Test
    public void testGetProteinCentric() {
        assertThat(xmlFileProperties.getProteinCentric()).isNotNull();
    }

    /**
     * Test of getSchema method, of class XmlFileProperties.
     */
    @Test
    public void testGetSchema() {
        assertThat(xmlFileProperties.getSchema()).isNotNull();
    }

    /**
     * Test of getChunkSize method, of class XmlFileProperties.
     */
    @Test
    public void testGetChunkSize() {
        assertThat(xmlFileProperties.getChunkSize()).isNotNull();
    }

    /**
     * Test of getPageSize method, of class XmlFileProperties.
     */
    @Test
    public void testGetPageSize() {
        assertThat(xmlFileProperties.getPageSize()).isNotNull();
    }

    /**
     * Test of toString method, of class XmlFileProperties.
     */
    @Test
    public void testToString() {
        assertThat(xmlFileProperties.toString()).isNotNull();
    }

}
