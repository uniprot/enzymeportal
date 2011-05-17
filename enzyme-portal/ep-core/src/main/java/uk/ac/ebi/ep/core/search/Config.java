package uk.ac.ebi.ep.core.search;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ebeye.param.ParamOfGetAllResults;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.EnzymeRelatedDomains;
import uk.ac.ebi.ep.config.ResultField;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.ebeye.result.jaxb.ResultCollection;
import uk.ac.ebi.ep.ebeye.result.jaxb.UniprotResult;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Config {

//********************************* VARIABLES ********************************//
    public static List<Domain> domainList;
    public static List<Result> enzymesInUniprot;
    //public static List<String> enzymeUniprotIds;
    //public static Set<String> enzymeUniprotAccs;
    public static String configFile = "/config.xml";
    public static String resourcePath = new File(
            Config.class.getResource(configFile).getPath()
    ).getParent();
    public static File cacheFile = new File (resourcePath+"/enzymeUniprot.xml");

    static {
        try {
            EnzymeRelatedDomains enzymeRelatedDomains =
                    //unmarshalConfigFile(new File("/Users/hongcao/Projects/enzymeportal/trunk/enzyme-portal/ep-core/src/main/java/uk/ac/ebi/ep/core/search/config.xml"));
                    unmarshalConfigFile(Config.class.getResourceAsStream(configFile));
            domainList = enzymeRelatedDomains.getDomain();
            //enzymesInUniprot = unmarshalCacheFile(Config.class.getResourceAsStream(""));
        } catch (IOException ex) {
            //COnfig file not found
        } catch (JAXBException ex) {
            //Config file provided can be unmarshalled
        }
        
        ResultCollection resultCollection = null;
        try {
            resultCollection = unmarshalCacheFile(cacheFile);
        } catch (IOException ex) {

        } catch (JAXBException ex) {

        }
        enzymesInUniprot =  resultCollection.getResultlist();

    }

    /*

        System.out.println("Number of enzyme uniprot accessions: " +resultCollection.getResultlist().size());
        enzymeUniprotAccs =  loadEnzymeUniprotAccessions(resultCollection);
        System.out.println("Number of non duplicated enzyme uniprot accessions: "
                +enzymeUniprotAccs.size());
    
     */
    public static File marshalResultToXml(ResultCollection resultCollection, File f) throws JAXBException
            {
            JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.ebeye.result.jaxb");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ebi.ac.uk/enzymeportal/ebeye/result ebeyeResult.xsd");
            /*
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    CmlConstant.CMLREACT_NAME_SPACE + " "
                    + CmlConstant.CMLREACT_SCHEMA_LOCATION);
             *
             */
            //jaxb.schemaLocation
            //RheaWsResponse response = cmlMapper.createCmlreactResponse();
            //Reaction response = cmlMapper.mapRheaReaction();
            marshaller.marshal(resultCollection, f);
        return f;
    }

    public static EnzymeRelatedDomains unmarshalConfigFile(InputStream inFile)
            throws IOException, JAXBException {
        EnzymeRelatedDomains enzymeRelatedDomains = null;
        JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.config");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement jaxbElement = (JAXBElement) unmarshaller.unmarshal(inFile);
        enzymeRelatedDomains = (EnzymeRelatedDomains) jaxbElement.getValue();
        return enzymeRelatedDomains;
    }

    public static ResultCollection unmarshalCacheFile(File inFile)
            throws IOException, JAXBException {
        ResultCollection resultCollection = null;
        JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.ebeye.result.jaxb");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        //JAXBElement jaxbElement = (JAXBElement) unmarshaller.unmarshal(inFile);
        resultCollection = (ResultCollection) unmarshaller.unmarshal(inFile);
        return resultCollection;
    }

    public static Set<String> loadEnzymeUniprotAccessions (
                                    ResultCollection resultCollection) {
        Iterator it = resultCollection.getResultlist().iterator();
        Set<String>  accessions = new TreeSet();
        while (it.hasNext()) {
            Result result = (Result)it.next();
            accessions.addAll(result.getAcc());
        }
        return accessions;
    }

    public static Domain getDomain(String domainId) {
        Iterator it = Config.domainList.iterator();
        Domain domain = null;
        while (it.hasNext()) {
            domain = (Domain) it.next();
            if (domainId.equals(domain.getId())) {
                return domain;
            }
        }
        return domain;
    }

    public static List<String> getResultFields(Domain domain) {
        List<ResultField> fieldList = domain.getResultFieldList().getResultField();
        Iterator it = fieldList.iterator();
        List<String> resultRefFields = new ArrayList<String>();
        while (it.hasNext()) {
            ResultField field = (ResultField) it.next();
            resultRefFields.add(field.getId());
        }
        return resultRefFields;
    }

    public static void main(String[] args) throws EnzymeFinderException, InterruptedException, ExecutionException, JAXBException, IOException {
        //System.out.print(idList);
        Runtime runtime = Runtime.getRuntime();
        System.out.println("max: " +runtime.maxMemory()/1024/1024);
        System.out.println("allocated memory: " + runtime.totalMemory() / 1024/1024);
        cacheEnzymesInUniprot();
        /*
        String[] queryArray = {
                    "EC:(1 OR 1.1* OR 1.2* OR 1.3*)"
        };

        List<ParamGetNumberOfResults> queryList = new
        ArrayList<ParamGetNumberOfResults>();
        for (String query: queryArray) {
        ParamGetNumberOfResults param = new ParamGetNumberOfResults(
        EnzymeFinder.UNIPROT_DOMAIN, query);
        queryList.add(param);
        }
        IEnzymeFinder finder = new EnzymeFinder();
        List<ResultOfGetNumberOfResults> nrOfResultsList =
        finder.getNumberOfResults(queryList);
        List<ResultOfGetResultsIds> resultList = finder.getResultsIds(nrOfResultsList);
        List<String> idList = DataTypeConverter.getResultsIds(resultList);
        System.out.print(idList.size());
*/
    }

    public static void cacheEnzymesInUniprot() throws InterruptedException, ExecutionException, JAXBException {
        testEbeyeAdapter();
        //testUniprot();
    }

    public static void testOldCode() throws MultiThreadingException, EnzymeFinderException {
        String[] queryArray = {
            "EC:(1 OR 1.1* OR 1.2* OR 1.3*)"                            
            /*
                    "EC:(1 OR 1.1* OR 1.2* OR 1.3*)"                            
                    , "EC:(1.4* OR 1.5* OR 1.6* OR 1.7* OR 1.8*)"
                    , "EC:(1.9*)"
                    , "EC:(2 OR 2.1* OR 2.2* OR 2.3* OR 2.4*)"
                    , "EC:(2.5* OR 2.6* OR 2.8* OR 2.9*)"
                    , "EC:(2.7* NOT 2.7.7*)"
                    , "EC:(2.7.7* OR 3 OR 3.1* OR 3.2*)"
                    , "EC:(3.3* OR 3.4* OR 3.5* OR 3.6* OR 3.7* OR 3.8* OR 3.9*)"
                    , "EC:(4*)"
                    , "EC:(5*)"
                    , "EC:(6* OR 7* OR 8* OR 9*)"      
                             * *
                             */
        };
        
        List<ParamGetNumberOfResults> queryList = new
        ArrayList<ParamGetNumberOfResults>();
        for (String query: queryArray) {
        ParamGetNumberOfResults param = new ParamGetNumberOfResults(
        EnzymeFinder.UNIPROT_DOMAIN, query);
        queryList.add(param);
        }
        IEnzymeFinder finder = new EnzymeFinder();
        List<ResultOfGetNumberOfResults> nrOfResultsList =
        finder.getNumberOfResults(queryList);
        List<ResultOfGetResultsIds> resultList = finder.getResultsIds(nrOfResultsList);
        List<String> idList = DataTypeConverter.getResultsIds(resultList);
        
    }
    public static void testEbeyeAdapter() throws InterruptedException, ExecutionException, JAXBException {
        List<String> queryList = buildEcQuery();
        List<String> fields = new ArrayList<String>();
        fields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
        fields.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());        
        List<ParamOfGetAllResults> paramList = new ArrayList<ParamOfGetAllResults>();
        Iterator itQ = queryList.iterator();
        while (itQ.hasNext()) {
            String query = (String)itQ.next();
            ParamOfGetAllResults param = new ParamOfGetAllResults(
                    IEbeyeAdapter.Domains.uniprot.name(), query, fields);
            paramList.add(param);

        }
       IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapterCache();

        Map<String, List<Result>> results =
                ebeyeAdapter.getAllDomainsResults(paramList);
        Iterator it = results.keySet().iterator();
        ResultCollection resultCollection = new ResultCollection();

        while (it.hasNext()) {
            List<Result> domainResult = (List<Result>)results.get((String)it.next());
            resultCollection.getResultlist().addAll(domainResult);
            //mergedResults.addAll(domainResult);
            //enzymesInUniprot.addAll(domainResult);
            //processResults(domainResult);

        }
        marshalResultToXml(resultCollection, cacheFile);
        System.out.println(resultCollection.getResultlist().size());

    }
    public static void testUniprot() {
        UniProtQueryService queryService = UniProtJAPI.factory.getUniProtQueryService();
                Query query1 = UniProtQueryBuilder.buildECNumberQuery("1.1.1.1*");
                EntryIterator<UniProtEntry> entries = queryService.getEntryIterator(query1);
                System.out.println("Number of entries with EC = 1.1.1.1* = " + entries.getResultSize());
		//System.out.println("Number of entries with protein name Clpl = " + entries.getResultSize());
                ResultCollection resultCollection = new ResultCollection();
                Result result = new UniprotResult();
		for (UniProtEntry entry : entries) {
                    result.setId(entry.getUniProtId().getValue());
                    result.getAcc().add(entry.getPrimaryUniProtAccession().getValue());
                    resultCollection.getResultlist().add(result);
			//System.out.println("entry.getPrimaryUniProtAccession() = " + entry.getPrimaryUniProtAccession());
		}
                System.out.println(resultCollection.getResultlist());


    }
    /*
    public static void processResults(List<Result> resultList) {
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            Result resultObj = (Result) it.next();
            if (resultObj instanceof UniprotResult) {
                UniprotResult uniprotResult = (UniprotResult) resultObj;
                enzymesInUniprot.add(uniprotResult);
                enzymeUniprotIds.add(uniprotResult.getId());
                enzymeUniprotAccs.addAll(uniprotResult.getAcc());
            }
        }

    }
     * 
     */

    public static List<String> buildEcQuery() throws InterruptedException, ExecutionException {
        int limit = 5;
        int start = 4;
        List<String> queryList = new ArrayList<String>();
        while (start < limit) {
            StringBuffer sb = new StringBuffer();
            sb.append("EC:(");
            sb.append(start);
            sb.append(")");
            queryList.add(sb.toString());
            buildWildcardQuery(String.valueOf(start),queryList);
            start++;
        }
        System.out.println(queryList.get(0));
        return queryList;
    }

    public static void buildWildcardQuery(String ecClass, List<String> queryList) {
            int level1Start = 1;
            int level1Limit = 10;
            while (level1Start < level1Limit) {
                StringBuffer sb1 = new StringBuffer();
                int ecSubclass = Integer.valueOf(level1Start);
                sb1.append("EC:(");
                sb1.append(ecClass);
                sb1.append(".");
                sb1.append(ecSubclass);
                sb1.append("*");
                sb1.append(")");
                String query =sb1.toString();
                if (query.equals("EC:(2.7*)")){
                    queryList.add("EC:(2.7)");
                    buildWildcardQuery("2.7", queryList);
                }
                else {
                    queryList.add(query);
                }
                level1Start++;
            }

    }
}
