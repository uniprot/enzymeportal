package uk.ac.ebi.ep.parser.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.model.service.WebStatService;

/**
 *
 * @author joseph
 */
@Slf4j
public class StatisticsParser {

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
        context.scan("uk.ac.ebi.ep.parser.config","uk.ac.ebi.ep.metaboliteService", "uk.ac.ebi.ep.model.service");
        context.refresh();

        WebStatService webStatService = context.getBean(WebStatService.class);

        log.info("About to load webStatComponents to the database");
        webStatService.loadWebStatComponentToDatabase();
        log.info("About to load webStatXrefs to the database");
        webStatService.loadWebStatXrefToDatabase();

        log.info("Done loading statisitics to the database");

    }
}
