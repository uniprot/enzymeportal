package uk.ac.ebi.ep.adapter.ebeye.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ebi.ebinocle.webservice.ArrayOfEntryReferences;
import uk.ac.ebi.ebinocle.webservice.EntryReferences;
import uk.ac.ebi.ebisearchservice.ArrayOfArrayOfString;
import uk.ac.ebi.ebisearchservice.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Transformer {

    public static ArrayOfString transformToArrayOfString(Collection<String> list) {
        ArrayOfString arrayOfString = new ArrayOfString();
        arrayOfString.getString().addAll(list);
        return arrayOfString;
    }

    /**
     * Transforms a list of {@link ArrayOfArrayOfString} into an ArrayOfString.
     * To that end, <b>only the first items of the contained {@link ArrayOfString}s
     * is taken into account</b>. Any other strings are lost.
     * @param laas
     * @return
     */
    public static ArrayOfString transformToArrayOfString(List<ArrayOfArrayOfString> laas){
    	ArrayOfString result = null;
    	for (ArrayOfArrayOfString aas : laas) {
        	for (ArrayOfString as : aas.getArrayOfString()) {
    			if (result == null) result = as;
    			else result.getString().add(as.getString().get(0));
    		}
		}
    	return result;
    }

    public static ArrayOfString transformToArrayOfString(String field) {
        ArrayOfString arrayOfString = new ArrayOfString();
        arrayOfString.getString().add(field);
        return arrayOfString;
    }

    /**
     * Converts a String[][] into String[] by concatenating all of the items in
     * one single array.
     * @param aoaos the String[][]
     * @return the String[], or <code>null</code> if the input is
     *      <code>null</code> or empty.
     * @since 1.0.4
     */
    public static ArrayOfString transformToArrayOfString(
            ArrayOfArrayOfString aoaos){
        if (aoaos == null || aoaos.getArrayOfString() == null){
            return null;
        }
        ArrayOfString arrayOfStrings = null;
        for (ArrayOfString aos : aoaos.getArrayOfString()) {
            if (aos == null || aos.getString().isEmpty()) continue;
            if (arrayOfStrings == null){
                arrayOfStrings = aos;
            } else {
                arrayOfStrings.getString().addAll(aos.getString());
            }
        }
        return arrayOfStrings;
    }

    public static List<List<String>> transformToList(ArrayOfArrayOfString rawResults) {
        List<List<String>> rawResultList = new ArrayList<List<String>>();
        Iterator<?> it = rawResults.getArrayOfString().iterator();
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
        Iterator<ArrayOfArrayOfString> it = rawResultsList.iterator();
        while (it.hasNext()) {
            ArrayOfArrayOfString resultLines = it.next();
            mergedResults.addAll(transformToList(resultLines));
        }
        return mergedResults;
    }





    public static LinkedHashSet<String> transformFieldValueToList(ArrayOfArrayOfString rawResults
            , boolean isUNIPROTfield) {
        LinkedHashSet<String> rawResultList = new LinkedHashSet<String>();
        Iterator<?> it = rawResults.getArrayOfString().iterator();
        while (it.hasNext()) {
            ArrayOfString arrayOfString = (ArrayOfString)it.next();
            String fieldValue = arrayOfString.getString().get(0);
            if (fieldValue == null) continue;
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
        Iterator<ArrayOfArrayOfString> it = rawResultsList.iterator();
        while (it.hasNext()) {
            ArrayOfArrayOfString resultLines = it.next();
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
        Map<String, String> resultLineMap = new LinkedHashMap<String, String>();
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

    public static Map<String, List<String>> transformToMap(
                                        ArrayOfEntryReferences arrayOfEntryReferences) {
        Map<String, List<String>> resultMap = new Hashtable<String, List<String>>();
        List<EntryReferences> entryRefs = arrayOfEntryReferences.getEntryReferences();
        for (EntryReferences entryRef:entryRefs) {
            String id = entryRef.getEntry().getValue();
            ArrayOfArrayOfString accs = entryRef.getReferences().getValue();
            List<String> accList = transformToListOfString(accs);
            if (accList.size()>0) {
                resultMap.put(id, accList);
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
        Iterator<ArrayOfString> it = arrayOfArrayOfString.getArrayOfString().iterator();
        while (it.hasNext()) {
            ArrayOfString arrayOfString = it.next();
            resultList.addAll(arrayOfString.getString());
        }
        return resultList;
    }
    
    /**
     * Transforms each of the passed enumeration items into Strings, using
     * their <code>name</code> method.
     * @param enumItems
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static List<String> transformToListOfString(Enum[] enumItems){
    	String[] strings = new String[enumItems.length];
    	for (int i = 0; i < strings.length; i++) {
			strings[i] = enumItems[i].name();
		}
    	return Arrays.asList(strings);
    }
}
