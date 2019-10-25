package uk.ac.ebi.ep.parser.main;

import uk.ac.ebi.ep.parser.tools.LogParser;

/**
 *
 * @author Joseph
 */
public class LogAnalyzer {
    
    public static void main(String[] args) {
//        if (args == null || args.length == 0) {
//            System.out.println("Please provide required parameters");
//            System.exit(0);
//
//        }

        // WebLogAnalyzer.analyseLog(args[0], args[1]);
        String userHome = System.getProperty("user.home");
        
        String output = userHome + "/ep-logs/2019/";
       //String jsonFile = "/Users/joseph/ep-logs/enzymeportal.log";
       String jsonFile = "/Users/joseph/ep-logs/enzymeportalDev.log";
        //String jsonFile = "/usr/local/apache-tomcat-9.0.14/logs/enzymeportal-json.json";
        
        LogParser logParser = new LogParser();
        logParser.logReport(jsonFile, output);
        
    }
}
