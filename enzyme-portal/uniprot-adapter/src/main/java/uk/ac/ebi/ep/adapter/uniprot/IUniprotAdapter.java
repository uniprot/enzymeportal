package uk.ac.ebi.ep.adapter.uniprot;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;

/**
 * Proxy to get enzyme data from UniProt.
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IUniprotAdapter {

	public static final String ACCESSION_FIELD = "accession";
    
    /** id field in EB-Eye, that is the UniProt accession! */
    public static final String ID_FIELD = "id";
    
    public static final String SEQUENCE_URL_BASE = "http://www.uniprot.org/uniprot/";
    public static final String SEQUENCE_URL_SUFFIX = ".html#section_seq";
    public static final String ID_SPLIT_SYMBOL = "_";

    public static final String DEFAULT_SPECIES = "Homo sapiens";
    // public static final String DEFAULT_SPECIES = "none";
    
    /**
     * @return the configuration for this UniProt proxy.
     */
    public UniprotConfig getConfig();
    
    /**
     * Sets the configuration for this proxy.
     * @param config
     */
    public void setConfig(UniprotConfig config);

    /**
     * Sets the data source for the mega-map to fix cross-references (ex. PDB
     * entries for theoretical models which come from UniProt).
     */
    void setMmDatasource(String mmDatasource);

    /**
     * Gets a basic summary of one entry by UniProt accession.
     * @param accession a UniProt accession.
     * @return an enzyme summary.
     * @throws UniprotWsException 
     */
    public EnzymeSummary getEnzymeSummary(String accession)
	throws UniprotWsException;

    /**
     * Gets a summary of one entry, including information about reactions and
     * pathways.
     * @param accession a UniProt accession.
     * @return an enzyme summary.
     * @throws UniprotWsException 
     */
    public EnzymeSummary getEnzymeSummaryWithReactionPathway(String accession)
	throws UniprotWsException;

    /**
     * Gets a summary of one entry, including information about small molecules
     * (drugs cross referenced in DrugBank, inhibitors and activators).
     * @param accession a UniProt accession.
     * @return an enzyme summary.
     * @throws UniprotWsException 
     */
    public EnzymeSummary getEnzymeSummaryWithMolecules(String accession)
	throws UniprotWsException;

    /**
     * Gets a summary of one entry, including information about structure.
     * @param accession a UniProt accession.
     * @return an enzyme summary.
     * @throws UniprotWsException 
     */
    public EnzymeSummary getEnzymeSummaryWithProteinStructure(String accession)
	throws UniprotWsException;

    //public EnzymeSummary getDrugSummary(String accession);
    
    /**
     * Gets summaries of enzymes by their IDs.
     * @param queries a list of UniProt ID prefixes (i.e. without the species
     * 		suffix).
     * @param defaultSpecies the species to use as default.
     * @param speciesFilter the species used as filter.
     * @return a list of enzyme summaries.
     * @throws MultiThreadingException
     */
    public List<EnzymeSummary> getEnzymesByIdPrefixes(List<String> queries,
    		String defaultSpecies, Collection<String> speciesFilter)
	throws MultiThreadingException;

    /**
     * Gets the UniProt IDs (entry names) returned by a simple text query.
     * @param query the query to UniProt.
     * @return a list of UniProt IDs (entry names).
     */
	public List<String> getUniprotIds(String query);

	/**
	 * Gets the UniProt accessions returned by a simple text query.
	 * @param query the query to UniProt.
	 * @return A list of UniProt accessions.
	 */
	public List<String> getUniprotAccessions(String query);

    /**
     * Gets the UniProt IDs (entry names) along with their corresponding
     * species.
     * @param accessions the UniProt accessions.
     * @return a map of UniProt IDs (entry names) to species (one to one).
     */
	public Map<String,Species> getIdsAndSpecies(Collection<String> accessions);
	
	/**
	 * Gets all known species (homologs) corresponding to the given ID prefixes.
	 * @param idPrefixes the prefixes of UniProt IDs.
	 * @return a collection of Species.
	 */
	public Collection<Species> getSpecies(Collection<String> idPrefixes);
}


