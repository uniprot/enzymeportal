package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.config.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.config.jaxb.Domain;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.InvalidSearchException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummaryCollection;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ep.util.validation.Validator;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.ebeye.result.jaxb.Xref;
import uk.ac.ebi.ep.search.result.jaxb.Compound;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeAccession;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.ep.search.result.jaxb.SearchFilter;
import uk.ac.ebi.ep.search.result.jaxb.Species;
import uk.ac.ebi.ep.uniprot.adapter.IUniprotAdapter;
import uk.ac.ebi.ep.uniprot.adapter.UniprotAdapter;
import uk.ac.ebi.util.result.DataTypeConverter;


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
    protected SearchParams searchParams;   
    private static Logger log = Logger.getLogger(EnzymeFinder.class);

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() {

    }


//****************************** GETTER & SETTER *****************************//
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }


//********************************** METHODS *********************************//

    public List<String> rankEnzymes(List<String> uniprotIdList) {
        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> rankedResults = resultRanker.getRankedResults();
        return rankedResults;
    }

    public Set<String> getUniprotPrimaAccList(
            List<Result> cachedResults, List<String> ids) {
        Set<String> accessions = new TreeSet<String>();
        for (String id:ids) {
            for (Result result:cachedResults) {
                String selectedId = result.getId();
                if (selectedId.equals(id)) {
                    accessions.add(result.getAcc().get(0));
                    break;
                }
            }
        }
        return accessions;
    }

    public ParamOfGetResults getDomainParam(List<ParamOfGetResults> params, String domain) {
        for (ParamOfGetResults param:params) {
            if (param.getDomain().equals(domain)) {
                return param;
            }
        }
        return null;
    }

    public List<ParamOfGetResults> getNumberOfResult() throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        List<String> fieldNames = new ArrayList<String>();
        FieldsOfGetResults[] fields = IEbeyeAdapter.FieldsOfGetResults.values();
        for (FieldsOfGetResults field:fields) {
            String fieldName = field.name();
            fieldNames.add(fieldName);
        }


        //prepare list of parameters
        List<ParamOfGetResults> paramOfGetAllResults =
                                    prepareParamOfGetAllResults(searchParams, fieldNames);
        try {
            //List<List<Result>> allDomainsResults = null;
            //get and set the value at the same time
            ebeyeAdapter.getNumberOfResults(paramOfGetAllResults);
        } catch (MultiThreadingException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Process uniprot separately
        /*
        ParamOfGetResults uniprotParam = getDomainParam(
                    paramOfGetAllResults, IEbeyeAdapter.Domains.uniprot.name());
        paramOfGetAllResults.remove(uniprotParam);
        queryResultsForAllDomains(paramOfGetAllResults);
         *
         */
            //Other domain number of result
          //Filter out non enzyme
            //get number of results that are enzyme only
            //create distributed queries for the first 100
            //increase 10 for each page
       return paramOfGetAllResults;

    }

