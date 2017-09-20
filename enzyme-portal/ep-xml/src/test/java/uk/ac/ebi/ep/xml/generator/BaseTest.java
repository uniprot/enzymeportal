package uk.ac.ebi.ep.xml.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.model.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.model.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.xml.config.MockXmlConfig;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, MockXmlConfig.class})
public abstract class BaseTest extends TestCase {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    protected EnzymePortalXmlService xmlService;

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected XmlConfigParams mockXmlConfigParams;

    @Autowired
    protected SessionFactory sessionFactory;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected String resolvePath(String folder) {
        return temporaryFolder
                .getRoot().toPath()
                .resolve(folder)
                .toString();
    }

    protected void printGeneratedXML(File output) throws IOException {
        //  peek the generated file   
        try (Stream<String> data = Files.lines(output.toPath(), StandardCharsets.UTF_8)) {
            data.forEach(d -> System.out.println(d));
        }
    }

    protected void printXml(String fileLocation) throws IOException {
        try (FileReader fileReader = new FileReader(fileLocation);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
