package uk.ac.ebi.ep.parser.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.metaboliteService.service.MetabolightService;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.parser.parsers.MetaboliteParser;

/**
 *
 * @author joseph
 */
@Slf4j
public class MetabolightParser {

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
        context.scan("uk.ac.ebi.ep.parser.config", "uk.ac.ebi.ep.metaboliteService");
        context.refresh();

        MetaboliteParser metaboliteParser = context.getBean(MetaboliteParser.class);

        MetabolightService metabolightService = context.getBean(MetabolightService.class);

        metaboliteParser.updateMetabolite(metabolightService);

        log.info("Done updating ENZYME_PORTAL_REACTANT table with metabolites information");

    }
}
