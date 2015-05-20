/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalPathwaysParser;

/**
 *
 * @author joseph
 */
public class PathwaysParser {

    public static void main(String args[]) throws Exception {
        String file = "";
        String profile = "";

        if (args == null || args.length == 0) {
            System.out.println("Please provide required parameters");
            System.exit(0);
        }

        if (args.length > 0) {

            profile = args[0];
            //file = args[1];

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.getEnvironment().setActiveProfiles(profile);
            //context.scan("uk.ac.ebi.ep.data.dataconfig");
            context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.parser.config");
            context.refresh();

            EnzymePortalPathwaysParser pathwaysParser = context.getBean(EnzymePortalPathwaysParser.class);

            pathwaysParser.parseReactomeFile();//Total time: 1:57:24.500s
        }

    }

}
