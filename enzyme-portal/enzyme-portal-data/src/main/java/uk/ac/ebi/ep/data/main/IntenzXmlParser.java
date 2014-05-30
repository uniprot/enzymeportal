/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.data.service.EnzymePortalCompoundService;

/**
 *
 * @author joseph
 */
public class IntenzXmlParser {

    public static void main(String... args) throws Exception {
        if (args == null ) {
            System.out.println("Please provide required parameters");
            System.exit(0);
        }

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(args[0]);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();
        EnzymePortalCompoundService compoundService = context.getBean(EnzymePortalCompoundService.class);
        
        compoundService.parseIntenzAndLoadCompoundsAndReactions(args[1]);

    }

}
