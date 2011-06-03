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
public class ParamOfResultSize {

//********************************* VARIABLES ********************************//
    protected int start;
    protected  int size;

//******************************** CONSTRUCTORS ******************************//

    public ParamOfResultSize(int start, int size) {
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
