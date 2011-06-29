package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.param.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.result.Result;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEbeyeAdapter {

//********************************* VARIABLES ********************************//
    public static final int EBEYE_RESULT_LIMIT = 100;
    //The number of accessions to be included in the query should not be more than 400
    public static final int EBEYE_NR_OF_ACC_LIMIT = 400;
    public static final int EP_RESULTS_PER_DOIMAIN_LIMIT = 20;
    public static final int EP_UNIPROT_XREF_RESULT_LIMIT = 5000;
    public static final int EP_THREADS_LIMIT = EP_UNIPROT_XREF_RESULT_LIMIT/EBEYE_RESULT_LIMIT;
    //MILLISECOND
    public static final int EBEYE_ONE_RECORD_TIMEOUT = 10;
    //SECOND
    public static final int EBEYE_ONLINE_REQUEST_TIMEOUT = 60;
    //HOUR
    public static final int EBEYE_CACHE_TIMEOUT = 2;
    public static enum Domains {intenz,uniprot,rhea,reactome,chebi
                                                ,pdbe,chembl_compound, chembl_target,omim,mesh
        };
    //Supported fields
        //TODO change the schema to add UNIPROT array
    public static enum FieldsOfGetResults {
       id, acc,UNIPROT;
       public static List<String> getFields() {
           List<String> fields = new ArrayList<String>();
           fields.add(id.name());
           fields.add(acc.name());
           fields.add(UNIPROT.name());
           return fields;
       }
    };
    
    public static enum FieldsOfGetNames {
        id, name;
       public static List<String> getFields() {
           List<String> fields = new ArrayList<String>();
           fields.add(id.name());
           fields.add(name.name());
           return fields;
       }

    };

/*
    public static enum UniprotResultFields  {
        descSubName,descRecName,organism_scientific_name,status
    };
 *
 */

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public List<Result> getResults(ParamOfGetResults param, boolean transformResultToUniprot)
            throws MultiThreadingException;
    
    /**
     * Query Ebeye service for uniprot {@link Result} of more than one domain.
     * The {@link #getNumberOfResults(java.util.List)} must be invoked to set
     * the totalFound before invoking this method.
     * @param paramOfGetResultsList
     * @return
     * @throws MultiThreadingException
     */
    public Map<String, List<Result>> getUniprotResults(
            List<ParamOfGetResults> paramOfGetResultsList) throws MultiThreadingException;
    
    public List<Result> getResults(ParamOfGetResults param)
            throws MultiThreadingException;

    public Map<String, String> getNames(String domain
            , List<String> ids);

    public List<ParamOfGetResults> getNumberOfResults(
            List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;
    
    public List<Result> getResultsByAccessions(String domain, List<String>
            accessions) throws MultiThreadingException;

    public Map<String,String> getUniprotXrefs(List<String>ids, String xRefDomain);

/*
    public int getNrOfResultsByAccessions(String domain
            , List<String> accessions) throws MultiThreadingException;
 * 
 */
}
