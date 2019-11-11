package uk.ac.ebi.ep.parser.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.parser.parsers.ChebiCompounds;

/**
 *
 * @author joseph
 */
@Slf4j
public class ChebiCompoundParser {

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

        ChebiCompounds chebiCompound = context.getBean(ChebiCompounds.class);
        log.info("About to process ChEBI data from ReactionInfo Table and load UNIQUE_CHEBI_COMPOUND Table. ");
        chebiCompound.loadUniqueChebiCompoundsToDatabase();
        log.info("Done loading UNIQUE_CHEBI_COMPOUND (incl Role Type Metabolite, Reatant ) ");

        log.info("About to reconcile data from ReactionInfo & UNIQUE_CHEBI_COMPOUND,remove duplicates and load ENZYME_PORTAL_CHEBI_COMPOUND Table .....");
        chebiCompound.loadChebiCompoundsToDatabase();

        log.info("Done Loading Chebi compounds to ENZYME_PORTAL_CHEBI_COMPOUND table");

        log.info("About to load Unique metabolites to the database .....");
        chebiCompound.loadUniqueMetabolitesToDatabase();

        log.info("Done loading Unique metabolites to the database");

    }
}
