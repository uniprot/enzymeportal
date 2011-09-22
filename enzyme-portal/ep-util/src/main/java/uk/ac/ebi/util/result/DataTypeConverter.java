package uk.ac.ebi.util.result;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
//import uk.ac.ebi.ebeye.ResultOfGetReferencedEntriesSet;
//import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.ResultField;
import uk.ac.ebi.ep.config.ResultFieldList;
import uk.ac.ebi.ep.config.SearchField;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.result.Pagination;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class DataTypeConverter {


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static URL createEncodedUrl(String baseUrl, String decodedQuery) throws UnsupportedEncodingException, MalformedURLException {
            String charset = "UTF-8";
            String encodedRequest = String.format(baseUrl+"%s",
            URLEncoder.encode(decodedQuery, charset));
            URL url = new URL(encodedRequest);
            return url;
    }

    public static List<String> getReactionXrefs(EnzymeModel enzymeModel) {
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        List<String> reactomeReactionIds = new ArrayList<String>();
        for (ReactionPathway reactionPathway:reactionPathways) {
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
            List<Object> xrefs = enzymeReaction.getXrefs();
            if (xrefs != null) {
                for (Object xref: xrefs) {
                    reactomeReactionIds.add((String)xref);
                }
            }
        }
        return reactomeReactionIds;
    }
/*
    public static String accessionToXLink(String url, String accession) {
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        sb.append(accession);
        return sb.toString();
    }
*/

    /**
     * ec numbers end with "-" will be excluded from the results.
     * @param enzymeSummaryList
     * @return
     * @throws MultiThreadingException
     */
    public static Set<String> getUniprotEcs(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {
        Set<String> ecSet = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            ecSet.addAll(getUniprotEcs(enzymeSummary));
        }
        return ecSet;
    }

    public static List<String> getMoleculeNames(List<Molecule> mols) {
        List<String> molNames = new ArrayList<String>();
        for (Molecule mol:mols) {
            molNames.add(mol.getName());
        }
        return molNames;
    }

    public static Set<String> getUniprotEcs(EnzymeSummary enzymeSummary) throws MultiThreadingException {
        Set<String> ecSet = new TreeSet<String>();
            List<String> ecList = enzymeSummary.getEc();
            for (String ec: ecList) {
                ecSet.add(ec);
                //if (!ec.contains("-")) {
                //}
        }
        return ecSet;
    }

    //TODO Maybe not necesssary
    /*
    public static Set<String> getPdbeAccessions(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {
        Set<String> pdbeAccs = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            enzymeSummary.getPdbeaccession();
        }
        return ecSet;
    }
*/
    public static List<List<String>> createSubLists(List<String> longList, int subListSize)  {
        List<List<String>> subLists = new ArrayList<List<String>>();
        int listSize = longList.size();
        int endIndex = 0;
        //Work around to solve big result set issue
        Pagination pagination = new Pagination(listSize, subListSize);
        int nrOfQueries = pagination.calTotalPages();
        int start = 0;
        //TODO
        for (int i = 0; i < nrOfQueries; i++) {
            //In case of last page and the result of the last page is more than 0
            if (i == (nrOfQueries - 1) && (listSize % subListSize) > 0) {
                endIndex = endIndex + pagination.getLastPageResults();
            } else {
                endIndex = endIndex + subListSize;
            }

            List<String> subList = longList.subList(start, endIndex);
            subLists.add(subList);

            start = endIndex;
        }
        return subLists;
    }

    /**
     * Merges several lists of Strings into one collection, limiting its
     * size. If the given limit has to be applied, the last objects are
     * discarded.
     * @param multipleList a collection of lists of Strings.
     * @param maxResult The maximum number of elements in the returned
     * 		collection.
     * @return a single collection of Strings with at most
     * 		<code>maxResult</code> elements.
     */
    public static LinkedHashSet<String> mergeAndLimitResult(
            Collection<List<String>> multipleList, int maxResult) {
        LinkedHashSet<String> mergedSet = new LinkedHashSet<String>();
        for (List<String> list : multipleList){
            if (mergedSet.size()+list.size() < maxResult){
                mergedSet.addAll(list);
            } else {
            	int i = 0;
            	while (mergedSet.size() < maxResult){
            		mergedSet.add(list.get(i++));
            	}
            	break;
            }
        }
        return mergedSet;
    }
    
    public static LinkedHashSet<String> mergeList(Collection<List<String>> multipleList) {
        LinkedHashSet mergedSet = new LinkedHashSet();
        for (List<String> list:multipleList){
            mergedSet.addAll((List<String>)list);
        }
        return mergedSet;
    }
    public static List<String> accessionsToList (String[] arrayOfAccessions) {
        List<String> list = new ArrayList<String>();
        for (String accession:arrayOfAccessions) {
            list.add(accession);
        }
        return list;
    }
    /**
     * Retieve the ids of all domains in the {@link ResultOfGetResultsIds} list
     * @param resultList
     * @return
     */
    /*
    public static List<String> getResultsIds(
        List<ResultOfGetResultsIds> resultList) {
        Iterator it = resultList.iterator();
        List<String> resultsIds = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetResultsIds result =
            (ResultOfGetResultsIds)it.next();
            resultsIds.addAll(result.getResult().getString());
        }
        return resultsIds;
    }
   
    public static List<String> getResultsIdsFromDomain(
        List<ResultOfGetResultsIds> resultList, String domain) {
        Iterator it = resultList.iterator();
        List<String> uniprotIdList = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetResultsIds result =
            (ResultOfGetResultsIds)it.next();
            String resultDomain = result.getParamOfGetResultsIds()
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            if (resultDomain.equals(domain)) {
                uniprotIdList.addAll(result.getResult().getString());
            }
        }
        return uniprotIdList;
    }
    
    public static List<ResultOfGetResultsIds> excludeDomainFromResults(
        List<ResultOfGetResultsIds> resultList, String domain) {
        Iterator it = resultList.iterator();
        List<ResultOfGetResultsIds> uniprotIdList =
                        new ArrayList<ResultOfGetResultsIds>();
        while (it.hasNext()) {
            ResultOfGetResultsIds result =
            (ResultOfGetResultsIds)it.next();
            String resultDomain = result.getParamOfGetResultsIds()
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            if (!resultDomain.equals(domain)) {
                uniprotIdList.add(result);
            }
        }
        return uniprotIdList;
    }

    
    public static List<String> getXrefIdsFromDomain(
            List<ResultOfGetReferencedEntriesSet> xrefResults, String domain) {
         List<String> result = new ArrayList<String>();
        Iterator it = xrefResults.iterator();
        while(it.hasNext()) {
            ResultOfGetReferencedEntriesSet xrefResult =
                    (ResultOfGetReferencedEntriesSet)it.next();
            String resultDomain = xrefResult
                    .getResultOfGetResultsIds()
                    .getParamOfGetResultsIds()
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            if (resultDomain.equals(domain)) {
                result = xrefResult.getUniprotXRefList();
            }
        }
        return result;
    }
 */
    public static List<String> getConfigResultFields(Domain domain) {
        List<ResultField> fields = domain.getResultFieldList().getResultField();
        List<String> configFields = new ArrayList<String>();
        Iterator it = fields.iterator();
        while (it.hasNext()) {
            ResultField field = (ResultField)it.next();
            configFields.add(field.getId());
        }
        return configFields;
    }

    public static List<String> getConfigSearchFields(Domain domain) {
        List<SearchField> fields = domain.getSearchFieldList().getSearchField();
        List<String> configFields = new ArrayList<String>();
        Iterator it = fields.iterator();
        while (it.hasNext()) {
            SearchField field = (SearchField)it.next();
            configFields.add(field.getId());
        }
        return configFields;
    }

    public static List<Compound> mapToCompound(Map<String,String> compoundMap) {
         List<Compound> compoundList = new ArrayList<Compound>();
         Set<String> keys = compoundMap.keySet();
         for (String key:keys) {
             Compound compound = new Compound();
             compound.setId(key);
             compound.setName(compoundMap.get(key));
             compoundList.add(compound);
         }
         return compoundList;
    }

    public static List<Compound> listToCompound(Collection<String> compoundIds) {
         List<Compound> compoundList = new ArrayList<Compound>();
         for (String compoundId:compoundIds) {
             Compound compound = new Compound();
             compound.setId(compoundId);
             compoundList.add(compound);
         }
         return compoundList;
    }
/*
    public static Map<String,String> rankMap(Map<String,String> map, String matchedKeywords) {
        Map<String,String> rankedMap = new lLinkedHashMap<String, String>();
        rankedMap.
    }
*/
    public static ResultFieldList cloneResultFieldList(ResultFieldList originalObj) {
        ResultFieldList clonedObj = new ResultFieldList();
        Iterator clonedIt = clonedObj.getResultField().iterator();
        Iterator orgIt = originalObj.getResultField().iterator();
        while (orgIt.hasNext() && clonedIt.hasNext()) {
            ResultField clonedResultField = (ResultField)clonedIt.next();
            ResultField orgResultField = (ResultField)orgIt.next();
            clonedResultField.setId(orgResultField.getId());
            clonedResultField.setName(orgResultField.getName());
            clonedResultField.setResultvalue(orgResultField.getResultvalue());
            clonedObj.getResultField().add(clonedResultField);
        }
        return clonedObj;
    }

/*
    
    public static String StringOfAccessionsToXLinks(Constant.DOMAINS_ENUM domains,
                                                                    String accessions) {
        String accessionXLinks = null;
        switch (domains) {
            case unitprot:{
                accessionXLinks = uniprotAccessionsToXLinks(accessions);
                break;
            }
        }
        return accessionXLinks;
    }
    
    public static String uniprotAccessionsToXLinks(String accessions) {        
        ResourceBundle templateFile = ResourceBundle.getBundle("template");
        String[] arrayOfAccessions = accessions.split(" ");
        String url = templateFile.getString("uniprotUrl");
        StringBuffer sb = new StringBuffer();
        for (String accession:arrayOfAccessions) {
            Object[] messageArguments = {accession,url+accession,accession};
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(templateFile.getString("xlinkTemplate"));
            String xLink = formatter.format(messageArguments);
            sb.append(xLink);
            sb.append(" ");
        }
        return sb.toString();
        
    }
     * */

}
