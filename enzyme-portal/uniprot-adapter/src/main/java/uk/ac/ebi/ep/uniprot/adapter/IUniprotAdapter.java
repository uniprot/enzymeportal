package uk.ac.ebi.ep.uniprot.adapter;

import java.util.List;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IUniprotAdapter {

//********************************* VARIABLES ********************************//
    //second
    public static final int ENTRY_TIMEOUT = 60;

    //Limited species list
    public static final int SPECIES_BRIEF_MAX_SIZE = 10;
    public static final String ACCESSION_FIELD = "accession";
    public static final String ID_FIELD = "id";
    public static final String SEQUENCE_URL_BASE = "http://www.uniprot.org/uniprot/";
    public static final String SEQUENCE_URL_SUFFIX = ".html#section_seq";
    public static final String ID_SPLIT_SYMBOL = "_";

    public static final String DEFAULT_SPECIES = "Homo sapiens";


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public EnzymeSummary getEnzymeSummary(String accession);

    public EnzymeSummary getReactionPathwaySummary(String accession);

    public EnzymeSummary getMoleculeSummary(String accession);

    public EnzymeSummary getProteinStructureSummary(String accession);

    //public EnzymeSummary getDrugSummary(String accession);
    
    public List<EnzymeSummary> queryEnzymeByIdPrefixes(List<String> queries, String defaultSpecies) throws
            MultiThreadingException;    
}


