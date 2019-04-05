package uk.ac.ebi.ep.parser.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalCompoundParser;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Slf4j
public class CofactorParserFTP {

    public static void main(String... args) {
        if (args == null || args.length == 0) {
            System.out.println("Please provide required parameters");
            System.exit(0);
        }

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(args[0]);
         //context.getEnvironment().setActiveProfiles("uzprel");
        context.register(DataConfig.class);
        context.register(ProdDataConfig.class);
        context.register(DevDataConfig.class);
        context.register(GlobalConfig.class);
        context.scan("uk.ac.ebi.ep.parser.config");
        context.refresh();

        EnzymePortalCompoundParser compoundService = context.getBean(EnzymePortalCompoundParser.class);
        compoundService.loadCofactorsFromFTPFiles();

        log.info("done populating compound table with cofactor information... and about to load unique cofactors.");
        //compoundService.loadUniqueCofactors();
        log.info("Done loading unique cofactors.");

    }
}
