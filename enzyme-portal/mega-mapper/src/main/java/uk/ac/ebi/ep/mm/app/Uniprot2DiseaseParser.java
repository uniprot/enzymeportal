package uk.ac.ebi.ep.mm.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 * Class to parse the HTML
 * <a href="http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html">file</a>
 * containing a table of equivalences from UniProt accessions to OMIM IDs and
 * MeSH terms.
 * @author rafa
 *
 */
public class Uniprot2DiseaseParser implements MmParser {

	private static final Logger LOGGER =
			Logger.getLogger(Uniprot2DiseaseParser.class);
	
	private MegaMapper mm;
	
	/**
	 * Minimum scores to accept a mapping.
	 */
	private final double minScore = 2.5;
	
	/**
	 * Parses the HTML file and stores in the mega-map the xrefs from UniProt
	 * accessions to OMIM and MeSH terms, but only if the scores is equal or
	 * greater than {@link #minScore}.
	 * @param args see {@link CliOptionsParser#getCommandLine(String...)}
	 * @throws Exception in case of error while parsing.
	 */
	public static void main(String[] args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null){
    		MegaMapper mm = null;
    		Connection con = null;
        	MmParser parser = new Uniprot2DiseaseParser();
        	try {
        		final String dbConfig = cl.getOptionValue("dbConfig");
        		con = OracleDatabaseInstance
        				.getInstance(dbConfig).getConnection();
        		con.setAutoCommit(false);
        		mm = new MegaJdbcMapper(con);
            	parser.setWriter(mm);
        		parser.parse(cl.getOptionValue("xmlFile"));
        		mm.commit();
    		} catch (Exception e){
    			mm.rollback();
    		} finally {
    			mm.closeMap();
    			if (con != null) con.close();
        	}
        }
	}

	public void parse(String htmlFile) throws Exception {
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {
			mm.openMap();
			is = htmlFile.startsWith("http://")?
					new URL(htmlFile).openStream():
					new FileInputStream(htmlFile);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			Pattern p = Pattern.compile("^(?:</TR>)?<TR><TD>(.*?)<\\/TD>" +
					"<TD>(.*?)<\\/TD><TD>(.*?)<\\/TD><TD>(.*?)<\\/TD>" +
					"<TD>(.*?)<\\/TD>");
            LOGGER.info("Parsing start");
			String line;
			while ((line = br.readLine()) != null){
				Matcher m = p.matcher(line);
				if (!m.matches()) continue;
				
				// Start with the scores, discard immediately if not interesting,
				// otherwise keep for the MeSH terms (see below):
				String[] scoresCell = m.group(5).split(" / ");
				double[] scores = new double[scoresCell.length];
				boolean interesting = false;
				for (int i = 0; i < scoresCell.length; i++) {
					if (scoresCell[i].equals("exact")){
						scores[i] = Double.MAX_VALUE;
					} else {
						scores[i] = Double.valueOf(scoresCell[i]);
					}
					if (scores[i] > minScore) interesting = true;
				}
				if (!interesting) continue;
				
				LOGGER.debug("Score over " + minScore);
				
				String uniprotAcc = m.group(1).replaceAll("<\\/?a[^>]*>", "");
				Entry uniprotEntry = mm.getEntryForAccession(
						MmDatabase.UniProt, uniprotAcc);
				// XXX: Ignore if not found in the enzymes mega-map:
				if (uniprotEntry == null) continue;
				
				LOGGER.debug("UniProt accession is an enzyme: " + uniprotAcc);
				
				Collection<XRef> diseaseXrefs = new ArrayList<XRef>();

				String[] omimCell = m.group(2).replaceAll("<\\/?a[^>]*>", "")
						.split("\\s");
				for (int i = 0; i < omimCell.length; i++) {
					if (omimCell[i].equals("-")) continue;
					Entry omimEntry = new Entry();
					omimEntry.setDbName(MmDatabase.OMIM.name());
					omimEntry.setEntryId(omimCell[i]);
					XRef omimXref = new XRef();
					omimXref.setFromEntry(uniprotEntry);
					omimXref.setRelationship(Relationship.between(
							MmDatabase.UniProt, MmDatabase.OMIM).name());
					omimXref.setToEntry(omimEntry);
					diseaseXrefs.add(omimXref);
					LOGGER.debug("OMIM xref to " + omimCell[i]);
									
				}
				
				String[] meshIdsCell = m.group(3).replaceAll("<\\/?a[^>]*>", "")
						.split(" / ");
				String[] meshHeadsCell = m.group(4).split(" / ");
				// Same number of MeSH IDs, headers and scores
				for (int i = 0; i < scores.length; i++) {
					if (scores[i] < minScore) continue;
					Entry meshEntry = new Entry();
					meshEntry.setDbName(MmDatabase.MeSH.name());
					meshEntry.setEntryId(meshIdsCell[i]);
					meshEntry.setEntryName(meshHeadsCell[i]);
					XRef meshXref = new XRef();
					meshXref.setFromEntry(uniprotEntry);
					meshXref.setRelationship(Relationship.between(
							MmDatabase.UniProt, MmDatabase.MeSH).name());
					meshXref.setToEntry(meshEntry);
					diseaseXrefs.add(meshXref);
					LOGGER.debug("MeSH xref to " + meshIdsCell[i]);
				}

				// Now look if the MeSH heading matches an EFO entry:
				
				
//				mm.writeXrefs(diseaseXrefs);
			}
            LOGGER.info("Parsing end");
            mm.closeMap();
            LOGGER.info("Map closed");
        } catch (Exception e){
            LOGGER.error("During parsing", e);
            mm.handleError();
            throw e;
		} finally {
			if (is != null) is.close();
			if (br != null) br.close();
		}
		
	}

	public void setWriter(MegaMapper mm) {
		this.mm = mm;
	}
}
