package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.core.ext.api.EBeyeClient;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.EnzymeResultSet;
import uk.ac.ebi.ep.search.result.EnzymeSummary;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;
import uk.ac.ebi.ep.search.result.Species;
import uk.ac.ebi.ep.util.query.LucenceQuery;
import uk.ac.ebi.util.result.EBeyeDataTypeConverter;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.ArrayOfEntryReferences;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.ArrayOfString;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.EntryReferences;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    protected List<String> uniprotIdList;
    //protected SortedMap<String, Integer> uniprotIdList = new TreeSet<String>();
    protected EBeyeClient eBeyeClient;
    //TODO
    public static final int TOP_RESULT_SIZE = 100;
    public static final int RESULT_PER_DOMAIN_LIMIT = 20;
    public static final int START_AT = 0;
    private static Logger log = Logger.getLogger("EnzymeFinder.class");


//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() {
        eBeyeClient = new EBeyeClient();
        uniprotIdList = new ArrayList<String>();
    }


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeResultSet find(SearchParams searchInput) {
        String query = null;

        //eBeyeClient.srvProxyConnect();
        EBeyeRequestProcessor processor = new EBeyeRequestProcessor();
        //AllResultsIdsRunner runner = new AllResultsIdsRunner(processor);

        List<Domain> domainList = Config.domainList;
        Iterator it = domainList.iterator();
        ArrayOfString result = null;
        EnzymeResultSet resultSet = null;
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            query = LucenceQuery.createQueryOR(domain, searchInput);
            String domainId = domain.getId();
            /*
            log.debug("Domain: " +domainId);
            log.debug("getResultsIds query: " +query);
            log.debug("Start at: " +START_AT);
            log.debug("Finish at: " +RESULT_PER_DOMAIN_LIMIT);
             *
             */
            System.out.println("Domain: " +domainId);
            System.out.println("getResultsIds query: " +query);
            System.out.println("Start at: " +START_AT);
            System.out.println("Finish at: " +RESULT_PER_DOMAIN_LIMIT);
            try {
                result = eBeyeClient.getSrvProxy()
                        .getResultsIds(domainId, query, START_AT, RESULT_PER_DOMAIN_LIMIT);
            } catch (ServiceException ex) {
                //Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            log.debug("Results: " 
                    +EBeyeDataTypeConverter.convertArrayOfStringToString(result));
             *
             */
            System.out.println("Results: "
                    +EBeyeDataTypeConverter.convertArrayOfStringToString(result));

            //ArrayOfString result = eBeyeClient.getSrvProxy().getAllResultsIds(domainId, query);
            if (domainId.equals("uniprot")) {
                addUniprotIdsToResults(result, false);
            }
            //If the domain is not uniprot then the results must be converted
            //into uniprot ids
            else {
                try {
                    ArrayOfEntryReferences uniprotIds = getUniprotIds(domainId, result);
                     if (domainId.equals("intenz")) {
                        /*if the domain is intenz then there is no need to check if
                    the entry is enzyme.
                   */
                        addUniprotIdsToResults(uniprotIds, true);
                     }
                     else{
                        addUniprotIdsToResults(uniprotIds, false);
                     }
                    
                } catch (ServiceException ex) {
                   // Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> topResult = resultRanker.getRankedResults().subList(0, TOP_RESULT_SIZE);
        try {
            //enzyme.printMergedResults();
            resultSet = queryUniprotResults(topResult);
        } catch (ServiceException ex) {
            //Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }


    public static void main(String[] args) throws ServiceException, IOException, JAXBException {
        EnzymeFinder enzyme = new EnzymeFinder();
        int numberOfResults = enzyme.eBeyeClient.getSrvProxy().getNumberOfResults("uniprot", "EC:1.1*");
        Set<String> uniprotEnzymeList = new TreeSet<String>();
        ArrayOfString result = null;
        if (numberOfResults>RESULT_PER_DOMAIN_LIMIT) {
            int counter = 0;
            while (counter<numberOfResults) {                
                result = enzyme.eBeyeClient.getSrvProxy().getResultsIds("uniprot", "EC:1*", counter-1,RESULT_PER_DOMAIN_LIMIT);
                counter = counter+RESULT_PER_DOMAIN_LIMIT;
            }
        }
        else {
            result = enzyme.eBeyeClient.getSrvProxy().getResultsIds("uniprot", "EC:1*", START_AT, numberOfResults);
        }
         List<String> resultList = result.getString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            String id = (String)it.next();
            uniprotEnzymeList.add(id);
            System.out.println(id);
        }

        
    }


    public void printMergedResults() {
         Iterator it = this.uniprotIdList.iterator();
          Pattern p = Pattern.compile("HUMAN");
          int counter = 0;
         while (it.hasNext()) {
             String id = (String)it.next();
             //System.out.println(id);
             /*
             Matcher m = p.matcher(id);
             if (m.find()) {
                 System.out.println(id);
                 counter++;
             }
            */
         }
          //System.out.println("Length" +counter);
    }

    public EnzymeResultSet queryUniprotResults(List<String> IdList) throws ServiceException {
        //String[] myArgs = {"--getResults","uniprot","gene_primary_name:dehydrogenase","acc, descSubName, gene_primary_name, id, organism_scientific_name, sequence_length, status","0","20" };
        //getEntry(domain, entry, fields)
        Iterator it = IdList.iterator();
        ArrayOfString result = null;        
        Domain uniprotDomain = Config.getDomain("uniprot");
        ArrayOfString resultFields = EBeyeDataTypeConverter
                                    .createEbeyeFieldArray(uniprotDomain);
       EnzymeResultSet resultSet = new EnzymeResultSet();
        EnzymeSummaryCollection enzymes = new EnzymeSummaryCollection();

        while (it.hasNext()) {
            String id = (String)it.next();
            result = eBeyeClient.getSrvProxy().getEntry(
                    uniprotDomain.getId(), id,resultFields);

             enzymes.getEnzymeSummary().add(createResultSet(result.getString()));
        }
         resultSet.setEnzymeSummaryCollection(enzymes);
         return resultSet;
     //  Config.getDomain("uniprot").getResultFieldList().getResultField().
    }
    
    public EnzymeSummary createResultSet(List<String> result) {
        EnzymeSummary enzymeSummary= new EnzymeSummary();
        int counter = 0;
        for (String field:result) {
            String value = new String(field);
            switch (counter) {
                case 0: {
                    enzymeSummary.setUniprotid(value);
                    break;
                }
                case 1: {
                    enzymeSummary.setUniprotaccession(value);
                    break;
                }
                case 2: {
                    enzymeSummary.setName(value);
                    break;
                }
                case 3: {
                    Species species = new Species();
                    species.setScientificname(value);
                    enzymeSummary.setSpecies(species);
                    break;
                }
            }
        counter++;
        }
       //System.out.println(enzymeSummary.getName() +", " +enzymeSummary.getSpecies().getScientificname());
       return enzymeSummary;

    }
      
     

    public void printResults(ArrayOfArrayOfString result) {
        List resultList = result.getArrayOfString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            createResultSet(arrayList.getString());
        }        
    }

    public void printResults(List<String> result) {
        Iterator it = result.iterator();
        while (it.hasNext()) {
            String resultItem = (String)it.next();
            System.out.println(resultItem);
        }
    }

    public ArrayOfEntryReferences getUniprotIds(String domainId, ArrayOfString result) throws ServiceException{
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfEntryReferences refSearchResult = eBeyeClient.getSrvProxy()
                .getReferencedEntriesSet(domainId, result, "uniprot", resultRefFields);
         return refSearchResult;
    }

    
    public List<String> addUniprotIdsToResults(ArrayOfEntryReferences result, boolean checkEnzyme) {
        List refList = result.getEntryReferences();
        Iterator it = refList.iterator();
        while (it.hasNext()) {
           EntryReferences entryReferences = (EntryReferences)it.next();
           JAXBElement<ArrayOfArrayOfString> jAXBElement = entryReferences.getReferences();
            uniprotIdList = addUniprotIdsToResults(jAXBElement.getValue(), checkEnzyme);
        }
        return uniprotIdList;
    }

    public List<String> mergeUniprotId(List<ArrayOfString> result, boolean checkEnzyme) {
        Iterator it = result.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            uniprotIdList = addUniprotIdsToResults(arrayList, checkEnzyme);
        }
        return uniprotIdList;
    }

    public List<String> addUniprotIdsToResults(ArrayOfArrayOfString result
            ,  boolean checkEnzyme) {
        List resultList = result.getArrayOfString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            uniprotIdList = addUniprotIdsToResults(arrayList, checkEnzyme);
        }
        return uniprotIdList;
    }

    public List<String> addUniprotIdsToResults( ArrayOfString result, boolean checkEnzyme) {
        List resultList = result.getString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            String id = (String)it.next();
            if (checkEnzyme){
                if (isEnzyme(id)) {
                    uniprotIdList.add(id);
                }
            }
            else {
                uniprotIdList.add(id);
            }
        }
        return uniprotIdList;
    }


    public boolean isEnzyme(String uniprotId){
        boolean isEnzyme=false;
        String query = LucenceQuery.createQueryToGetEnzymeOnly(uniprotId);
        ArrayOfString result = null;
        try {
            result = eBeyeClient.getSrvProxy().getResultsIds("uniprot", query, 0, 1);
        } catch (ServiceException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        if (result != null) {
            if (result.getString().size()>0){
                isEnzyme = true;
            }
        }
        return isEnzyme;
    }

}
