package uk.ac.ebi.ebeye;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 * @deprecated 
 */
public class ParamOfGetResults extends ParamGetNumberOfResults {
    protected int start;
    protected  int size;
    //This is configured in the config file
    //protected ResultFieldList resultFieldList;

//********************************* VARIABLES ********************************//

//******************************** CONSTRUCTORS ******************************//
    public ParamOfGetResults(String domain, String query) {
        super(domain, query);
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
