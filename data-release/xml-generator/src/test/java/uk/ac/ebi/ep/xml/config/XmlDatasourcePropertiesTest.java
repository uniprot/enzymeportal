
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
public class XmlDatasourcePropertiesTest {

    @Autowired
    private XmlDatasourceProperties xmlDatasourceProperties;

    /**
     * Test of getUrl method, of class XmlDatasourceProperties.
     */
    @Test
    public void testGetUrl() {
        assertThat(xmlDatasourceProperties.getUrl()).isNotNull();
    }

    /**
     * Test of getPassword method, of class XmlDatasourceProperties.
     */
    @Test
    public void testGetUsername() {
        assertThat(xmlDatasourceProperties.getUsername()).isNotNull();
    }

    /**
     * Test of getPassword method, of class XmlDatasourceProperties.
     */
    @Test
    public void testGetPassword() {
        assertThat(xmlDatasourceProperties.getPassword()).isNotNull();
    }

    /**
     * Test of toString method, of class XmlDatasourceProperties.
     */
    @Test
    public void testToString() {
        assertThat(xmlDatasourceProperties.toString()).isNotNull();
    }

}
