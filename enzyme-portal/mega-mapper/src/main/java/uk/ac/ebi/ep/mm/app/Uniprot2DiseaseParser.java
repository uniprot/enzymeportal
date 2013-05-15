package uk.ac.ebi.ep.mm.app;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.adapter.bioportal.BioportalAdapterException;
import uk.ac.ebi.ep.adapter.bioportal.BioportalConfig;
import uk.ac.ebi.ep.adapter.bioportal.BioportalOntology;


import uk.ac.ebi.ep.adapter.bioportal.BioportalWsAdapter;

import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.mm.*;


/**
 * Class to parse the file - either
 * <a href="http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html">HTML</a>
 * or <a href="http://research.isb-sib.ch/unimed/SP_MeSH.tab">tab-delimited</a>
 * - containing a table of equivalences from UniProt accessions to OMIM IDs and
 * MeSH terms.
 * @author rafa
 */
public class Uniprot2DiseaseParser implements MmParser {

	/**
	 * The format of the provided file to parse.
	 * @author rafa
	 */
	private enum Format { html, tab }
	
	private static final Logger LOGGER =
			Logger.getLogger(Uniprot2DiseaseParser.class);
	
	private MegaMapper mm;
	
	private BioportalWsAdapter bioportalAdapter;
	
	/**
	 * Minimum scores to accept a mapping.
	 */
	private final double minScore = 2.5;

	private final Pattern htmlTablePattern = Pattern.compile(
			"^(?:</TR>)?<TR><TD>(.*?)<\\/TD>" +
			"<TD>(.*?)<\\/TD><TD>(.*?)<\\/TD><TD>(.*?)<\\/TD>" +
			"<TD>(.*?)<\\/TD>");

    /**
     * Constructor.
     * @throws IOException in case of problem loading the properties file which
     *      contains the BioPortal API key.
     */
	public Uniprot2DiseaseParser() throws IOException {
		bioportalAdapter = new BioportalWsAdapter();
        final BioportalConfig bioportalConfig = new BioportalConfig();
        Properties props = new Properties();
        props.load(Uniprot2DiseaseParser.class.getClassLoader()
                .getResourceAsStream("ep-web-client.properties"));
        bioportalConfig.setApiKey(props.getProperty("bioportal.api.key"));
        bioportalAdapter.setConfig(bioportalConfig); // defaults
	}

	/**
	 * Parses the UniMed file and stores in the mega-map the xrefs from UniProt
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
        		parser.parse(cl.getOptionValue("file"));
        		mm.commit();
    		} catch (Exception e){
    			if (mm != null) mm.rollback();
    		} finally {
                if (mm != null) mm.closeMap();
    			if (con != null) con.close();
        	}
        }
	}

	public void parse(String file) throws Exception {
		// Check the extension of the file:
		Format format = Format.valueOf(file.substring(file.lastIndexOf('.')+1));
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {
			mm.openMap();
			is = file.startsWith("http://")?
					new URL(file).openStream():
					new FileInputStream(file);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			LOGGER.info("Parsing start");
			String line;
			while ((line = br.readLine()) != null){
				String[] fields = getFields(format, line);
				if (fields == null) continue; // header lines
				
				// Start with the scores, discard immediately if not
				// interesting, otherwise keep for the MeSH terms (see below):
				String[] scoresCell = fields[4].split(" ?/ ?");
				double[] scores = new double[scoresCell.length];
				boolean interesting = false;
				for (int i = 0; i < scoresCell.length; i++) {
					final String scoreString = scoresCell[i].trim();
					if (scoreString.equals("exact")){
						scores[i] = Double.MAX_VALUE;
					} else {
						scores[i] = Double.valueOf(scoreString);
					}
					if (scores[i] > minScore) interesting = true;
				}
				if (!interesting) continue;
				LOGGER.debug("Score over " + minScore);
				
				String uniprotAcc = fields[0];
				Entry uniprotEntry = mm.getEntryForAccession(
						MmDatabase.UniProt, uniprotAcc);
				// XXX: Ignore if not found in the enzymes mega-map:
				if (uniprotEntry == null) continue;
				LOGGER.debug("UniProt accession is an enzyme: " + uniprotAcc);
				
				Collection<XRef> diseaseXrefs = new ArrayList<XRef>();

				String[] omimCell = fields[1].split("\\s");
				addOmimXrefs(uniprotEntry, diseaseXrefs, omimCell);
				
				String[] meshIdsCell = fields[2].split(" ?/ ?");
				String[] meshHeadsCell = fields[3].split(" / ");
				addMeshAndEfoXrefs(uniprotEntry, diseaseXrefs, scores,
						meshIdsCell, meshHeadsCell);
				mm.writeXrefs(diseaseXrefs);
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

	/**
	 * Splits the fields in one line of the file, namely:
	 * <ul>
	 * <li>[0] - UniProt accession</li>
	 * <li>[1] - MIM number(s)</li>
	 * <li>[2] - MeSH ID(s)</li>
	 * <li>[3] - MeSH heading(s)</li>
	 * <li>[4] - Score(s)</li>
	 * </ul>
	 * @param format the {@link Format} of the file.
	 * @param line one line read from the file.
	 * @return the split fields in the line, or <code>null</code> if it is a
	 * 		header line. Note that multi-valued fields must be split further.
	 */
	private String[] getFields(Format format, String line) {
		String[] fields = null;
		switch (format) {
		case html:
			Matcher m = htmlTablePattern.matcher(line);
			// Discard header lines:
			if (!m.matches()) return null;
			fields = new String[5];
			fields[0] = m.group(1).replaceAll("<\\/?a[^>]*>", "");
			fields[1] = m.group(2).replaceAll("<\\/?a[^>]*>", "");
			fields[2] = m.group(3).replaceAll("<\\/?a[^>]*>", "");
			fields[3] = m.group(4);
			fields[4] = m.group(5);
			break;
		case tab:
			// Discard header lines:
			if (line.startsWith("Swiss-Prot")) return null;
			fields = line.split("\t");
			break;
		}
		return fields;
	}

