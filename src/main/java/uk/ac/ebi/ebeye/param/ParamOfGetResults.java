package uk.ac.ebi.ebeye.param;

import java.util.List;

/**
 *
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
