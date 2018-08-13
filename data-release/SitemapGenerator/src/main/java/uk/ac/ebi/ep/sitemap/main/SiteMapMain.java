
package uk.ac.ebi.ep.sitemap.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.model.service.SitemapService;
import uk.ac.ebi.ep.sitemap.generator.EnzymePortalSiteMap;
import uk.ac.ebi.ep.sitemap.generator.SiteMapGenerator;

/**
 *
 * @author joseph
 */
public class SiteMapMain {

    public static void main(String... args) throws Exception {

        //////////////comment out here ///////////////////////
//        args = new String[4];
//       
//        String dbConfig = "uzprel";
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
            System.out.println("Please provide required parameters");
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
            context.scan("uk.ac.ebi.ep.config");
            context.refresh();

            SitemapService sitemapService = context.getBean(SitemapService.class);
           

            SiteMapGenerator siteMapGenerator = new EnzymePortalSiteMap(sitemapService);
            boolean testMode = Boolean.parseBoolean(args[3]);
            siteMapGenerator.generateSitemap(args[1], args[2], testMode);

        }

    }
}