	/**
	 * Adds xrefs from a UniProt entry to the passed MeSH terms if their score
	 * is not less than the {@link #minScore}, and to EFO entries whose disease
	 * name matches exactly the MeSH term.
	 * @param uniprotEntry the UniProt entry referencing MeSH
	 * @param diseaseXrefs the collection to update with any cross-references
	 * 		to MeSH and EFO.
	 * @param scores the scores of MeSH terms for the UniProt accession.
	 * @param meshIds the MeSH identifiers.
	 * @param meshHeads the MeSH headers (terms).
	 */
	private void addMeshAndEfoXrefs(Entry uniprotEntry,
			Collection<XRef> diseaseXrefs, double[] scores,
			String[] meshIds, String[] meshHeads) {
		// Same number of MeSH IDs, headers and scores
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] < minScore) continue;
			Entry meshEntry = new Entry();
			meshEntry.setDbName(MmDatabase.MeSH.name());
			final String meshId = meshIds[i].trim();
			meshEntry.setEntryId(meshId);
			final String meshHead = meshHeads[i].trim();
			meshEntry.setEntryName(meshHead);
			
			XRef meshXref = new XRef();
			meshXref.setFromEntry(uniprotEntry);
			meshXref.setRelationship(Relationship.between(
                    MmDatabase.UniProt, MmDatabase.MeSH).name());
			meshXref.setToEntry(meshEntry);
			diseaseXrefs.add(meshXref);
			LOGGER.debug("MeSH xref to " + meshId);

			// Now look if the MeSH heading matches an EFO entry:
			try {
				Disease efoDisease = (Disease) bioportalAdapter.searchConcept(
						BioportalOntology.EFO,
						meshHead, Disease.class, false);
				if (efoDisease != null){
					Entry efoEntry = new Entry();
					efoEntry.setDbName(MmDatabase.EFO.name());
					efoEntry.setEntryId(efoDisease.getId());
					efoEntry.setEntryName(efoDisease.getName());

					XRef efoXref = new XRef();
					efoXref.setFromEntry(uniprotEntry);
					efoXref.setRelationship(Relationship.between(
							MmDatabase.UniProt, MmDatabase.EFO).name());
					efoXref.setToEntry(efoEntry);
					diseaseXrefs.add(efoXref);
					LOGGER.debug("EFO xref to " + efoDisease.getId());
				}
			} catch (BioportalAdapterException e) {
				// Don't stop the others just because of BioPortal/EFO problems:
				LOGGER.error("Unable to get EFO xref for " + meshHead, e);
			}
		}
	}

	/**
	 * Adds xrefs from a UniProt entry to the passed OMIM identifiers.
	 * @param uniprotEntry the UniProt entry referencing OMIM
	 * @param diseaseXrefs the collection to update with any cross-references
	 * 		to OMIM.
	 * @param omimIds the OMIM identifiers.
	 * @throws BioportalAdapterException
	 */
	private void addOmimXrefs(Entry uniprotEntry,
			Collection<XRef> diseaseXrefs, String[] omimIds)
	throws BioportalAdapterException {
		for (int i = 0; i < omimIds.length; i++) {
			final String omimString = omimIds[i].trim();
			if (omimString.equals("-")) continue;
			Entry omimEntry = new Entry();
			omimEntry.setDbName(MmDatabase.OMIM.name());
			omimEntry.setEntryId(omimString);
			// We need a disease name:
			Disease omimDisease = (Disease) bioportalAdapter.searchConcept(
					BioportalOntology.OMIM, omimString, Disease.class, false);
			if (omimDisease != null){
				omimEntry.setEntryName(omimDisease.getName());
			}
			XRef omimXref = new XRef();
			omimXref.setFromEntry(uniprotEntry);
			omimXref.setRelationship(Relationship.between(
					MmDatabase.UniProt, MmDatabase.OMIM).name());
			omimXref.setToEntry(omimEntry);
			diseaseXrefs.add(omimXref);
			LOGGER.debug("OMIM xref to " + omimString);
		}
	}

	public void setWriter(MegaMapper mm) {
		this.mm = mm;
	}
}
