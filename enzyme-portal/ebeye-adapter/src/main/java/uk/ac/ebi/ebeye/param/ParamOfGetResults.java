package uk.ac.ebi.ebeye.param;

import java.util.List;

import uk.ac.ebi.ep.config.Domain;

/**
 * Objects of this class wrap query parameters:
 * <ul>
 * 	<li>domain: the {@link Domain domain} to restrict the search to.</li>
 * 	<li>query: a Lucene query.</li>
 * 	<li>totalFound: the total number of results found.</li>
 * 	<li>fields: the fields to be retrieved from the Lucene index.</li>
 * </ul>
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamOfGetResults extends ParamGetNumberOfResults {
    //This is configured in the config file
    //protected ResultFieldList resultFieldList;
    //It's better to have it here to decouple the code completely
    protected List<String> fields;    
    //protected List<ParamOfResultSize> resultSizeList;

//********************************* VARIABLES ********************************//

//******************************** CONSTRUCTORS ******************************//
    public ParamOfGetResults(String domain, String query) {
        super(domain, query);
    }

    public ParamOfGetResults(String domain, String query, List<String> fields) {
        super(domain, query);
        this.fields = fields;
    }
    

//****************************** GETTER & SETTER *****************************//


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    
//********************************** METHODS *********************************//

}
