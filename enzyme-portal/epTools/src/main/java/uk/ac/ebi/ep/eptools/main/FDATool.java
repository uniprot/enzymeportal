/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.eptools.main;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.dataconfig.DevDataConfig;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.dataconfig.ProdDataConfig;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalCompoundParser;

/**
 *
 * @author joseph
 */
@Deprecated
public class FDATool {

    private static final Logger logger = Logger.getLogger(FDATool.class);

    public static void main(String args[]) throws Exception {

        String profile = "";

        if (args == null || args.length == 0) {
            logger.error("Please provide required parameters");
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
            compoundService.loadChemblFDA();

        }

    }
}
