package uk.ac.ebi.ep.mm;

import org.apache.commons.cli.CommandLine;

public class EbeyeIndexer implements MmIndexer {

	/**
	 * Parses a EB-Eye XML file and indexes/stores the database identifiers
	 * corresponding to UniProt accessions.
	 * @param args
	 * <ul>
	 * 	<li>-xmlFile: the XML file to parse</li>
	 * 	<li>-indexDir: the directory for the lucene index. If it does not
	 * 		exist, a new one is created.</li>
	 * </ul>
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null){
    		MmIndexer parser = new EbeyeIndexer();
    		parser.parse(cl.getOptionValue("xmlFile"), cl.getOptionValue("indexDir"));
        }
	}	

	public void parse(String xmlFile, String luceneIndexDir) {
		// TODO Auto-generated method stub
		
	}

}
