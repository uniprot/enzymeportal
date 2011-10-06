package uk.ac.ebi.ep.adapter.ebeye.param;

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
