package uk.ac.ebi.util.result;

import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.ResultField;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EBeyeDataTypeConverter {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static ArrayOfString createEbeyeFieldArray(Domain domain) {
        List<ResultField> fieldList = domain.getResultFieldList().getResultField();
        Iterator it = fieldList.iterator();
        ArrayOfString resultRefFields= new ArrayOfString();
        while (it.hasNext()) {
            String field = (String)it.next();
            resultRefFields.getString().add(field);
        }
        return resultRefFields;
    }
}
