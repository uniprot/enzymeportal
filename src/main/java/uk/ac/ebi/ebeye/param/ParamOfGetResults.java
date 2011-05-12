package uk.ac.ebi.ebeye.param;

import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamOfGetResults extends ParamGetNumberOfResults {
    protected int start;
    protected  int size;
    //This is configured in the config file
    //protected ResultFieldList resultFieldList;
    //It's better to have it here to decouple the code completely
    protected ArrayOfString fields;
//********************************* VARIABLES ********************************//

//******************************** CONSTRUCTORS ******************************//
    public ParamOfGetResults(String domain, String query) {
        super(domain, query);
    }

    public ParamOfGetResults(String domain, String query, ArrayOfString fields, int start, int size) {
        super(domain, query);
        this.start = start;
        this.size = size;
        this.fields = fields;
    }



//****************************** GETTER & SETTER *****************************//

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public ArrayOfString getFields() {
        return fields;
    }

    public void setFields(ArrayOfString fields) {
        this.fields = fields;
    }

    
//********************************** METHODS *********************************//

}
