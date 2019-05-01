/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Joseph
 */
@Slf4j
public class ChEMBL {

    public static void main(String args[]) throws Exception {

        String profile = "";

        if (args == null || args.length == 0) {
            System.out.println("Please provide required parameters");
            System.exit(0);
        }

        if (args.length == 1) {

            profile = args[0];

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.getEnvironment().setActiveProfiles(profile);
            context.register(DataConfig.class);
            context.register(ProdDataConfig.class);
            context.register(DevDataConfig.class);
            context.register(GlobalConfig.class);
            context.scan("uk.ac.ebi.ep.parser.config");
            context.refresh();

            EnzymePortalCompoundParser compoundService = context.getBean(EnzymePortalCompoundParser.class);
            log.info("About to load ChEMBL targets to the database");
            compoundService.loadChemblTargets();
            log.info("Done loading targets and now about to run loadChemblFDA()");
            //activators & inhibitors
            compoundService.loadChemblFDA();
            log.info("Done loading FDA approved drugs to the database");
            //log.info("Done running FDA and now about to process more inhibitors by running processChemblTargetsAndUpdateDatabase()");
            //more activators & inhibitors
            //compoundService.processChemblTargetsAndUpdateDatabase();

        }

    }
}
