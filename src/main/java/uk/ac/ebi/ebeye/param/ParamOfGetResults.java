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
    protected int totalFound;
    protected List<ParamOfResultSize> resultSizeList;
//********************************* VARIABLES ********************************//

//******************************** CONSTRUCTORS ******************************//
    public ParamOfGetResults(String domain, String query) {
        super(domain, query);
    }

    public ParamOfGetResults(String domain, String query, List<String> fields) {
        super(domain, query);
        this.fields = fields;
    }

    public ParamOfGetResults(String domain, String query, List<String> fields, int totalFound) {
        super(domain, query);
        this.fields = fields;
        this.totalFound = totalFound;
    }

    


//****************************** GETTER & SETTER *****************************//


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    public List<ParamOfResultSize> getResultSizeList() {
        return resultSizeList;
    }

    public void setResultSizeList(List<ParamOfResultSize> resultSizeList) {
        this.resultSizeList = resultSizeList;
    }



    
//********************************** METHODS *********************************//

}
