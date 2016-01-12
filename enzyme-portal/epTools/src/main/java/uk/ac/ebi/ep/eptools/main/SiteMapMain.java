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
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.sitemap.generator.EnzymePortalSiteMap;
import uk.ac.ebi.ep.sitemap.generator.SiteMapGenerator;

/**
 *
 * @author joseph
 */
public class SiteMapMain {

     private static final Logger logger = Logger.getLogger(SiteMapMain.class);
    public static void main(String... args) throws Exception {

        //////////////comment out here ///////////////////////
        //args = new String[4];
       
//        String dbConfig = "uzpdev";
//        String userHome = System.getProperty("user.home");
//        String filename = "SiteMap";
//        String testing = "true";
//        
//               
//        args[0] = dbConfig;
//        args[1] = userHome;
//        args[2] = filename;
//        args[3] = testing;
        //////////////uncomment for test only ///////////////////////          
        String profile = "";

        if (args == null || args.length == 0) {
            logger.error("Please provide required parameters");
            System.exit(0);
        }
       

        if (args.length == 4) {

            profile = args[0];

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.getEnvironment().setActiveProfiles(profile);
            context.register(DataConfig.class);
            context.register(ProdDataConfig.class);
            context.register(DevDataConfig.class);
            context.register(GlobalConfig.class);
            context.scan("uk.ac.ebi.ep.data.dataconfig");
            context.refresh();

            UniprotEntryService service = context.getBean(UniprotEntryService.class);

            SiteMapGenerator siteMapGenerator = new EnzymePortalSiteMap(service);
            boolean testMode = Boolean.parseBoolean(args[3]);
            siteMapGenerator.generateSitemap(args[1], args[2], testMode);

        }

    }
}
