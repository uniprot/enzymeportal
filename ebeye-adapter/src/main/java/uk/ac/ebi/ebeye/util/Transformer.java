package uk.ac.ebi.ebeye.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ep.ebeye.adapter.ResultFactory;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Transformer {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public static ArrayOfString transformToArrayOfString(List<String> list) {
        ArrayOfString arrayOfString = new ArrayOfString();
        arrayOfString.getString().addAll(list);
        return arrayOfString;
    }

    public static List<List<String>> transformToList(ArrayOfArrayOfString rawResults) {
        List<List<String>> rawResultList = new ArrayList<List<String>>();
        Iterator it = rawResults.getArrayOfString().iterator();
        while (it.hasNext()) {
            ArrayOfString arrayOfString = (ArrayOfString)it.next();
            List<String> resultLine = arrayOfString.getString();
            rawResultList.add(resultLine);
        }
        return rawResultList;
    }

    public static List<List<String>> transformToList(
            List<ArrayOfArrayOfString> rawResultsList) {
        List<List<String>> mergedResults = new ArrayList<List<String>>();
        Iterator it = rawResultsList.iterator();
        while (it.hasNext()) {
            ArrayOfArrayOfString resultLines = (ArrayOfArrayOfString)it.next();
            mergedResults.addAll(transformToList(resultLines));
        }
        return mergedResults;
    }
}