/*
    public  Map<String, List<Result>> queryResultsForAllDomains(
            List<ParamOfGetResults> paramOfGetAllResults) throws EnzymeFinderException {
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        Map<String, List<Result>> allDomainsResults;
        try {
            allDomainsResults = ebeyeAdapter.getMultiDomainsResults(paramOfGetAllResults);
        } catch (MultiThreadingException ex) {
            throw new EnzymeFinderException("Find enzymes process failed! ", ex);
        }

        return allDomainsResults;
    }
 * 
 */

    public void validateSearchInput(String userKeywords) throws InvalidSearchException {
        if (!Validator.isSearchParamsOK(userKeywords)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at leasr one keyword!");
        }
        LuceneParser luceneParser = new LuceneParser();        
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(userKeywords);
        this.searchParams.setKeywords(cleanedKeywords);
    }

    public String getIdPrefix(String id) {
       String[] idSplit = id.split("_");
       String idPrefix = idSplit[0];
       return idPrefix;
    }

    public Set<String> getIdPrefixes(List<Result> results) {
        Set<String> idPrefixes = new LinkedHashSet<String>();
       for (Result result:results) {
           String id = result.getId();
           String idPrefix = getIdPrefix(id);
           idPrefixes.add(idPrefix);
       }
       return idPrefixes;
    }


    public Map<String, List<Result>> groupResultsBySpecies(Set<String> idPrefixes, List<Result> results) {
        Map<String, List<Result>> resultsGroupedBySpecies = new HashMap<String, List<Result>>();
        for (String idPrefix:idPrefixes) {
            List<Result> resultsSubList = new ArrayList<Result>();
           for (Result result:results) {
               String idPrefixFromResult = getIdPrefix(result.getId());
               if (idPrefix.equals(idPrefixFromResult)) {
                   resultsSubList.add(result);
               }

           }
           resultsGroupedBySpecies.put(idPrefix, resultsSubList);

        }
       return resultsGroupedBySpecies;
    }

   public Map<String, List<Result>> groupResultsBySpecies(Map<String, Result> resultsMap) {
       Iterator it = resultsMap.keySet().iterator();
       Map<String, List<Result>> resultsGroupedBySpecies = new HashMap<String, List<Result>>();
       while (it.hasNext()) {
           String id = (String)it.next();
           Result result = resultsMap.get(id);
           String idPrefix = this.getIdPrefix(id);
           List<Result> results = resultsGroupedBySpecies.get(idPrefix);
           if (results == null) {
               results = new ArrayList<Result>();
           }
           results.add(result);
           resultsGroupedBySpecies.put(idPrefix, results);
           }
       return resultsGroupedBySpecies;
   }



      public Map<String, Result> removeDuplications(List<Result> results) {
            Map<String, Result> resultsGroupedBySpecies = new HashMap<String, Result>();
                //List<Result> resultsSubList = new ArrayList<Result>();
               for (Result result:results) {
                   String id = result.getId();
                   resultsGroupedBySpecies.put(id, result);
                   /*
                   if (resultsGroupedBySpecies.containsKey(id)) {
                       List<Result> resultList = resultsGroupedBySpecies.get(id);
                       resultList.add(result);
                   }
                   else {
                       List<Result> resultList = new ArrayList<Result>();
                       resultList.add(result);
                       resultsGroupedBySpecies.put(id, resultsSubList);
                   }
                    *
                    */
            }
           return resultsGroupedBySpecies;
        }

      public Map<String, List<Result>> subMap(List<String> keys, Map<String, List<Result>> resultGroupedBySpecies) {
          Iterator it = keys.iterator();
          Map<String, List<Result>> subMap = new HashMap<String, List<Result>>();
          while (it.hasNext()) {
              String idPrefix = (String)it.next();
              List<Result> resultlist = resultGroupedBySpecies.get(idPrefix);
              subMap.put(idPrefix, resultlist);
          }
          return subMap;
      }


    public EnzymeSearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {
        //setting variable values and validation
        this.searchParams = searchParams;
        String userKeywords = this.searchParams.getKeywords();
        validateSearchInput(userKeywords);
        EnzymeSearchResults enzymeSearchResults = createEmptyResponse();

        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        //Query the number of results for all domains and save the value in ParamOfGetResults object
        List<ParamOfGetResults> nrOfResultParams = this.getNumberOfResult();

        //Process uniprot separately
        ParamOfGetResults uniprotParam = getDomainParam(
                    nrOfResultParams, IEbeyeAdapter.Domains.uniprot.name());
        nrOfResultParams.remove(uniprotParam);

        //Query uniprot results
        List<Result> uniprotResults = new ArrayList<Result>();
        uniprotResults.addAll(ebeyeAdapter.getResults(uniprotParam));

        //Save uniprot accession separately
        Set<String> accessionsFromUniprot = this.getUniprotPrimAcc(uniprotResults);

        //Query results for other domain
        Map<String, List<Result>> allDomainsResults = ebeyeAdapter
                .getUniprotResults(nrOfResultParams);
        
        //Save uniprot accessions resulted from other domains
        Set<String> accessionsFromOtherDomains = this.getUniprotPrimAcc(allDomainsResults);

        //Save chebi ids
        String chebiDomain = IEbeyeAdapter.Domains.chebi.name();
        List<Result> chebiResults =  allDomainsResults.get(chebiDomain);
        List<String> chebiAccs = this.getResultsXrefsList(chebiResults);

        //Save pdbE results

        //List<Result> pdbeResults = allDomainsResults.get(IEbeyeAdapter.Domains.pdbe.name());

        

        SearchFilter searchFilter = new SearchFilter();
        

        Map<String,String> chebiNames = ebeyeAdapter.getNames(chebiDomain, chebiAccs);
        List<Compound> compList = DataTypeConverter.mapToCompound(chebiNames);
        searchFilter.getCompounds().addAll(compList);
        //searchFilter.getCompounds().addAll(chebiAccs);
        enzymeSearchResults.setSearchfilter(searchFilter);

        int nrOfResultFromOtherDomains = accessionsFromOtherDomains.size();
        List<Result> queryResults = null;
        if (nrOfResultFromOtherDomains > 0) {
            //Remove all entries that are in the uniprot result
            //from accessionsFromOtherDomains to avoid duplication
            accessionsFromOtherDomains.removeAll(accessionsFromUniprot);
            List<String> accessionsFromOtherDomainsList = new ArrayList<String>();
            accessionsFromOtherDomainsList.addAll(accessionsFromOtherDomains);
            //IEbeyeAdapter adapter = new EbeyeAdapter();

            try {
                queryResults = ebeyeAdapter.getResultsByAccessions(
                        IEbeyeAdapter.Domains.uniprot.name(), accessionsFromOtherDomainsList);
            } catch (MultiThreadingException ex) {
                //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //TODO
        //Set<String> ids = getUniprotIds(allDomainsResults);
        //Set<String> uniprotIdsFromOtherDomains = getIds(queryResults);
        //ids.addAll(uniprotIds);
        //int nrOfResultFromUniprot = accessionsFromUniprot.size();
        
        uniprotResults.addAll(queryResults);

        //Set<String> idPrefixes = getIdPrefixes(uniprotResults);
        //Map<String, Result> uniqueResults = this.removeDuplications(uniprotResults);
        
        


        //Map<String,List<Result>> resultsGroupedBySpecies = this
           //     .groupResultsBySpecies(uniqueResults);

        List<String> idPrefixes = new ArrayList<String>();
        idPrefixes.addAll(this.getIdPrefixes(uniprotResults));

        //int totalGroupedBySpecies = resultsGroupedBySpecies.size();

        //get data for the list of top ranked ids
        EnzymeSummaryCollection searchResults = new EnzymeSummaryCollection();


        //Process the pagination
        //int totalFound = uniprotResults.size();
        int totalFound = idPrefixes.size();
        int size = searchParams.getSize();
        int start =  searchParams.getStart();
        int subListIndex = start+size;
        if (totalFound<subListIndex) {
            subListIndex=totalFound;
        }
        List<String> idPrefixesSubList = idPrefixes.subList(
               start, subListIndex);
        //Map<String, List<Result>>  briefResultSubset =
           //     subMap(keySubset, resultsGroupedBySpecies);

        //TODO

                /*
        List<Result> briefResultSubset = uniprotResults.subList(
               start, subListIndex);
        */
        searchResults.setTotalfound(totalFound);


        searchResults.setResultstartat(searchParams.getStart());
        
        //this.getUniprotResults(topRankedAccessions);
        
        IUniprotAdapter uniprotAdapter = new UniprotAdapter();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        //enzymeSummaryList = uniprotAdapter.getEnzymeEntries(briefResultSubset);
        List<String> queries = LuceneQueryBuilder.createUniprotQueryByIdPrefixes(idPrefixesSubList);
        enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);

        //setPdbeAccession(enzymeSummaryList, pdbeResults);
        //resultsize
        List<String> uniprotIds = getUniprotIds(enzymeSummaryList);
        Map<String,String> pdbeAccs =
        ebeyeAdapter.getUniprotXrefs(uniprotIds, IEbeyeAdapter.Domains.pdbe.name());

        setPdbeAccession(enzymeSummaryList, pdbeAccs);
        searchResults.setResultsize(enzymeSummaryList.size());

        searchResults.getEnzymesummary().addAll(enzymeSummaryList);

        //EnzymeSummary enzymeSummary = searchResults.getEnzymesummary().get(0);
        //enzymeSummary.

        enzymeSearchResults.setEnzymesummarycollection(searchResults);

        createSpeciesFilter(enzymeSearchResults);

        searchParams.setKeywords(userKeywords);

        return enzymeSearchResults;

    }
    public void setPdbeAccession(List<EnzymeSummary> enzymeSummaryList
            ,  Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            String pdbeAcc = pdbeAccs.get(uniprotId);
            enzymeSummary.getPdbeaccession().add(pdbeAcc);
        }       
    }

