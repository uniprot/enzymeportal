package uk.ac.ebi.ebeye;

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
public class ParamGetEntries extends ParamDomain {

//********************************* VARIABLES ********************************//
    protected List<String> entries;
    protected List<String> fields;

//******************************** CONSTRUCTORS ******************************//
    public ParamGetEntries() {
    }

    public ParamGetEntries(String domain, List<String> entries, List<String> fields) {
        super(domain);
        this.entries = entries;
        this.fields = fields;
    }

 //****************************** GETTER & SETTER *****************************//


    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    


}
