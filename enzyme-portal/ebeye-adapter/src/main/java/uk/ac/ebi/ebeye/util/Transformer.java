package uk.ac.ebi.ebeye.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static ArrayOfString transformToArrayOfString(Collection<String> list) {
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





    public static Collection<String> transformFieldValueToList(ArrayOfArrayOfString rawResults
            , boolean isUNIPROTfield) {
        Collection<String> rawResultList = new LinkedHashSet<String>();
        Iterator it = rawResults.getArrayOfString().iterator();
        while (it.hasNext()) {
            ArrayOfString arrayOfString = (ArrayOfString)it.next();
            String fieldValue = arrayOfString.getString().get(0);
            if (isUNIPROTfield) {
                rawResultList.addAll(transformAccessionsString(fieldValue));
            } else {
                rawResultList.add(fieldValue);
            }
            
        }
        return rawResultList;
    }

    public static LinkedHashSet<String> transformFieldValueToList(
            List<ArrayOfArrayOfString> rawResultsList,  boolean isUNIPROTfield) {
        LinkedHashSet<String> mergedResults = new LinkedHashSet<String>();
        Iterator it = rawResultsList.iterator();
        while (it.hasNext()) {
            ArrayOfArrayOfString resultLines = (ArrayOfArrayOfString)it.next();
            mergedResults.addAll(transformFieldValueToList(resultLines, isUNIPROTfield));
        }
        return mergedResults;
    }

    public static List<String> transformAccessionsString(String ebeyeAccList) {
     String[] accArray = ebeyeAccList.split("\\s");
     List<String> accList = new ArrayList<String>();
     for (String acc: accArray) {
         if (acc != null) {
             if (!acc.equals("")) {
                accList.add(acc);
             }
         }
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

    public static Set<String> transformToSet(ArrayOfArrayOfString rawResultsList) {
        Set<String> resultSet = new LinkedHashSet<String>();
        List<ArrayOfString> resultLines = rawResultsList
                .getArrayOfString();
        for (ArrayOfString resultLine: resultLines) {
            resultSet.add(resultLine.getString().get(0));
        }
        return resultSet;
    }

    public static Map<String,String> transformToMap(
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

    public static Map<String,Map<String,String>> transformIdAndNameMap(
                                       ArrayOfArrayOfString rawResults) {
        Map<String,Map<String,String>> resultMap = new Hashtable<String,Map<String,String>>();

        List<List<String>> results = Transformer.transformToList(rawResults);
        for (List<String> resultFields:results) {
            String uniprotId = resultFields.get(0);
            String referencedId = resultFields.get(1);
            String referencedName = resultFields.get(2);
            Map<String,String> idAndName = new HashMap<String, String>();
            idAndName.put(referencedId, referencedName);
            resultMap.put(uniprotId, idAndName);
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
