package uk.ac.ebi.ep.xml.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.xml.config.XmlConfig;
import uk.ac.ebi.ep.xml.generator.ProteinCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;
import uk.ac.ebi.ep.xml.util.Preconditions;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinXmlGenerator {

    private ProteinXmlGenerator() {
    }

    public static void main(String[] args) throws Exception {
        String profile;

        Preconditions.checkArgument(args == null || args.length != 1, "Please provide required parameters. e.g "
                + "DBconfig instance");

        profile = args[0];

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, XmlConfig.class, GlobalConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();

        XmlGenerator proteinGenerator = context.getBean(ProteinCentric.class);
        proteinGenerator.generateXmL();
        proteinGenerator.validateXML();
    }
}
