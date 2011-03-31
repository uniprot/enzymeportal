package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.core.ext.api.EBeyeClient;
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
protected Set<String> uniprotIdList = new TreeSet<String>();
protected EBeyeClient eBeyeClient = new EBeyeClient();

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public Map<String,Map<String,String>> find(Object searchInput) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public static void main(String[] args) throws ServiceException, IOException, JAXBException {
        String query = null;
        
        //eBeyeClient.srvProxyConnect();
        EBeyeRequestProcessor processor = new EBeyeRequestProcessor();
        //AllResultsIdsRunner runner = new AllResultsIdsRunner(processor);

        EnzymeFinder enzyme = new EnzymeFinder();
        List<Domain> domainList = Config.domainList;
        Iterator it = domainList.iterator();
        ArrayOfString result = null;
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            query = LucenceQuery.createQueryOR(domain);
            String domainId = domain.getId();
            result = enzyme.eBeyeClient.getSrvProxy().getResultsIds(domainId, query, 0, 10);
            //ArrayOfString result = eBeyeClient.getSrvProxy().getAllResultsIds(domainId, query);           
            if (domainId.equalsIgnoreCase("uniprot")) {
                enzyme.mergeUniprotId(result);
            }
            else {
                enzyme.mergeUniprotId(domainId, result);
            }
        }
        //enzyme.printMergedResults();
        enzyme.queryUniprotResults(enzyme.uniprotIdList);
        
    }

    public void printMergedResults() {
         Iterator it = this.uniprotIdList.iterator();
          Pattern p = Pattern.compile("HUMAN");
          int counter = 0;
         while (it.hasNext()) {
             String id = (String)it.next();
             System.out.println(id);
             /*
             Matcher m = p.matcher(id);
             if (m.find()) {
                 System.out.println(id);
                 counter++;
             }
            */
         }
          System.out.println("Length" +counter);
    }

    public void queryUniprotResults(Set<String> IdList) throws ServiceException {
        //String[] myArgs = {"--getResults","uniprot","gene_primary_name:dehydrogenase","acc, descSubName, gene_primary_name, id, organism_scientific_name, sequence_length, status","0","20" };
        //getEntry(domain, entry, fields)
        Iterator it = IdList.iterator();
        ArrayOfString result = null;        
        Domain uniprotDomain = Config.getDomain("uniprot");
        ArrayOfString resultFields = EBeyeDataTypeConverter
                                    .createEbeyeFieldArray(uniprotDomain);       
        while (it.hasNext()) {
            String id = (String)it.next();
            result = eBeyeClient.getSrvProxy().getEntry(
                    uniprotDomain.getId(), id,resultFields);
             printResults(result.getString());
        }
     //  Config.getDomain("uniprot").getResultFieldList().getResultField().
    }
    /*
    public void createResultSet(resultFields, List<String> result) {
        ResultField resultField = 
        int counter = 0;
        for (String field:result) {
            switch (counter) {
                case 0:
            }
        }

    }
     * 
     */

    public void printResults(ArrayOfArrayOfString result) {
        List resultList = result.getArrayOfString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            printResults(arrayList.getString());
        }        
    }

    public void printResults(List<String> result) {
        Iterator it = result.iterator();
        while (it.hasNext()) {
            String resultItem = (String)it.next();
            System.out.println(resultItem);
        }
    }

    public Set<String> mergeUniprotId(String domainId, ArrayOfString result) throws ServiceException{
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfEntryReferences refSearchResult = eBeyeClient.getSrvProxy().getReferencedEntriesSet(domainId, result, "uniprot", resultRefFields);       
        mergeUniprotId(refSearchResult);
         return uniprotIdList;
    }

    
    public Set<String> mergeUniprotId(ArrayOfEntryReferences result) {        
        List refList = result.getEntryReferences();
        Iterator it = refList.iterator();
        while (it.hasNext()) {
           EntryReferences entryReferences = (EntryReferences)it.next();
           JAXBElement<ArrayOfArrayOfString> jAXBElement = entryReferences.getReferences();
            uniprotIdList = mergeUniprotId(jAXBElement.getValue());
        }
        return uniprotIdList;
    }

    public Set<String> mergeUniprotId(List<ArrayOfString> result) {
        Iterator it = result.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            uniprotIdList = mergeUniprotId(arrayList);
        }
        return uniprotIdList;
    }

    public Set<String> mergeUniprotId(ArrayOfArrayOfString result) {       
        List resultList = result.getArrayOfString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            ArrayOfString arrayList = (ArrayOfString)it.next();
            uniprotIdList = mergeUniprotId(arrayList);
        }
        return uniprotIdList;
    }
    
    public Set<String> mergeUniprotId( ArrayOfString result) {
        List resultList = result.getString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            uniprotIdList.add((String)it.next());
        }
        return uniprotIdList;
    }

}
