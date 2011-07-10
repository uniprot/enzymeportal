package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
//import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import java.util.Set;
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
    //TODO either use this or domains in the config file
    public static enum Domains {intenz,uniprot,rhea,reactome,chebi,pdbe;
                                                //,pdbe,chembl_compound, chembl_target,omim,mesh
       public static List<String> getFields() {
           List<String> fields = new ArrayList<String>();
           fields.add(intenz.name());
           fields.add(uniprot.name());
           fields.add(rhea.name());
           fields.add(reactome.name());
           fields.add(chebi.name());
           fields.add(pdbe.name());
           return fields;
       }

        };
    public static final String UNIPROT_REF_FIELD="UNIPROT";
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

    public static enum FieldsOfUniprotNameMap {
    id, descRecName;
   public static List<String> getFields() {
       List<String> fields = new ArrayList<String>();
       fields.add(id.name());
       fields.add(descRecName.name());
       return fields;
   }

    };

    public static enum FieldsOfChebiNameMap {
    id, name;
   public static List<String> getFields() {
       List<String> fields = new ArrayList<String>();
       fields.add(id.name());
       fields.add(name.name());
       return fields;
   }

    };
    /*
    public static enum FieldsOfGetNames {
        id, name;
       public static List<String> getFields() {
           List<String> fields = new ArrayList<String>();
           fields.add(id.name());
           fields.add(name.name());
           return fields;
       }

    };
*/
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

    public Collection<String> getUniprotXrefAccessions(List<ParamOfGetResults> params)
            throws MultiThreadingException;

/*
    public Map<String, String> getNames(String domain
            , List<String> ids);
*/

    /**
     * Gets number of results for more than 1 queries. The number of results per
     * query is set to the ParamOfGetResults object.
     * @param paramOfGetResults
     * @return
     * @throws MultiThreadingException
     */
    public List<ParamOfGetResults> getNumberOfResults(
            List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;
    
    public ParamOfGetResults getNumberOfResults(ParamOfGetResults paramOfGetResults);

    public Set<String> getRelatedUniprotAccessionSet(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;

    public Set<String> getValueOfFields(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;

    public Set<String> getValueOfFields(ParamOfGetResults paramOfGetResults)  throws MultiThreadingException;
    

    public Map<String,String> getUniprotXrefIds(List<String>ids, String xRefDomain);

    public Map<String,Map<String,String>> getUniprotXrefIdAndName(List<String> ids, String xRefDomain);

/*
    public int getNrOfResultsByAccessions(String domain
            , List<String> accessions) throws MultiThreadingException;
 * 
 */


    //TESTING new interface
    //public Collection<String> getUniprotXrefAccessions(String query);


    public Map<String, String> getNameMapByAccessions(
            String domain, Collection<String> accessions);

    public Collection<String> getNameSetByAccessions(String domain, Collection<String> accessions);

    public Collection<String> getNameSetByAccessions(String domain, Collection<String> accessions, int from, int size);

    public int getNrOfResultsOfGetNameSetByAccessions(String domain, Collection<String> accessions);
    
}
