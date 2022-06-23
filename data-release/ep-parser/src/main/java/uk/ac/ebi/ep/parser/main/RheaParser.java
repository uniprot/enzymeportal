
package uk.ac.ebi.ep.parser.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.parser.parsers.RheaReaction;

/**
 *
 * @author Joseph
 */
@Slf4j
public class RheaParser {

    public static void main(String[] args) {
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
        //context.scan("uk.ac.ebi.ep.parser.config");
         context.scan("uk.ac.ebi.ep.parser.config", "uk.ac.ebi.ep.metaboliteService");
        context.refresh();

        RheaReaction rhea = context.getBean(RheaReaction.class);

        rhea.parseAndLoadRheaReactions(null);
        log.info("Rhea reaction update is DONE");

    }
}