/*

    public void setPdbeAccession(List<EnzymeSummary> enzymeSummaryList
            , List<Result> pdbeResults) {
        Set<String> uniprotIds = getUniprotAccs(enzymeSummaryList);
        //Set<String> pdbeAccs = getResultsXrefs(pdbeResults);
        //Map<String, String> uniprotAndPdbeIdsMap = new Hashtable<String, String>();
        //getResultsXrefs(pdbeResults);
        for (Result pdbeResult:pdbeResults) {
            String pdbeResultUniprotAcc = pdbeResult.getAcc().get(0);
            for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
                String uniprotAcc = enzymeSummary.getUniprotaccessions().get(0);
                if (pdbeResultUniprotAcc.equals(uniprotAcc)) {
                    String pdbeId = pdbeResult.getXrefs().get(0).getAcc().get(0);
                    enzymeSummary.getPdbeaccession().add(pdbeId);
                }
            }
        }

        System.out.print(uniprotIds);
    }
*/
   public List<String> getUniprotIds(List<EnzymeSummary> enzymeSummaryList) {
        List<String> uniprotIds = new ArrayList<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            uniprotIds.add(uniprotId);
        }
        return uniprotIds;
    }
    public Set<String> getUniprotAccs(List<EnzymeSummary> enzymeSummaryList) {
        Set<String> uniprotAccs = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotAcc = enzymeSummary.getUniprotaccessions().get(0);
            uniprotAccs.add(uniprotAcc);
        }
        return uniprotAccs;
    }
    public void createSpeciesFilter(EnzymeSearchResults enzymeSearchResults) {
        List<EnzymeSummary> enzymeSummaryList = enzymeSearchResults
                .getEnzymesummarycollection().getEnzymesummary();
        List<Species> speciesFilter = enzymeSearchResults.getSearchfilter().getSpecies();
        Set<String> speciesNames = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary: enzymeSummaryList) {
            Species species = enzymeSummary.getSpecies();
            String name = species.getCommonname();
            if (name == null || name.equals("")) {
                name = species.getScientificname();
            }
            boolean added = false;
            if (name != null) {
                added = speciesNames.add(name);
            }

            if (added) {
                speciesFilter.add(species);
            }

            List<EnzymeAccession> enzymeAccessions = enzymeSummary.getRelatedspecies();
            for (EnzymeAccession acc: enzymeAccessions) {
                Species relSpecies = acc.getSpecies();
                String relName = relSpecies.getCommonname();
                if (relName == null) {
                    relName = relSpecies.getScientificname();
                }
                boolean relAdded = speciesNames.add(relName);
                if (relAdded) {
                    speciesFilter.add(relSpecies);
                }
            }
        }

        System.out.print(speciesFilter);

    }


    public static List<ParamOfGetResults>  prepareParamOfGetAllResults(
            SearchParams searchParams, List<String> resultFields) {
        Iterator<Domain> it = Config.domainList.iterator();
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            String query = LuceneQueryBuilder.createQueryOR(domain, searchParams);
            String domainId = domain.getId();
            //List<String> fields = DataTypeConverter.getConfigResultFields(domain);
            ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(domainId, query, resultFields);
            paramList.add(paramOfGetAllResults);
        }
        return paramList;
    }
