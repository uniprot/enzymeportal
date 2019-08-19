package uk.ac.ebi.ep.xml.transformer;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class XmlTransformerTest {

    @Autowired
    private ProteinXmlRepository proteinXmlRepository;
    private final XmlTransformer protein = new ProteinGroupsProcessor(proteinXmlRepository);

    /**
     * Test of withResourceField method, of class XmlTransformer.
     */
    @Test
    public void testWithResourceField() {

        String resourceId = "ID123";
        String accession = "P1234";
        String commonName = "Human";
        int entryType = 0;
        String with = protein.withResourceField(resourceId, accession, commonName, entryType);
        String expResult = "ID123;P1234;Human;0";
        assertThat(with).isEqualTo(expResult);
    }

}
