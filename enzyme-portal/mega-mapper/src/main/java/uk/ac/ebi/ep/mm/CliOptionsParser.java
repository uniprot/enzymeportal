package uk.ac.ebi.ep.mm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A parser of the command line options for the mega-mapper parsers.
 * @author rafa
 *
 */
public class CliOptionsParser {

	/**
	 * Processes the command line options.
	 * @param args (order does not matter):
	 * 		-xmlFile &lt;xmlFile&gt; -indexDir &lt;indexDir&gt;
	 * <ul>
	 * 	<li>-xmlFile: the XML file to parse</li>
	 * 	<li>-indexDir: the directory for the lucene index. If it does not
	 * 		exist, a new one is created.</li>
	 * </ul>
	 * @return a commandLine object with the required options, or
	 * 		<code>null</code> if the arguments are not valid.
	 */
	@SuppressWarnings("static-access")
	static CommandLine getCommandLine(String... args) {
		Options options = new Options();
        options.addOption(OptionBuilder.isRequired()
                .hasArg().withArgName("xmlFile")
                .withDescription("UniProt XML file")
                .create("xmlFile"));
        options.addOption(OptionBuilder.isRequired()
                .hasArg().withArgName("indexDir")
                .withDescription("Lucene index directory")
                .create("indexDir"));
        CommandLine cl = null;
        try {
            cl = new GnuParser().parse(options, args);
        } catch (ParseException e){
            new HelpFormatter().printHelp(UniprotIndexer.class.getName(), options);
        }
		return cl;
	}

}
