/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.sitemap.generator.EnzymePortalSiteMap;
import uk.ac.ebi.ep.sitemap.generator.SiteMapGenerator;

/**
 *
 * @author joseph
 */
public class SiteMapMain {

    public static void main(String... args) throws Exception {

        //////////////comment here ///////////////////////
//        args = new String[4];
//        //String dbConfig = "ep-mm-db-enzdev";
//        //String dbConfig = "ep-mm-db-enzprel";
//        String userHome = System.getProperty("user.home");
//        String filename = "SiteMap";
//        String testing = "true";
//        
//               
//        args[0] = "vezpdev";
//        args[1] = userHome;
//        args[2] = filename;
//        args[3] = testing;
       //////////////uncomment for testing parameters only ///////////////////////          
        if (args == null) {
            System.out.println("Please provide required parameters");
            System.exit(0);
        }

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(args[0]);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();

        UniprotEntryService service = context.getBean(UniprotEntryService.class);

        SiteMapGenerator siteMapGenerator = new EnzymePortalSiteMap(service);
        boolean testMode = Boolean.parseBoolean(args[3]);
        siteMapGenerator.generateSitemap(args[1], args[2], testMode);

    }
}