/*
    public List<Result> getUniprotResults(Set<String> accessions) {
        Iterator accessionsIt = accessions.iterator();
        List<Result> uniprotResults = new ArrayList<Result>();
        while (accessionsIt.hasNext()) {
            String acc = (String)accessionsIt.next();
            if (acc!= null) {
                if (!acc.equals("")){
                    Result result = getUniprotResult(acc);
                    if (result !=null) {
                        uniprotResults.add(result);
                    }
                    
                }
            }
            
        }
        return uniprotResults;
    }


    public Result getUniprotResult(String accession) {
        List<Result> accConfigList = Config.enzymesInUniprot;
        Iterator accConfigIt = accConfigList.iterator();
        Result uniprotResult = null;
        while (accConfigIt.hasNext()) {
            uniprotResult = (Result)accConfigIt.next();
            if (uniprotResult.getAcc().contains(accession)) {
                return uniprotResult;
            }
        }
        return uniprotResult;
    }
    */    /*

    public List<Result> getUniprotResult(List<Xref> xRefList) {
        List<Result> results = new ArrayList<Result>();
        Iterator it = xRefList.iterator();
        Set<String> uniprotAccList = new TreeSet<String>();
        while (it.hasNext()) {
               Xref uniprotXref = (Xref)it.next();
               List<String> uniprotXrefAccs = uniprotXref.getAcc();
               for (String accession: uniprotXrefAccs) {
                   uniprotAccList.add(accession);
               }
               //results.addAll( getUniprotResults(uniprotXrefAccs));
        }
        results.addAll(getUniprotResults(uniprotAccList));
        return results;
    }

    public List<Result> getEnzymeUniprot(List<Result> resultList) {
        List<Result> filteredResults = new ArrayList<Result>();
        if (resultList.size() != 0) {
            Iterator it1 = resultList.iterator();
            while (it1.hasNext()) {
                Result resultObj = (Result) it1.next();

                //Uniprot Xref
                List<Xref> accList = resultObj.getXrefs();
                filteredResults.addAll(getUniprotResult(accList));
            }
        }
        return filteredResults;
    }
*/
    public List<String> getResultsXrefsList(List<Result> resultList) {
        List<String> list = new ArrayList<String>();
        list.addAll(getResultsXrefs(resultList));
        return list;
    }
    public Set<String> getResultsXrefs(List<Result> resultList) {
        Set<String> xrefAccList = new TreeSet<String>();
        if (resultList.size() != 0) {
            Iterator it1 = resultList.iterator();
            while (it1.hasNext()) {
                Result resultObj = (Result) it1.next();

                //Uniprot Xref
                List<Xref> xRefList = resultObj.getXrefs();
                for (Xref xRef: xRefList) {
                    List<String> accList = xRef.getAcc();
                    if (accList!=null) {
                        if (accList.size()>0) {
                            for (String acc:accList) {
                                xrefAccList.add(acc);
                            }                        
                        }
                    }
                }
                //filteredResults.addAll(getUniprotResult(accList));
            }
        }
        return xrefAccList;
    }
