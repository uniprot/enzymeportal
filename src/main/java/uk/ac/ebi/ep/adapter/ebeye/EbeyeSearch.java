package uk.ac.ebi.ep.adapter.ebeye;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ebi.ep.adapter.ebeye.EbeyeCallable.GetEntriesCallable;
import uk.ac.ebi.ep.adapter.ebeye.util.Transformer;
import uk.ac.ebi.ep.search.adapter.ISearchAdapter;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public abstract class EbeyeSearch implements ISearchAdapter {
    public static enum FieldsOfNameMap {
    id, name;
   public static List<String> getFields() {
       List<String> fields = new ArrayList<String>();
       fields.add(id.name());
       fields.add(name.name());
       return fields;
   }

    };

    protected String domain;

    public EbeyeSearch() {
    }

    public EbeyeSearch(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     *
     * @param domain
     * @param accessions
     * @return
     * @deprecated
     */
    public Map<String, String> getNameMapByAccessions(String domain, Set<String> accessions) {
        ArrayOfString fields = Transformer
                .transformToArrayOfString(FieldsOfNameMap.getFields());
        ArrayOfString idsArray = Transformer
                .transformToArrayOfString(accessions);

        GetEntriesCallable caller = new EbeyeCallable
                .GetEntriesCallable(domain, idsArray, fields);

        ArrayOfArrayOfString results = caller.callGetEntries();
        Map<String,String> resultMap = Transformer.transformToMap(results);
        return resultMap;
    }



    public Set<String> getNameSetByAccessions(String domain, Set<String> accessions) {
        ArrayOfString fields = Transformer
                .transformToArrayOfString(FieldsOfNameMap.name.name());
        ArrayOfString idsArray = Transformer
                .transformToArrayOfString(accessions);

        GetEntriesCallable caller = new EbeyeCallable
                .GetEntriesCallable(domain, idsArray, fields);

        ArrayOfArrayOfString results = caller.callGetEntries();
        Set<String> resultSet = Transformer.transformToSet(results);
        return resultSet;

    }

    public Set<String> getIdSet(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<String> getIdAccessionMap(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNumberOfResults(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

}
