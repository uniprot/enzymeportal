/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.analysis.config.AnalysisConfig;
import uk.ac.ebi.ep.analysis.service.DataAnalyzer;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class AnalysisTest {
//private static final Logger logger = Logger.getLogger(AnalysisTest.class);
    private AnalysisTest() {

    }

    public static void main(String... args) throws Exception {

        if (args == null || args.length == 0) {
           // logger.error("Please provide required parameters");
            System.exit(0);
        }

        if (args.length == 4) {

            DataAnalyzer analyzer = prepareAnalyser(args);
            String fileDir = args[1];
            String filename = args[2];
            boolean deleteFile = Boolean.parseBoolean(args[3]);

            analyzer.writeToFile(fileDir, filename, deleteFile);

        } else {
            DataAnalyzer analyzer = prepareAnalyser(args);
            analyzer.writeToFile(null, null, false);
        }

    }

    public static DataAnalyzer prepareAnalyser(String... args) {
        String profile = args[0];

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(AnalysisConfig.class);
        context.register(DataConfig.class);
        context.register(ProdDataConfig.class);
        context.register(DevDataConfig.class);
        context.register(GlobalConfig.class);
        context.scan("uk.ac.ebi.ep.analysis.config");
        context.refresh();

        return context.getBean(DataAnalyzer.class);
    }
}
