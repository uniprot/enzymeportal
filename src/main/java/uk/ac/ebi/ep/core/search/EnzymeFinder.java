package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.InvalidSearchException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ep.util.validation.Validator;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeSearch;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.ebeye.result.Result;
import uk.ac.ebi.ep.ebeye.result.Xref;
import uk.ac.ebi.ep.intenz.adapter.IintenzAdapter;
import uk.ac.ebi.ep.intenz.adapter.IntenzAdapter;
import uk.ac.ebi.ep.search.adapter.ISearchAdapter;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchFilters;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.search.result.Pagination;
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

    public List<ParamOfGetResults> getNrOfRecordsRelatedToUniprot() throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        //prepare list of parameters
        List<ParamOfGetResults> paramsWithoutNrOfResults =
                                    prepareGetRelatedRecordsToUniprotQueries(searchParams);
       List<ParamOfGetResults> paramsWithNrOfResults = new ArrayList<ParamOfGetResults>();
        try {
            //List<List<Result>> allDomainsResults = null;
            //get and set the value at the same time
            paramsWithNrOfResults.addAll(ebeyeAdapter.getNumberOfResults(paramsWithoutNrOfResults));
        } catch (MultiThreadingException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
       return paramsWithoutNrOfResults;
    }

    public ParamOfGetResults getUniprotNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareGetUniprotIdQueries(searchParams);
        ebeyeAdapter.getNumberOfResults(param);
       return param;
    }

    public List<ParamOfGetResults> createSubQueries(
            String domain, List<String> fieldValues) throws EnzymeFinderException {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        int listSize = fieldValues.size();
        int endIndex = 0;
        //Work around to solve big result set issue
        Pagination pagination = new Pagination(listSize, IEbeyeAdapter.EBEYE_NR_OF_ACC_LIMIT);
        int nrOfQueries = pagination.calTotalPages();
        int start = 0;
        //TODO
        for (int i = 0; i < nrOfQueries; i++) {            
            if (i == (nrOfQueries - 1) && (listSize % IEbeyeAdapter.EBEYE_NR_OF_ACC_LIMIT) > 0) {
                endIndex = endIndex + pagination.getLastPageResults();
            } else {
                endIndex = endIndex + IEbeyeAdapter.EBEYE_NR_OF_ACC_LIMIT;
            }

            List<String> subList = fieldValues.subList(start, endIndex);
            
             String query = LuceneQueryBuilder.createQueryIN(
                IEbeyeAdapter.FieldsOfGetResults.acc.name(), false, subList);
            ParamOfGetResults param = createParamOfGetResults(domain, query, subList);
            paramList.add(param);
            start = endIndex;
        }
        return paramList;
    }

    public ParamOfGetResults getNrOfRecords(String domain, Collection<String> accessions) throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        String query = LuceneQueryBuilder.createUniprotQueryForEnzyme (accessions, null);
        ParamOfGetResults param = createParamOfGetResults(domain, query, accessions);
        ebeyeAdapter.getNumberOfResults(param);
       return param;
    }

    public ParamOfGetResults getNrOfRecordsWithFilter(String domain, Collection<String> queryValues) throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        String query = LuceneQueryBuilder.createIdSuffixWildcardQuery(
                queryValues, searchParams.getSpecies());
        ParamOfGetResults param =
                createParamOfGetResults(domain, query, queryValues);
        ebeyeAdapter.getNumberOfResults(param);
       return param;
    }

    public ParamOfGetResults createParamOfGetResults(String domain, String query, Collection<String> queryValues) throws EnzymeFinderException {
        //Uniprot number of result
        //String query = LuceneQueryBuilder.createUniprotQueryForEnzyme(queryValues, null);

        List<String> resultFields = new ArrayList<String>();
        //resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.descRecName.name());
        resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.id.name());
        //prepare list of parameters
        ParamOfGetResults param = new ParamOfGetResults(
                domain, query, resultFields);
       return param;
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

    /*
    public void validateSearchInput(String userKeywords) throws InvalidSearchException {
        if (!Validator.isSearchParamsOK(userKeywords)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at least one keyword!");
        }
        LuceneParser luceneParser = new LuceneParser();        
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(userKeywords);
        this.searchParams.setText(cleanedKeywords);
    }
*/
    public String getIdPrefix(String id) {
       String[] idSplit = id.split("_");
       String idPrefix = idSplit[0];
       return idPrefix;
    }

    public LinkedHashSet<String> getIdPrefixes(Collection<String> results) {
        LinkedHashSet<String> idPrefixes = new LinkedHashSet<String>();
       for (String id:results) {
           String idPrefix = getIdPrefix(id);
           idPrefixes.add(idPrefix);
       }
       return idPrefixes;
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


    public SearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {        
        //setting variable values and validation
        this.searchParams = searchParams;
        String userKeywords = this.searchParams.getText();
        LuceneParser luceneParser = new LuceneParser();
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(userKeywords);
        this.searchParams.setText(cleanedKeywords);

        SearchResults enzymeSearchResults = new SearchResults();

        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        //Query the number of results for all domains and save the value in ParamOfGetResults object
        List<ParamOfGetResults> nrOfResultParams = this.getNrOfRecordsRelatedToUniprot();


        //Chebi has to be processed separately due to the filter

        //Retrieve accessions from UNIPROT field
        //Filter does not work here
        Set<String> relatedUniprotAccessionSet =
                ebeyeAdapter.getRelatedUniprotAccessionSet(nrOfResultParams);


        List<String> relatedUniprotAccessionList = new ArrayList<String>();
        relatedUniprotAccessionList.addAll(relatedUniprotAccessionSet);
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();

        //Query the number of results from UNIPROT accessions
        //Filter can be added here
        //ParamOfGetResults nrOfNameResult = this.getNrOfRecords(uniprotDomain, relatedUniprotAccessionSet);
        List<ParamOfGetResults> paramList = this.createSubQueries(uniprotDomain, relatedUniprotAccessionList);

        ebeyeAdapter.getNumberOfResults(paramList);

        int otherDomainsNrOfResults = calTotalResultsFound(paramList);

        //This could be an ArrayList, because the prefix id List will eliminate the
        //duplications
        Collection<String> mergedList = new ArrayList<String>();

        //createSubQueries

        
        //ParamOfGetResults uniprotNameQueryParams = this.getUniprotNrOfRecords();
        //Set<String> relatedUniprotNames =
        //(Set<String>) ebeyeAdapter.getNameSetByAccessions(uniprotDomain, relatedUniprotAccessionSet);

        //Prepare query in

        //Process uniprot separately
        /*
        ParamOfGetResults uniprotParam = getDomainParam(
                    nrOfResultParams, IEbeyeAdapter.Domains.uniprot.name());
        nrOfResultParams.remove(uniprotParam);
        */
        //Query uniprot results
        //List<Result> uniprotResults = new ArrayList<Result>();
        //uniprotResults.addAll(ebeyeAdapter.getResults(uniprotParam));

        //Set<String> mergedList = new LinkedHashSet<String>();

        //Query uniprot directly
        ParamOfGetResults uniprotNrOfResultParams = this.getUniprotNrOfRecords();
        int uniprotResultSize = uniprotNrOfResultParams.getTotalFound();

        //Uniprot results are ranked in the first place.
        if (uniprotResultSize > 0) {
            mergedList.addAll(ebeyeAdapter.getValueOfFields(uniprotNrOfResultParams));
        }

        //Results from other domain are ranked in second place
        if (otherDomainsNrOfResults > 0) {
            mergedList.addAll(ebeyeAdapter.getValueOfFields(paramList));
        }



        //
      
        //int otherDomainsNrOfResults = nrOfNameResult.getTotalFound();

        
        //if (otherDomainsNrOfResults > 0) {
           // mergedList.addAll(ebeyeAdapter.getValueofFields(nrOfNameResult));
        //}


        List<String> resultList = new ArrayList<String>();

        LinkedHashSet<String> idPrefixes = this.getIdPrefixes(mergedList);
        
        List<String> speciesFilter = searchParams.getSpecies();
        if (speciesFilter != null) {
            if (speciesFilter.size() > 0) {
                ParamOfGetResults filteredNrOfResults = this.getNrOfRecordsWithFilter(
                        uniprotDomain, idPrefixes);
                Collection filteredResults = ebeyeAdapter.getValueOfFields(filteredNrOfResults);
                idPrefixes = this.getIdPrefixes(filteredResults);
            }
         }

        resultList.addAll(idPrefixes);
        //resultList.addAll(mergedList);

        //Set<String> uniprotResults = new LinkedHashSet<String>();
        //uniprotResults.addAll(ebeyeAdapter.g(uniprotParam));

        //Save uniprot accession separately
        //Set<String> accessionsFromUniprot = this.getUniprotPrimAcc(uniprotResults);

        //Query results for other domain


        //Save chebi ids
        //String chebiDomain = IEbeyeAdapter.Domains.chebi.name();
        //List<Result> chebiResults =  allDomainsResults.get(chebiDomain);
        //List<String> chebiAccs = this.getResultsXrefsList(chebiResults);
        Set<String> chebiAccs = null;
                //this.getResultsXrefs(chebiResults);

        //Save pdbE results

        //List<Result> pdbeResults = allDomainsResults.get(IEbeyeAdapter.Domains.pdbe.name());

        

        SearchFilters searchFilter = new SearchFilters();
        
        //IEbeyeDomainAdapter ebeyeDomainAdapter = new EbeyeChebiAdapter();

        //TODO
        /*
        Map<String,String> chebiNames = ebeyeAdapter.getNameMapByAccessions(chebiDomain,chebiAccs);
        List<Compound> compList = DataTypeConverter.mapToCompound(chebiNames);
        searchFilter.getCompounds().addAll(compList);
        //searchFilter.getCompounds().addAll(chebiAccs);
        enzymeSearchResults.setSearchfilters(searchFilter);
        */

        //TEST
        
        //Set<String> uniprotNameList = (Set<String>) ebeyeAdapter.getNameSetByAccessions(
           //     IEbeyeAdapter.Domains.uniprot.name(),accessionsFromOtherDomains);
        int resultSize = resultList.size();


        //Process the pagination
        //int totalFound = uniprotResults.size();
        int totalFound = resultList.size();
        int size = searchParams.getSize();
        int start =  searchParams.getStart();
        int subListIndex = start+size;
        if (totalFound<subListIndex) {
            subListIndex=totalFound;
        }

        List<String> resultSubList = resultList.subList(
               start, subListIndex);

        enzymeSearchResults.setTotalfound(totalFound);

        //List<String> idPrefixesSubList = (List<String>) uniprotNameList;
                //idPrefixes.subList(
               //start, subListIndex);
        //searchResults.setTotalfound(totalFound);

        //TODO ????
        //searchModel.getSearchparams().setStart(searchParams.getStart());
        
        IUniprotAdapter uniprotAdapter = new UniprotAdapter();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        //enzymeSummaryList = uniprotAdapter.getEnzymeEntries(briefResultSubset);
        //List<String> queries = LuceneQueryBuilder.createUniprotQueryByIdPrefixes(idPrefixesSubList);
        //enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);

        List<String> queries = LuceneQueryBuilder.createUniprotQueryByIdPrefixes(resultSubList, searchParams.getSpecies());
        enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);

        //setPdbeAccession(enzymeSummaryList, pdbeResults);
        //resultsize
        List<String> uniprotIds = getUniprotIds(enzymeSummaryList);
        Map<String,String> pdbeAccs =
        ebeyeAdapter.getUniprotXrefIds(uniprotIds, IEbeyeAdapter.Domains.pdbe.name());

        setPdbeAccession(enzymeSummaryList, pdbeAccs);



        Map<String,Map<String,String>> chebiFilters =
                ebeyeAdapter.getUniprotXrefIdAndName(uniprotIds, IEbeyeAdapter.Domains.chebi.name());


        setIntenzSynonyms(enzymeSummaryList);
        //TODO
        //searchResults.setResultsize(enzymeSummaryList.size());

        //searchResults.getEnzymesummary().addAll(enzymeSummaryList);

        //EnzymeSummary enzymeSummary = searchResults.getEnzymesummary().get(0);
        //enzymeSummary.
        enzymeSearchResults.getSummaryentries().addAll(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(totalFound);

        createSpeciesFilter(enzymeSearchResults);

        searchParams.setText(userKeywords);

        return enzymeSearchResults;

    }


    public static int calTotalResultsFound(
            List<ParamOfGetResults> resultList) {
        if (resultList ==  null) {
            return 0;
        }
        Iterator it = resultList.iterator();
        int counter = 0;
        for (ParamOfGetResults param:resultList){
            counter = counter + param.getTotalFound();
        }
        return counter;
    }
    public void setIntenzSynonyms(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {

        Set<String> ecSet  = getUniprotEcs(enzymeSummaryList);
        Map<String,Set<String>> intenzSynonyms = queryIntenzSynonyms(ecSet);

        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            List<String> uniprotSyns = enzymeSummary.getSynonym();
            Set<String> intenzUniqueSyns = new TreeSet<String>();
            for (String ec:ecList) {
                Set<String> ecSynonyms = intenzSynonyms.get(ec);
                if (ecSynonyms != null) {
                    intenzUniqueSyns.addAll(ecSynonyms);
                }
                
            }

            intenzUniqueSyns.addAll(uniprotSyns);

            enzymeSummary.getSynonym().clear();

            enzymeSummary.getSynonym().addAll(intenzUniqueSyns);
        }
    }

    /**
     * ec numbers end with "-" will be excluded from the results.
     * @param enzymeSummaryList
     * @return
     * @throws MultiThreadingException
     */
    public Set<String> getUniprotEcs(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {        
        Set<String> ecSet = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            for (String ec: ecList) {
                if (!ec.contains("-")) {
                    ecSet.add(ec);
                }
            }
        }
        return ecSet;
    }

    public Map<String,Set<String>>  queryIntenzSynonyms(Set<String> ecSet) throws MultiThreadingException {
            IintenzAdapter intenzAdapter = new IntenzAdapter();
            return intenzAdapter.getSynonyms(ecSet);
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


    public void createSpeciesFilter(SearchResults enzymeSearchResults) {
        List<EnzymeSummary> enzymeSummaryList = enzymeSearchResults
                .getSummaryentries();
        
        SearchFilters searchFilters = new SearchFilters();
        List<Species> speciesFilter = new ArrayList<Species>();

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

        searchFilters.getSpecies().addAll(speciesFilter);
        enzymeSearchResults.setSearchfilters(searchFilters);
        System.out.print(speciesFilter);

    }


    /**
     * Prepare field queries for all domains except for uniprot and chebi.
     * @param searchParams
     * @return
     */
    public static List<ParamOfGetResults>  prepareGetRelatedRecordsToUniprotQueries(
            SearchParams searchParams) {
        List<Domain> domains = Config.domainList;
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        for (Domain domain: domains) {
            String domainId = domain.getId();
            if (!domainId.equals(IEbeyeAdapter.Domains.uniprot.name())
                    || !domainId.equals(IEbeyeAdapter.Domains.chebi.name())) {
                List<String> resultFields = new ArrayList<String>();
                resultFields.add(IEbeyeAdapter.UNIPROT_REF_FIELD);
                String query = LuceneQueryBuilder.createGetRelatedUniprotAccessionsQueries(
                        domain, searchParams);
               ParamOfGetResults paramOfGetAllResults =
                        new ParamOfGetResults(domainId, query, resultFields);
                paramList.add(paramOfGetAllResults);
            }

        }
        return paramList;
    }


    public static ParamOfGetResults  prepareGetUniprotIdQueries(
            SearchParams searchParams) {
        Domain domain = Config.getDomain(IEbeyeAdapter.Domains.uniprot.name());
        //List<String> searchFields = DataTypeConverter.getConfigSearchFields(domain);
            List<String> resultFields = new ArrayList<String>();
            resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.id.name());
            String query = LuceneQueryBuilder.createGetUniprotFieldQueries(domain, searchParams);
           ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(IEbeyeAdapter.Domains.uniprot.name(), query, resultFields);
        return paramOfGetAllResults;
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
    /*
    public List<String> getResultsXrefsList(List<Result> resultList) {
        List<String> list = new ArrayList<String>();
        list.addAll(getResultsXrefs(resultList));
        return list;
    }
     * */
     
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
    /*
    public SearchResults createEmptyResponse() {
        SearchResults enzymeSearchResults = new SearchResults();
        enzymeSearchResults.setTotalfound(0);
        return enzymeSearchResults;
    }
     *
     */
    
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
