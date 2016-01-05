/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.analysis.config.AnalysisConfig;
import uk.ac.ebi.ep.analysis.service.DataAnalyzer;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.dataconfig.DevDataConfig;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.dataconfig.ProdDataConfig;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class AnalysisTest {

    private AnalysisTest() {

    }

    public static void main(String... args) throws Exception {

        if (args == null || args.length == 0) {
            System.out.println("Please provide required parameters");
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

        DataAnalyzer analyzer = context.getBean(DataAnalyzer.class);
        return analyzer;
    }
}
