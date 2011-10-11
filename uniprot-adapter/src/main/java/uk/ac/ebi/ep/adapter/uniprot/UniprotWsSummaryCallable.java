package uk.ac.ebi.ep.adapter.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.enzyme.model.Sequence;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;

/**
 * Callable to get enzyme summaries from UniProt web services given an
 * accession number or entry name (ID).
 * @author rafa
 *
 */
public class UniprotWsSummaryCallable implements Callable<EnzymeSummary> {

	private static final Logger LOGGER = Logger.getLogger(UniprotWsSummaryCallable.class);
	
	/** Type of identifier used to retrieve the summary. */
	protected enum IdType {
		/** Accession number. */
		ACCESSION,
		/** Entry name (ID) */
		ENTRY_NAME
	}
	
	private String accOrId;
	
	private IdType idType;
	
	private Field field;
	
	private String defaultSpecies;
	
	private int timeout;
	
	/**
	 * Constructor.
	 * @param accOrId a UniProt accession number or ID.
	 * @param idType the type of identifier (accession or entry name)
	 * @param field the field we are interested in.
	 * @param defaultSpecies the species to show by default (only used when the
	 * 		web service returns more than one entries).
	 * @param timeout timeout (ms) for the web service request.
	 */
	public UniprotWsSummaryCallable(String accOrId, IdType idType, Field field,
			String defaultSpecies, int timeout){
		this.accOrId = accOrId;
		this.idType = idType;
		this.field = field;
		this.defaultSpecies = defaultSpecies;
		this.timeout = timeout;
	}

	public EnzymeSummary call() throws Exception {
		return getEnzymeSummary();
	}
	
	protected EnzymeSummary getEnzymeSummary(){
		List<String> enzymeInfo = getEnzymeInfo(accOrId, idType, getColumns());
		// Take only first one, see below about the others:
		String[] colValues = enzymeInfo.iterator().next().split("\t", -1);
		// Parse common properties:
		EnzymeModel summary = buildSummary(colValues);
		summary.setRelatedspecies(parseRelatedSpecies(enzymeInfo));
		if (enzymeInfo.size() > 1 && idType.equals(IdType.ACCESSION)){
			// More than one entry for one accession? It has been demerged?
			LOGGER.warn("More than one entry for " + accOrId);
		}
		
		// Populate the summary according to the field:
		switch (field) {
		case enzyme:
			if (colValues[5].length() > 0){
				summary.setFunction(parseFunction(colValues[5]));
			}
			if (colValues[6].length() > 0){
				summary.setDisease(parseDiseases(colValues[6]));
			}
			if (colValues[7].length() > 0){
				summary.setPdbeaccession(Arrays.asList(colValues[7].split(";")));
			}
			Sequence seq = new Sequence();
			seq.setSequence(String.valueOf(colValues[8].length()));
			// TODO seq.setWeight(value);
	        seq.setSequenceurl(IUniprotAdapter.SEQUENCE_URL_BASE + accOrId
	        		+ IUniprotAdapter.SEQUENCE_URL_SUFFIX);
			Enzyme enzyme = new Enzyme();
			enzyme.setSequence(seq);
			summary.setEnzyme(enzyme);
			
	        String uniprotIdPrefix = summary.getUniprotid()
	        		.split(IUniprotAdapter.ID_SPLIT_SYMBOL)[0];
	        String defSp = idType.equals(IdType.ACCESSION)?
	        		summary.getSpecies().getScientificname():
	        		defaultSpecies;
			summary.setRelatedspecies(getSpecies(uniprotIdPrefix, defSp));
			break;
		case proteinStructure:
			if (colValues[5].length() > 0){
				summary.getPdbeaccession().addAll(
						Arrays.asList(colValues[5].toLowerCase().split(";")));
			}
			break;
		case reactionsPathways:
			if (colValues[5].length() > 0){
				summary.getReactionpathway().add(parseReactionPathways(colValues[5]));
			}
			break;
		case molecules:
			summary.setMolecule(parseChemicalEntity(colValues[5], colValues[5]));
			break;
		case diseaseDrugs:
			// TODO
			break;
		case literature:
			// TODO
			break;
		case full:
			// TODO
			break;
		}
		return summary;
	}

