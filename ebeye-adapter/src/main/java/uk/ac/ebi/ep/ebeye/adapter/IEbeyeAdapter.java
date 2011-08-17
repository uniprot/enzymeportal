package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
//import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import java.util.Set;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
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
    //public static final int QRY_WITH_UNIPROT_FIELD_RESULT_LIMIT = 10;
    //The number of accessions to be included in the query should not be more than 400
    public static final int EBEYE_NR_OF_QUERY_IN_LIMIT = 100;
    public static final int EP_RESULTS_PER_DOIMAIN_LIMIT = 2;
    //TEST
    public static final int EP_CHEBI_RESULTS_LIMIT = 15;
    //public static final int EP_CHEBI_RESULTS_LIMIT = 2;
    public static final int QUERY_ENZYME_DOMAIN_RESULT_LIMIT = 600;
    public static final int QUERY_UNIPROT_FIELD_RESULT_LIMIT = 100;
    public static final int JOINT_QUERY_UNIPROT_FIELD_RESULT_LIMIT = 2000;
    public static final int QUERY_UNIPROT_FIELD_ALL_DOMAINS_RESULT_LIMITE = 5000;
    public static final int EP_THREADS_LIMIT = QUERY_UNIPROT_FIELD_ALL_DOMAINS_RESULT_LIMITE/EBEYE_RESULT_LIMIT;
    //MILLISECOND
    public static final int EBEYE_ONE_RECORD_TIMEOUT = 10;
    //SECOND
    public static final int EBEYE_ONLINE_REQUEST_TIMEOUT = 120;
    //HOUR
    public static final int EBEYE_CACHE_TIMEOUT = 2;
    
    public static final String EBEYE_SPECIES_FIELD ="organism_scientific_name";
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

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

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

    public List<String> getRelatedUniprotAccessionSet(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;

    public Set<String> getValueOfFields(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException;

    public Set<String> getValueOfFields(ParamOfGetResults paramOfGetResults)  throws MultiThreadingException;

    public Map<String, String> getMapOfFieldAndValue(List<ParamOfGetResults> params) throws MultiThreadingException;

    //public String getValueOfField(ParamOfGetResults param);
    

    public Map<String,List<String>> getUniprotRefAccesionsMap(ParamOfGetResults paramOfGetResults) throws MultiThreadingException;

    public Map<String, String> getNameMapByAccessions(String domain, Collection<String> accessions);
 
}
