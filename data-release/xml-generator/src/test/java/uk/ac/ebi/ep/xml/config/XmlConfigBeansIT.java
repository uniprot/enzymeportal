
package uk.ac.ebi.ep.xml.config;

import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ep.xml.entities.repositories.ProteinXmlRepository;

/**
 *
 * @author joseph
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlConfigBeansIT {

    @Autowired
    private Job enzymeXmlJobTest;

    @Autowired
    private Job proteinXmlJobTest;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Job proteinXmlJob;
    @Autowired
    private Job enzymeXmlJob;

    @Autowired
    private XmlDatasourceProperties xmlDatasourceProperties;
    @Autowired
    private XmlFileProperties xmlFileProperties;
    @Autowired
    private ProteinXmlRepository proteinXmlRepository;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(enzymeXmlJobTest).isNotNull();
        assertThat(enzymeXmlJob).isNotNull();
        assertThat(proteinXmlJobTest).isNotNull();
        assertThat(proteinXmlJob).isNotNull();
        assertThat(dataSource).isNotNull();
        assertThat(xmlDatasourceProperties).isNotNull();
        assertThat(xmlFileProperties).isNotNull();
        assertThat(proteinXmlRepository).isNotNull();

    }

}
