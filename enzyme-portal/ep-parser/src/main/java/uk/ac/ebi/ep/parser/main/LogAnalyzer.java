package uk.ac.ebi.ep.parser.main;

import uk.ac.ebi.ep.parser.tools.WebLogAnalyzer;

/**
 *
 * @author Joseph
 */
public class LogAnalyzer {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("Please provide required parameters");
            System.exit(0);

        }

         WebLogAnalyzer.analyseLog(args[0], args[1]);

    }
}