/*
    public static boolean isEnzyme(String acc) {
        boolean isEnzyme = false;
        List<String> cachedAcc = Config.enzymeUniprotAccs;
        int index = Collections.binarySearch(cachedAcc, acc);
        if (index > -1) {
            isEnzyme = true;
        }
        return isEnzyme;
    }
*/
    //Results from more than one domains
/*
    public List<Result> mergeUniprotResult(List<Result> resultList1,List<Result> resultList2) {
        List<Result> mergedList = new ArrayList<Result>();
        if (resultList1.size()>resultList2.size()) {
            mergedList.addAll(resultList1);
            mergedList.removeAll(resultList2);
        }
        else {
            mergedList.addAll(resultList2);
            mergedList.removeAll(resultList1);

        }
        return mergedList; 
    }
    */


    public Result getResultFromAcc(List<Result> uniprotResults, String acc) {
        for (Result result: uniprotResults ) {
            List<String> accs = result.getAcc();
            if (accs.contains(acc)) {
                return  result;
            }
        }
        return null;
    }
/*
    public List<Result> saveXrefsFromUniToOthers(List<Result> uniprotResults
            , Map<String,List<Result>> results) {
        List<Result> intersecResults = new ArrayList<Result>();
                Set<String> domains = results.keySet();
                Iterator it = domains.iterator();
                //for each domain
                while (it.hasNext()) {
                    String domain = (String)it.next();
                    List<Result> resultsFromOtherDomains = results.get(domain);
                    List<Integer> resultsFromOtherDomainsIndex = new
                            ArrayList<Integer>();
                    //for each result of a domain
                     for (Result result: resultsFromOtherDomains ) {
                            List<Xref> xrefs = result.getXrefs();
                            for (Xref xref: xrefs ) {
                                List<String> uniprotXrefAccs = xref.getAcc();
                                for (String uniAcc: uniprotXrefAccs) {
                                    Result uniResult = getResultFromAcc(uniprotResults,uniAcc);
                                    if (uniResult !=null) {
                                        //int indexOfUni = uniprotResults.indexOf(uniResult);
                                        Xref uniXref = new Xref();
                                        uniXref.setDomain(domain);
                                        uniXref.getAcc().addAll(result.getAcc());
                                        uniResult.getXrefs().add(xref);
                                        intersecResults.add(uniResult);
                                        resultsFromOtherDomainsIndex.add(
                                                resultsFromOtherDomains.indexOf(result));

                                    }
                                }

                            }
                     }
                     uniprotResults.removeAll(intersecResults);
                     for (int index:resultsFromOtherDomainsIndex) {
                         resultsFromOtherDomains.remove(index);
                     }
                     
        }
        return intersecResults;
    }
    */
    public List<Result> saveXrefsFromUniToOthers(List<Result> uniprotResults
            , Map<String,List<Result>> results) {
        List<Result> intersecResults = new ArrayList<Result>();
                Set<String> domains = results.keySet();
                Iterator it = domains.iterator();
                //for each domain
                while (it.hasNext()) {
                    String domain = (String)it.next();
                    List<Result> resultsFromOtherDomains = results.get(domain);
                    List<Integer> resultsFromOtherDomainsIndex = new
                            ArrayList<Integer>();
                    //for each result of a domain
                     for (Result result: resultsFromOtherDomains ) {
                         List<String> accs = result.getAcc();
                         String primaAcc = accs.get(0);
                          Result uniResult = getResultFromAcc(uniprotResults,primaAcc);
                          if (uniResult !=null) {
                              uniResult.getXrefs().addAll(result.getXrefs());
                               intersecResults.add(uniResult);
                                resultsFromOtherDomainsIndex.add(
                                        resultsFromOtherDomains.indexOf(result));
                          }
                     }
                     uniprotResults.removeAll(intersecResults);
                     for (int index:resultsFromOtherDomainsIndex) {
                         resultsFromOtherDomains.remove(index);
                     }

        }
        return intersecResults;
    }
    public List<Result> filterNonEnzymes(Map<String,List<Result>> results) {
        //for domains other than uniprot, get the accession numbers
        Set<String> uniprotXrefAccs = getUniprotPrimAcc(results);
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();
        List<Result> uniprotResults = results.get(uniprotDomain);
        results.remove(uniprotDomain);
        //results: non uniprot, uniprotResults: uniprot
        List<Result> queryResults = null;
        //uniprotResults and uniprotXrefAccs accession in common are removed
        // and added to the mergeResult
        List<Result> mergedResults = new ArrayList<Result>();
        List<Result> intersecResult = saveXrefsFromUniToOthers(uniprotResults,results);

        //TODO: ref to other domains must be kept

        //TODO: call EBEYE
        /*
        if (uniprotXrefAccs.size() > 0) {
            List<String> uniprotXrefAccList = new ArrayList<String>();
            uniprotXrefAccList.addAll(uniprotXrefAccs);
            List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
            List<String> fields = new ArrayList<String>();
            fields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
            fields.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());
            int endIndex =0;
            int total = uniprotXrefAccs.size();
            //Work around to solve big result set issue
            if (total > IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT) {
                total = IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT;
            }
            Pagination pagination = new Pagination(total, IEbeyeAdapter.EBEYE_RESULT_LIMIT);
            int nrOfQueries = pagination.calTotalPages();
            int start = 0;
            for (int i = 0; i < nrOfQueries; i++) {
                if (i == nrOfQueries-1 && (total% IEbeyeAdapter.EBEYE_RESULT_LIMIT)>0) {
                    endIndex = endIndex + pagination.getLastPageResults();
                }
                else {
                    endIndex = endIndex + IEbeyeAdapter.EBEYE_RESULT_LIMIT;
                }
            String query = LuceneQueryBuilder
                    .createUniprotQueryForEnzyme(uniprotXrefAccList.subList(start, endIndex));
                        ParamOfGetResults paramOfGetAllResults =
                        new ParamOfGetResults(uniprotDomain, query, fields);
                        params.add(paramOfGetAllResults);
                start = endIndex;
            }

*/
        if (uniprotXrefAccs.size() > 0) {
            List<String> uniprotXrefAccList = new ArrayList<String>();
            uniprotXrefAccList.addAll(uniprotXrefAccs);
            IEbeyeAdapter adapter = new EbeyeAdapter();

            try {
                queryResults = adapter.getResultsByAccessions(
                        IEbeyeAdapter.Domains.uniprot.name(), uniprotXrefAccList);
            } catch (MultiThreadingException ex) {
                //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //List<Result> mergedResult = null;
        if (queryResults != null) {
            if (uniprotResults != null) {
                //uniprotResults.addAll(queryResults);
                //TODO
                mergedResults.addAll(uniprotResults);
                mergedResults.addAll(intersecResult);
                mergedResults.addAll(queryResults);
                //mergeResult = mergeUniprotResult(uniprotResults,queryResults);
            }
            else {
                mergedResults = queryResults;
            }
        }
        


        //iterate thru each domain
        //List<Result> filteredResults = getUniprotResults(mergedUniprotAccs);

        //return filteredResults;
        return mergedResults;
    }

    public Set<String> getUniprotPrimAcc(List<Result> resultList) {
        Set<String> accList = new TreeSet<String>();
        for (Result result:resultList) {
            List<String> accs = result.getAcc();
            if (accs.size()>0) {
                //String acc = accs.get(0);
                accList.addAll(accs);
            }
        }
        return accList;
    }

    /*
    public Set<String> getAllUniprotXrefs(Map<String,List<Result>> results) {
        Iterator it = results.keySet().iterator();
        Set<String> uniprotAccList = new TreeSet<String>();
        while (it.hasNext()) {
            String key = (String)it.next();
            if (!key.equals(IEbeyeAdapter.Domains.uniprot.name())) {
                List<Result> resultList = (List<Result>)results.get(key);
                    Set<String> accList = getUniprotXrefs(resultList);
                     if (accList.size() > 0 ) {
                         uniprotAccList.addAll(accList);
                     }

            }
        }
        return uniprotAccList;
    }
    */
    public Set<String> getUniprotPrimAcc(Map<String,List<Result>> results) {
        Iterator it = results.keySet().iterator();
        Set<String> uniprotAccList = new TreeSet<String>();
        while (it.hasNext()) {
            String key = (String)it.next();
            List<Result> resultList = (List<Result>)results.get(key);
            uniprotAccList.addAll(this.getUniprotPrimAcc(resultList));
        }
        return uniprotAccList;
    }

    public Set<String> getIds(List<Result> results) {
        Set<String> ids = new LinkedHashSet<String>();
        for (Result result:results) {
            ids.add(result.getId());
        }
        return ids;
    }

/*
    public Map<String, Result> getUniAccFromXrefs(Map<String,List<Result>> results) {
        Iterator it = results.keySet().iterator();
        Map<String,Result> uniprotAccs = new Hashtable<String, Result>();
        while (it.hasNext()) {
            String key = (String)it.next();
            if (!key.equals(IEbeyeAdapter.Domains.uniprot.name())) {
                List<Result> resultList = (List<Result>)results.get(key);
                for (Result result:resultList) {
                    List<Xref> xrefs = result.getXrefs();
                    for (Xref xref:xrefs) {
                        List<String> accs = xref.getAcc();
                        for (String acc: accs) {
                            uniprotAccs.put(acc, result);
                        }
                    }
                }

            }
        }
        return uniprotAccs;
    }
*/
    public EnzymeSearchResults createEmptyResponse() {
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        EnzymeSummaryCollection enzymeSummaryCollection = new EnzymeSummaryCollection();
        enzymeSummaryCollection.setTotalfound(0);
        enzymeSearchResults.setEnzymesummarycollection(enzymeSummaryCollection);
        return enzymeSearchResults;
    }
    
    public List<String>  getTopRankedResults(
                 List<String> rankedResults, int start, int size) {
        List<String> topResults = null;
        int rankedResultsSize = rankedResults.size();
        if (rankedResults !=null && rankedResultsSize>size) {
            int endIndex = start+size;
            //To avoid OutOfBoundIndex exception
            if (endIndex > rankedResultsSize) {
                endIndex = rankedResultsSize;
            }
            topResults = rankedResults.subList(start, endIndex);
        }
        else {
            topResults = rankedResults;
        }
        return topResults;

    }

  }
