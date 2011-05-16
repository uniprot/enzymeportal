package uk.ac.ebi.ebeye.param;

import java.util.List;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamOfGetResults extends ParamOfGetAllResults {

//********************************* VARIABLES ********************************//
    protected int start;
    protected  int size;

    //This is configured in the config file
    //protected ResultFieldList resultFieldList;
    
//******************************** CONSTRUCTORS ******************************//


    public ParamOfGetResults(String domain, String query, List<String> fields, int start, int size) {
        super(domain, query, fields);
        this.start = start;
        this.size = size;
    }

    public ParamOfGetResults(String domain, String query, int start, int size) {
        super(domain, query);
        this.start = start;
        this.size = size;
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

//********************************** METHODS *********************************//

}
