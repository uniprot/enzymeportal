/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalPDBeParser;

/**
 *
 * @author joseph
 */
public class PDBeParser {

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
            context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.parser.config");
            context.refresh();

            EnzymePortalPDBeParser pdbParser = context.getBean(EnzymePortalPDBeParser.class);

            pdbParser.updatePDBeData();
        }

    }

}
