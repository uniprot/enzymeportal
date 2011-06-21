package uk.ac.ebi.ebeye.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfEntryReferences;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;
import uk.ac.ebi.webservices.ebeye.EntryReferences;

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

    public static ArrayOfString transformToArrayOfString(String field) {
        ArrayOfString arrayOfString = new ArrayOfString();
        arrayOfString.getString().add(field);
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

    public static List<String> transform(String ebeyeAccList) {
     String[] accArray = ebeyeAccList.split("\\s");
     List<String> accList = new ArrayList<String>();
     for (String acc: accArray) {
         accList.add(acc);
     }
     return accList;
    }

    public static Map<String,String> transformToMap(ArrayOfArrayOfString rawResultsList) {
        Map<String, String> resultLineMap = new HashMap<String, String>();
        List<ArrayOfString> resultLines = rawResultsList
                .getArrayOfString();
        for (ArrayOfString resultLine: resultLines) {
            resultLineMap.put(resultLine.getString().get(0),
                    resultLine.getString().get(1));
        }
        return resultLineMap;
    }

    public static Map<String,String> transformToList(
                                        ArrayOfEntryReferences arrayOfEntryReferences) {
        Map<String,String> resultMap = new Hashtable<String, String>();
        List<EntryReferences> entryRefs = arrayOfEntryReferences.getEntryReferences();
        for (EntryReferences entryRef:entryRefs) {
            String id = entryRef.getEntry().getValue();
            ArrayOfArrayOfString Accs = entryRef.getReferences().getValue();
            List<String> accList = transformToListOfString(Accs);
            if (accList.size()>0) {
                resultMap.put(id, accList.get(0));
            }

        }

        return resultMap;

    }


    public static List<String> transformToListOfString(
                                                ArrayOfArrayOfString arrayOfArrayOfString) {
        List<String> resultList = new ArrayList<String>();
        Iterator it = arrayOfArrayOfString.getArrayOfString().iterator();
        while (it.hasNext()) {
            ArrayOfString arrayOfString = (ArrayOfString) it.next();
            resultList.addAll(arrayOfString.getString());
        }
        return resultList;
    }
}