	/**
	 * Parses multiple lines returned by the web service in tab format,
	 * getting the whole list of species with the default one (if found)
	 * at the top of it.
	 * @param enzymeInfo lines returned by the web service.
	 * @return a list of Species with the default one (if found) at the top..
	 */
	private List<EnzymeAccession> parseRelatedSpecies(List<String> enzymeInfo) {
		List<EnzymeAccession> relatedSpecies = null;
		Map<String, String> spAcc = new HashMap<String, String>();
		for (String line : enzymeInfo) {
			String[] split = line.split("\t", -1);
			// organism is always [4], accession is [0], see getColumns()
			spAcc.put(split[4], split[0]);
		}
		if (!spAcc.isEmpty()){
			relatedSpecies = new ArrayList<EnzymeAccession>();
			for (String sp : spAcc.keySet()) {
				EnzymeAccession ea = new EnzymeAccession();
				ea.getUniprotaccessions().add(spAcc.get(sp));
				Species relSp = parseSpecies(sp);
				ea.setSpecies(relSp);
				if (relSp.getScientificname().equalsIgnoreCase(defaultSpecies)){
					relatedSpecies.add(0, ea);
				} else {
					relatedSpecies.add(ea);
				}
			}
		}
		return relatedSpecies;
	}

	/**
	 * Selects the columns to retrieve from the web service according to the
	 * field.
	 * @return
	 */
	private String getColumns() {
		String columns = "id,entry+name,protein+names,ec,organism";
		switch (field) {
		case enzyme:
			columns += ",comment(FUNCTION),comment(DISEASE),database(PDB),sequence";
			break;
		case proteinStructure:
			columns += ",database(PDB)";
			break;
		case reactionsPathways:
			columns += ",database(REACTOME)";
			break;
		case molecules:
			columns += ",database(DRUGBANK),comment(ENZYME_REGULATION)";
			break;
		case diseaseDrugs:
			// TODO
			break;
		case literature:
			// TODO
			break;
		case full:
			// TODO
			break;
		}
		return columns;
	}

	/**
	 * Builds an EnzymeModel and populates it with basic information (enough
	 * for an EnzymeSummary).
	 * @param accession the UniProt accession number.
	 * @param columns fields returned from the web service. There might be more,
	 * 		but these first ones are required in this order:
	 * <ol>
	 * 	<li>accession</li>
	 * 	<li>entry name (id)</li>
	 * 	<li>protein name(s)</li>
	 * 	<li>ec(s)</li>
	 * 	<li>species</li>
	 * </ol>
	 * @return
	 */
	private EnzymeModel buildSummary(String[] columns) {
		final String accession = columns[0];
		final String id = columns[1];
		final List<String> nameSynonyms = parseNameSynonyms(columns[2]);
		final List<String> ecs = Arrays.asList(columns[3].split("; "));
		final Species species = parseSpecies(columns[4]);
		EnzymeModel summary = new EnzymeModel();
		summary.getUniprotaccessions().add(accession);
		summary.setUniprotid(id);
		summary.setName(nameSynonyms.get(0));
		if (nameSynonyms.size() > 1){
			summary.getSynonym().addAll(nameSynonyms.subList(0, nameSynonyms.size()-1));
		}
		summary.getEc().addAll(ecs);
		summary.setSpecies(species);
		return summary;
	}
	
	/**
	 * Retrieves the protein recommended name as well as any synonyms.
	 * @param namesColumn the column returned by the web service
	 * @return a list of names, the first one of them being the recommended one.
	 */
	private List<String> parseNameSynonyms(String namesColumn){
		List<String> nameSynonyms = new ArrayList<String>();
		final int sepIndex = namesColumn.indexOf(" (");
		if (sepIndex == -1){
			// no synonyms, just recommended name:
			nameSynonyms.add(namesColumn);
		} else {
			// Recommended name:
			nameSynonyms.add(namesColumn.substring(0, sepIndex));
			// take out starting and ending parentheses
			String[] synonyms = namesColumn
					.substring(sepIndex+2, namesColumn.length()-1)
					.split("\\) \\(");
			for (int i = 0; i < synonyms.length; i++){
				nameSynonyms.add(synonyms[i]);
			}
		}
		return nameSynonyms;
	}

	private String parseFunction(String functionColumn){
		return functionColumn.replaceAll("; FUNCTION: ", "\n")
				.replaceAll("FUNCTION: ", "");
	}

	private Species parseSpecies(String speciesTxt){
		Species species = null;
		Matcher m = UniprotWsAdapter.speciesPattern.matcher(speciesTxt);
		if (m.matches()){
	        species = new Species();
	        species.setScientificname(m.group(1));
	        species.setCommonname(m.group(2));
		}
		return species;
	}

	private List<Disease> parseDiseases(String diseaseColumn){
		diseaseColumn.replaceAll("^DISEASE: ", "");
		List<Disease> diseases = new ArrayList<Disease>();
		for (String s : diseaseColumn.split("; DISEASE: ")){
			Disease disease = new Disease();
			disease.setDescription(s);
			// TODO: disease ID
			diseases.add(disease);
		}
		return diseases;
	}

