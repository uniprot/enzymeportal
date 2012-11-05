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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private UniprotConfig config;

    /**
     * Constructor.
     * @param accOrId a UniProt accession number or ID.
     * @param idType the type of identifier (accession or entry name)
     * @param field the field we are interested in.
     * @param defaultSpecies the species to show by default (only used when the
     * 		web service returns more than one entries).
     * @param config Configuration for UniProt.
     */
    public UniprotWsSummaryCallable(String accOrId, IdType idType, Field field,
            String defaultSpecies, UniprotConfig config) {
        this.accOrId = accOrId;
        this.idType = idType;
        this.field = field;
        this.defaultSpecies = defaultSpecies;
        this.config = config;
    }

    public EnzymeSummary call() throws Exception {
        return getEnzymeSummary();
    }

    protected EnzymeSummary getEnzymeSummary() throws UniprotWsException {
        List<String> enzymeInfo = getEnzymeInfo(accOrId, idType, getColumns());
        if (enzymeInfo == null) {
            return null;
        }
        if (enzymeInfo.size() > 1 && idType.equals(IdType.ACCESSION)) {
            // More than one entry for one accession? It has been demerged?
            LOGGER.warn("More than one entry for " + accOrId);
        }

        String accession = null, id = null,
                nameSynonymsString = null, ecsString = null,
                diseasesString = null, sequence = null, functionString = null,
                pdbeAccessions = null, pdbMethods = null, rpString = null, drugsString = null,
                regulString = null;
        Species species = null;
        List<EnzymeAccession> relSpecies = new LinkedList<EnzymeAccession>();



        int bestSpecies = 0; // the best choice to show
        String[] colValues = null;
        for (int i = 0; i < enzymeInfo.size(); i++) {
            colValues = enzymeInfo.get(i).split("\t", -1);

            // some entries don't return a species, ex. Q5D707
            if (colValues[4].length() == 0) {
                continue;
            }
            //EnzymeAccessionWrapper enzymeAccessionWrapper = new EnzymeAccessionWrapper(new EnzymeAccession());
           // EnzymeAccession ea = enzymeAccessionWrapper.getAccession(); 
             EnzymeAccession ea = new EnzymeAccession();
            // organism is always [4], accession is [0], see getColumns()
            ea.setSpecies(UniprotWsAdapter.parseSpecies(colValues[4]));
            ea.getUniprotaccessions().add(colValues[0]);
            List<String> pdbCodes = null;
            if (field.equals(Field.brief) || field.equals(Field.enzyme)) {
                // We already have PDB codes in enzymeInfo as [6], see getColumns()
                pdbCodes = parsePdbCodes(colValues[6], colValues[7]);
            }
            ea.setPdbeaccession(pdbCodes);

            final boolean isDefSp = ea.getSpecies().getScientificname().equalsIgnoreCase(defaultSpecies);
            final boolean noDefSp = relSpecies.isEmpty()
                    || !relSpecies.get(0).getSpecies().getScientificname().equalsIgnoreCase(defaultSpecies);

            if (isDefSp || (noDefSp && pdbCodes != null)) {
                relSpecies.add(0, ea);
                bestSpecies = i;
            } else {
                relSpecies.add(ea);
            }

            //relSpecies.add(ea);

        }

        species = relSpecies.get(0).getSpecies();

        // Take the default species or one with structures.
        colValues = enzymeInfo.get(bestSpecies).split("\t", -1);
        accession = colValues[0];
        id = colValues[1];
        nameSynonymsString = colValues[2];
        ecsString = colValues[3];
        switch (field) {
            case enzyme:
                diseasesString = colValues[8];
                sequence = colValues[9];
            case brief:
                functionString = colValues[5];
                pdbeAccessions = colValues[6];
                pdbMethods = colValues[7];
                break;
            case proteinStructure:
                pdbeAccessions = colValues[5];
                pdbMethods = colValues[6];
                break;
            case reactionsPathways:
                rpString = colValues[5];
                break;
            case molecules:
                drugsString = colValues[5];
                regulString = colValues[6];
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
        if (idType.equals(IdType.ACCESSION)) {
            // We'll have to make one more request to get other species:
            String uniprotIdPrefix = id.split(IUniprotAdapter.ID_SPLIT_SYMBOL)[0];
            String defSp = species.getScientificname();
            relSpecies = getRelatedSpecies(uniprotIdPrefix, defSp);
        }

        return buildSummary(accession, id, nameSynonymsString, ecsString,
                species, diseasesString, sequence, functionString,
                pdbeAccessions, pdbMethods, rpString, drugsString, regulString, relSpecies);
    }

    /**
     * Selects the columns to retrieve from the web service according to the
     * field.
     * @return
     */
    private String getColumns() {
        String columns = "id,entry+name,protein+names,ec,organism";
        switch (field) {
            case brief:
                columns += ",comment(FUNCTION),database(PDB),3d";
                break;
            case enzyme:
                columns += ",comment(FUNCTION),database(PDB),3d,comment(DISEASE),length";
                break;
            case proteinStructure:
                columns += ",database(PDB),3d";
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
     * @param accession
     * @param id
     * @param nameSynonymsString
     * @param ecsString
     * @param species
     * @param diseasesString
     * @param sequence
     * @param functionString
     * @param pdbAccessions
     * @param pdbMethods
     * @param rpString
     * @param drugsString
     * @param regulString
     * @param relSpecies
     * @return
     */
    private EnzymeModel buildSummary(String accession, String id,
            String nameSynonymsString, String ecsString, Species species,
            String diseasesString, String sequence, String functionString,
            String pdbAccessions, String pdbMethods, String rpString,
            String drugsString, String regulString, List<EnzymeAccession> relSpecies) {
        EnzymeModel summary = new EnzymeModel();
        summary.getUniprotaccessions().add(accession);
        summary.setUniprotid(id);
        final List<String> nameSynonyms = parseNameSynonyms(nameSynonymsString);
        summary.setName(nameSynonyms.get(0));
        if (nameSynonyms.size() > 1) {
            summary.getSynonym().addAll(nameSynonyms.subList(0, nameSynonyms.size() - 1));
        }
        if (ecsString != null && ecsString.length() > 0) {
            summary.getEc().addAll(Arrays.asList(ecsString.split("; ")));
        }
        summary.setSpecies(species);
        summary.setRelatedspecies(relSpecies);
        // Optional fields:
        if (diseasesString != null && diseasesString.length() > 0) {
            summary.setDisease(parseDiseases(diseasesString));
        }
        if (sequence != null) {
            Sequence seq = new Sequence();
            seq.setSequence(sequence);
            // TODO seq.setWeight(value);
            seq.setSequenceurl(IUniprotAdapter.SEQUENCE_URL_BASE + accession
                    + IUniprotAdapter.SEQUENCE_URL_SUFFIX);
            Enzyme enzyme = new Enzyme();
            enzyme.setSequence(seq);
            summary.setEnzyme(enzyme);
        }
        if (functionString != null && functionString.length() > 0) {
            summary.setFunction(parseFunction(functionString));
        }
        if (pdbAccessions != null) {
            summary.setPdbeaccession(parsePdbCodes(pdbAccessions, pdbMethods));
        }
        if (rpString != null) {
            summary.getReactionpathway().add(parseReactionPathways(rpString));
        }
        if (drugsString != null || regulString != null) {
            summary.setMolecule(parseChemicalEntity(drugsString, regulString));
        }
        return summary;
    }

    /**
     * Retrieves the protein recommended name as well as any synonyms.
     * @param namesColumn the column returned by the web service
     * @return a list of names, the first one of them being the recommended one.
     */
    private List<String> parseNameSynonyms(String namesColumn) {
        List<String> nameSynonyms = new ArrayList<String>();
        final int sepIndex = namesColumn.indexOf(" (");
        if (sepIndex == -1) {
            // no synonyms, just recommended name:
            nameSynonyms.add(namesColumn);
        } else {
            // Recommended name:
            nameSynonyms.add(namesColumn.substring(0, sepIndex));
            // take out starting and ending parentheses
            String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
            for (int i = 0; i < synonyms.length; i++) {
                nameSynonyms.add(synonyms[i]);
            }
        }
        return nameSynonyms;
    }

    private String parseFunction(String functionColumn) {
        return functionColumn.replaceAll("; FUNCTION: ", "\n").replaceAll("FUNCTION: ", "");
    }

    /**
     * Parses the strings returned from the UniProt web service for the PDB
     * codes, discarding any theoretical models for which there is no image
     * available from PDBe website.
     * @param pdbAccessions the PDB accessions. <i>Note that we are assuming
     * 		that any theoretical models come always first.</i>
     * @param pdbMethods the methods used to resolve the structures.
     * @return a list of Strings (non-theoretical PDB codes), or
     * 		<code>null</code> if none is returned by the web service or if all
     * 		of them are theoretical models.
     */
    protected List<String> parsePdbCodes(String pdbAccessions, String pdbMethods) {
    	List<String> result = null;
    	if (pdbAccessions != null && pdbAccessions.length() > 0
    			&& pdbMethods != null){
        	Pattern p = Pattern.compile(".*Model \\((\\d+)\\).*");
        	Matcher m = p.matcher(pdbMethods);
        	int theorNum = 0;
        	if (m.matches()){
        		theorNum = Integer.parseInt(m.group(1));
        	}
            final String[] split = pdbAccessions.toLowerCase().split(";");
            if (split.length > theorNum){
            	// Get rid of the theoretical models, which come first:
            	result = Arrays.asList(split).subList(theorNum, split.length);
            }
    	}
    	return result;
    }

    private List<Disease> parseDiseases(String diseaseColumn) {
        String ds = diseaseColumn.replaceAll("^DISEASE: ", "");
        List<Disease> diseases = new ArrayList<Disease>();
        for (String s : ds.split("; DISEASE: ")) {
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
     * @return a ReactionPathway object containing one or more Pathways,
     * 		or <code>null</code> if none found.
     */
    private ReactionPathway parseReactionPathways(String reactomeIds) {
        ReactionPathway reactionPathway = null;
        for (String reactomeId : reactomeIds.split(";")) {
            if (reactionPathway == null) {
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
        if (drugsCol.length() > 0) {
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
        if (regulCol.length() > 0) {
            String[] sentences = regulCol.replace("ENZYME REGULATION: ", "").split("\\.");
            for (String sentence : sentences) {
                if (sentence.matches(".*" + Transformer.ACTIVATOR_REGEX + ".*")) {
                    List<Molecule> activators =
                            Transformer.parseTextForActivators(sentence.trim());
                    chemicalEntity.getActivators().addAll(activators);
                }
                if (sentence.matches(".*" + Transformer.INHIBITOR_REGEX + ".*")) {
                    List<Molecule> inhibitors =
                            Transformer.parseTextForInhibitors(sentence.trim());
                    chemicalEntity.getInhibitors().addAll(inhibitors);
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
     * 		in the same order.
     * @throws UniprotWsException if some error happens 
     */
    private List<String> getEnzymeInfo(String accOrId, IdType idType, String columns)
            throws UniprotWsException {
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
        String url = MessageFormat.format(config.getWsUrl(),
                query, columns);
//		LOGGER.debug(url);
        BufferedReader br = null;
        InputStreamReader isr = null;
        InputStream is = null;
        try {
            final URL theUrl = new URL(url);
            URLConnection con = config.getUseProxy()
                    ? theUrl.openConnection()
                    : theUrl.openConnection(Proxy.NO_PROXY);
            con.setReadTimeout(config.getTimeout());
            con.connect();
            is = con.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (enzymeInfo == null) {
                    enzymeInfo = new ArrayList<String>();
                    // first line is a header
                    continue;
                }
                enzymeInfo.add(line);
            }
        } catch (MalformedURLException e) {
            throw new UniprotWsException("Bad URL: " + url, e);
        } catch (IOException e) {
            throw new UniprotWsException("Could not open connection to " + url, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close stream", e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close reader", e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close reader", e);
                }
            }
        }
        if (enzymeInfo == null) {
            throw new UniprotWsException("No enzyme info retrieved for " + accOrId);
        }
        return enzymeInfo;
    }

    /**
     * Parses multiple lines returned by the web service in tab format,
     * getting the whole list of species with the default one (if found)
     * at the top of it.
     * @param enzymeInfo lines returned by the web service.
     * @return a list of EnzymeAccession objects corresponding to all known
     * 		species, with the default one (if found) at the top.
     */
    private List<EnzymeAccession> parseRelatedSpecies(List<String> enzymeInfo) {
        List<EnzymeAccession> enzymeAccessions = null;
        // EnzymeAccession is automatically generated, no hashcode or equals methods:
        Map<String, EnzymeAccession> relSps = new HashMap<String, EnzymeAccession>();
        for (String line : enzymeInfo) {
            String[] split = line.split("\t", -1);
            // some entries don't return a species, ex. Q5D707
            if (split[4].length() > 0) {
                List<String> pdbCodes = null;
                if (field.equals(Field.brief) || field.equals(Field.enzyme)) {
                    // we already have PDB codes in enzymeInfo as [6], see getColumns()
                    pdbCodes = parsePdbCodes(split[6], split[7]);
                }
                // organism is always [4], accession is [0], see getColumns()
                Species sp = UniprotWsAdapter.parseSpecies(split[4]);
                if (relSps.containsKey(sp.getScientificname()) && pdbCodes != null) {
                    // Just add any new PDB codes:
                    relSps.get(sp.getScientificname()).getPdbeaccession().addAll(pdbCodes);
                } else {
                    // Create a new one:
                    EnzymeAccession ea = new EnzymeAccession();
                    ea.setSpecies(sp);
                    ea.getUniprotaccessions().add(split[0]);
                    ea.setPdbeaccession(pdbCodes);
                    relSps.put(sp.getScientificname(), ea);
                }
            }
        }
        if (!relSps.isEmpty()) {
            enzymeAccessions = new ArrayList<EnzymeAccession>(relSps.values());
            if (relSps.containsKey(defaultSpecies)) {
                // Put it at the top:
                enzymeAccessions.remove(relSps.get(defaultSpecies));
                enzymeAccessions.add(0, relSps.get(defaultSpecies));
            }
        }
        return enzymeAccessions;
    }

    /**
     * Retrieves all known species related to a given UniProt ID prefix.
     * @param uniprotIdPrefix the UniProt ID prefix.
     * @param defaultSpecies the default species to show, if any (can be null).
     * @return a list of objects containing information about UniProt
     * 		accessions and PDB codes, one object per species.
     * @throws UniprotWsException
     */
    private List<EnzymeAccession> getRelatedSpecies(String uniprotIdPrefix,
            String defaultSpecies) throws UniprotWsException {
        List<EnzymeAccession> enzymeAccessions = null;
        String fields = "id,organism,database(PDB),3d";
        List<String> enzymeInfo =
                getEnzymeInfo(uniprotIdPrefix + "_*", IdType.ENTRY_NAME, fields);
        for (String s : enzymeInfo) {
            EnzymeAccession ea = new EnzymeAccession();
            String[] split = s.split("\t", -1);
            if (split[1].length() == 0) {
                continue; // no organism info
            }
            ea.getUniprotaccessions().add(split[0]);
            ea.setSpecies(UniprotWsAdapter.parseSpecies(split[1]));
            if (split[2].length() > 0) {
                ea.setPdbeaccession(parsePdbCodes(split[2], split[3]));
            }
            if (enzymeAccessions == null) {
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