	/**
	 * Parses Reactome IDs.
	 * @param reactomeIds Reactome IDs returned by the UniProt web service.
	 * @return a ReactionPathway object, or <code>null</code> if none found.
	 */
	private ReactionPathway parseReactionPathways(String reactomeIds) {
		ReactionPathway reactionPathway = null;
		for (String reactomeId : reactomeIds.split(";")){
			if (reactionPathway == null){
				reactionPathway = new ReactionPathway();
			}
			Pathway pathway = new Pathway();
			pathway.setId(reactomeId);
			// TODO pathway.setName(description);
			reactionPathway.getPathways().add(pathway);
		}
		return reactionPathway;
	}
	/**
	 * Parses chemical entities from the fields (columns) returned by the web
	 * service.
	 * @param drugsCol drugs column
	 * @param regulCol enzyme regulation comments
	 * @return
	 */
	private ChemicalEntity parseChemicalEntity(String drugsCol, String regulCol) {
		ChemicalEntity chemicalEntity = new ChemicalEntity();
		if (drugsCol.length() > 0){
			String[] drugbankIds = drugsCol.split(";");
			List<Molecule> drugs = new ArrayList<Molecule>();
			for (int i = 0; i < drugbankIds.length; i++) {
				Molecule drug = new Molecule();
				drug.setId(drugbankIds[i]);
				// TODO molecule.setName(name);
				drugs.add(drug);
			}
			chemicalEntity.setDrugs(drugs);
		}
		if (regulCol.length() > 0){
			String[] sentences = regulCol.replace("ENZYME REGULATION: ", "").split("\\.");
			for (String sentence : sentences) {
				if (sentence.contains("Activated by") || sentence.contains("activated by")){
	                List<Molecule> activators =
	                		Transformer.parseTextForActivators(sentence.trim());
	                chemicalEntity.setActivators(activators);
	            }
	            if (sentence.contains("Inhibited by") || sentence.contains("inhibited by")) {
	                List<Molecule> inhibitors =
	                		Transformer.parseTextForInhibitors(sentence.trim());
	                chemicalEntity.setInhibitors(inhibitors);
	            }
			}
		}
		return chemicalEntity;
	}

	/**
	 * Retrieves data from UniProt for a given accession.
	 * @param accOrId the query to UniProt (an accession number or ID).
	 * @param idType 
	 * @param columns columns to retrieve.
	 * @return a list of tab-delimited Strings containing the requested fields
	 * 		in the same order, or <code>null</code> if some error happened.
	 */
	private List<String> getEnzymeInfo(String accOrId, IdType idType, String columns) {
		List<String> enzymeInfo = null;
		String query = null;
		switch (idType) {
		case ACCESSION:
			query = "accession:";
			break;
		case ENTRY_NAME:
			query = "mnemonic:";
			break;
		}
		query += accOrId;
		String url = MessageFormat.format(UniprotWsAdapter.UNIPROT_WS_URL,
				query, columns);
		LOGGER.debug(url);
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {
			URLConnection con = new URL(url).openConnection(Proxy.NO_PROXY);
			con.setReadTimeout(timeout);
			con.connect();
			is = con.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				if (enzymeInfo == null){
					enzymeInfo = new ArrayList<String>();
					// first line is a header
					continue;
				}
				enzymeInfo.add(line);
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Bad URL: " + url, e);
		} catch (IOException e) {
			LOGGER.error("Could not open connection to " + url, e);
		} finally {
			if (is != null) try {
				is.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close stream", e);
			}
			if (isr != null) try {
				isr.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close reader", e);
			}
			if (br != null) try {
				br.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close reader", e);
			}
		}
		return enzymeInfo;
	}

	private List<EnzymeAccession> getSpecies(String uniprotIdPrefix,
			String defaultSpecies) {
		List<EnzymeAccession> enzymeAccessions = null;
		String fields = "id,organism";
		List<String> enzymeInfo =
				getEnzymeInfo(uniprotIdPrefix + "*", IdType.ENTRY_NAME, fields);
		for (String s : enzymeInfo) {
			EnzymeAccession ea = new EnzymeAccession();
			String[] split = s.split("\t");
			ea.getUniprotaccessions().add(split[0]);
			ea.setSpecies(parseSpecies(split[1]));
			if (enzymeAccessions == null){
				enzymeAccessions = new ArrayList<EnzymeAccession>();
			}
            if (ea.getSpecies().getScientificname().equalsIgnoreCase(defaultSpecies)) {
                enzymeAccessions.add(0, ea);
            } else {
    			enzymeAccessions.add(ea);
            }
		}
		return enzymeAccessions;
	}

}
