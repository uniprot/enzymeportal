package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.intenz.adapter.IintenzAdapter;
import uk.ac.ebi.ep.intenz.adapter.IntenzAdapter;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchFilters;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.Species;
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
    SearchResults enzymeSearchResults;
    IEbeyeAdapter ebeyeAdapter;
    Set<String> uniprotEnzymeIds;
    boolean newSearch;
    Map<String,List<String>> chebiResults;
    List<String> chebiIds;
    Set<String> uniprotIdPrefixSet;
    List<String> speciesFilter;
    List<EnzymeSummary> enzymeSummaryList;
    IintenzAdapter intenzAdapter;
    IUniprotAdapter uniprotAdapter;
    
    //This could be an ArrayList, because the prefix id List will eliminate the duplications
    


    private static Logger log = Logger.getLogger(EnzymeFinder.class);

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() {
        enzymeSearchResults = new SearchResults();
        ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new LinkedHashSet<String>();
        uniprotIdPrefixSet = new LinkedHashSet<String>();
        enzymeSummaryList = new ArrayList<EnzymeSummary>();
        intenzAdapter = new IntenzAdapter();
        uniprotAdapter = new UniprotAdapter();
    }


//****************************** GETTER & SETTER *****************************//
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }


//********************************** METHODS *********************************//

    /**
     * Limite the number of results to the {@code IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT}
     * 
     * @param params
     */
    public void resetNrOfResultsToLimit(List<ParamOfGetResults> params) {
        for (ParamOfGetResults param:params) {
            resetNrOfResultsToLimit(param);
        }

    }

    public void resetNrOfResultsToLimit(ParamOfGetResults param) {
            int totalFound = param.getTotalFound();
            if (param.getDomain().equals(IEbeyeAdapter.Domains.uniprot.name())) {
                if (totalFound > IEbeyeAdapter.EP_UNIPROT_RESULTS_LIMIT) {
                    param.setTotalFound(IEbeyeAdapter.EP_UNIPROT_RESULTS_LIMIT);
                }
            } else {
                if (param.getDomain().equals(IEbeyeAdapter.Domains.chebi.name())) {
                    if (totalFound > IEbeyeAdapter.EP_CHEBI_RESULTS_LIMIT) {
                        param.setTotalFound(IEbeyeAdapter.EP_CHEBI_RESULTS_LIMIT);
                    }
                } else {
                    if (totalFound > IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT) {
                        param.setTotalFound(IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT);
                    }
            }
            }
    }


    public List<ParamOfGetResults> getNrOfRecordsRelatedToUniprot() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        //prepare list of parameters
        List<ParamOfGetResults> paramsWithoutNrOfResults =
                                    prepareGetRelatedRecordsToUniprotQueries(searchParams);
       List<ParamOfGetResults> paramsWithNrOfResults = new ArrayList<ParamOfGetResults>();
        try {
            //List<List<Result>> allDomainsResults = null;
            //get and set the value at the same time
            paramsWithNrOfResults.addAll(ebeyeAdapter.getNumberOfResults(paramsWithoutNrOfResults));

            //resetNrOfResultsToLimit(paramsWithNrOfResults);

        } catch (MultiThreadingException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
       return paramsWithoutNrOfResults;
    }

    public ParamOfGetResults getUniprotNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareGetUniprotIdQueries(searchParams);
        ebeyeAdapter.getNumberOfResults(param);
        //resetNrOfResultsToLimit(param);
       return param;
    }

    public ParamOfGetResults getChebiNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareChebiQueries();
        ebeyeAdapter.getNumberOfResults(param);
        //resetNrOfResultsToLimit(param);
       return param;
    }

    public static List<String> createIdWildcardQueriesWithSpeciesFilter (
                    List<String> ids, List<String> seletedSpecies)  {
        String fieldName = IEbeyeAdapter.FieldsOfGetResults.id.name();        
        boolean wildcard = true;
        int idListSizeLimit = EbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT;
        String filterFieldName = EbeyeAdapter.EBEYE_SPECIES_FIELD;
        //List<String> queries = LuceneQueryBuilder.createQueriesIn(
        List<String> queries = LuceneQueryBuilder.createUniprotAPIQueriesIn(
                fieldName, ids, wildcard, idListSizeLimit);
        //List<String> queriesWithFilter = new ArrayList<String>();
        //String filterQuery = LuceneQueryBuilder.createQueryIN(filterFieldName, false, filterValues);
        List<String> joinedQueries = LuceneQueryBuilder.addFilterQueriesAND(
                queries, filterFieldName, seletedSpecies);
        return joinedQueries;
    }

    public List<ParamOfGetResults> prepareParamsForQueryIN(
            String domain, List<String> queries, List<String> resultFields) {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();       
        for (String query: queries) {
            ParamOfGetResults param = new ParamOfGetResults(
            domain, query, resultFields);
            paramList.add(param);
        }
        return paramList;
    }

    public String getIdPrefix(String id) {
       String[] idSplit = id.split("_");
       String idPrefix = idSplit[0];
       return idPrefix;
    }

    public ArrayList<String> getIdPrefixes(Collection<String> results) {
        Set<String> prefixes = new LinkedHashSet<String>();
       for (String id:results) {
           String idPrefix = getIdPrefix(id);
           prefixes.add(idPrefix);
       }
       return new ArrayList<String>(prefixes);
    }

      /**
       * Get Uniprot ids which are enzymes
       * @param accs
       * @return
       * @throws MultiThreadingException
       */
    public List<String> queryUniprotEnzymeIdRefFromOtherDomains(List<String> accs) throws MultiThreadingException {
        String queryField = IEbeyeAdapter.FieldsOfGetResults.acc.name();
        int subListSize = IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT;
        List<String> resultFields = new ArrayList<String>();

        resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
         /*
        List<String> limitedList = null;
        //Limited the results to process in order to improve the performance
       
        if (accs.size() > IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT){
            limitedList = accs.subList(0, IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT);
        } else {
            limitedList = accs;
        }
         * 
         */

        //Enzyme filter must be added
        List<String> queries = LuceneQueryBuilder.createQueriesIn(
                queryField,accs, false, subListSize);
        List<ParamOfGetResults> paramList = this.prepareParamsForQueryIN(
                IEbeyeAdapter.Domains.uniprot.name(), queries,
                resultFields);

        ebeyeAdapter.getNumberOfResults(paramList);

        int otherDomainsNrOfResults = calTotalResultsFound(paramList);
        List<String> results = new ArrayList<String>();
        if (otherDomainsNrOfResults > 0) {
            results.addAll(ebeyeAdapter.getValueOfFields(paramList));
        }
        return results;
    }

    public void queryEbeyeChebiForUniprotIds() throws EnzymeFinderException {
        
        //Chebi has to be processed separately due to the filter
        ParamOfGetResults chebiParam = this.getChebiNrOfRecords();
        resetNrOfResultsToLimit(chebiParam);
        //int chebiResultSize = chebiParam.getTotalFound();

        chebiResults = ebeyeAdapter.getUniprotRefAccesionsMap(chebiParam);

        int chebiResultSize = chebiParam.getTotalFound();
        Collection<List<String>> chebiAccs = chebiResults.values();

        LinkedHashSet<String> uniprotAccsFromChebiSet = DataTypeConverter.mergeList(
                chebiAccs);
         List<String> uniprotAccsFromChebi = new ArrayList<String>(uniprotAccsFromChebiSet);
        List<String> uniprotIdsRefFromChebi =
        queryUniprotEnzymeIdRefFromOtherDomains(uniprotAccsFromChebi);

        if (uniprotIdsRefFromChebi != null) {
            this.uniprotEnzymeIds.addAll(uniprotIdsRefFromChebi);
        }
        
    }

    public void setCounpoundFilter(List<String> chebiIds ) {
        SearchFilters searchFilter = new SearchFilters();        
        Map<String,String> chebiIdNameMap = ebeyeAdapter.getNameMapByAccessions(
                IEbeyeAdapter.Domains.chebi.name(), chebiIds);
        List<Compound> compList = DataTypeConverter.mapToCompound(chebiIdNameMap);
        searchFilter.getCompounds().addAll(compList);
        //searchFilter.getCompounds().addAll(chebiAccs);
        enzymeSearchResults.setSearchfilters(searchFilter);
    }
    
    public void queryOtherDomainEbeyeForIds() throws EnzymeFinderException {
        //Query the number of results for all domains and save the value in ParamOfGetResults object
        List<ParamOfGetResults> nrOfResultParams = this.getNrOfRecordsRelatedToUniprot();
        resetNrOfResultsToLimit(nrOfResultParams);
        //Retrieve accessions from UNIPROT field
        //Filter does not work here
        Set<String> relatedUniprotAccessionSet =
                ebeyeAdapter.getRelatedUniprotAccessionSet(nrOfResultParams);


        List<String> relatedUniprotAccessionList = new ArrayList<String>();
        relatedUniprotAccessionList.addAll(relatedUniprotAccessionSet);

        //Query the number of results from UNIPROT accessions
        //Species Filter can not be added here because some species in the top 10 result set
        //are from uniprot API and can not be found here
        //ParamOfGetResults nrOfNameResult = this.getNrOfRecords(uniprotDomain, relatedUniprotAccessionSet);

        List<String> uniprotIdsRefFromOtherDomains =
                queryUniprotEnzymeIdRefFromOtherDomains(relatedUniprotAccessionList);

        if (uniprotIdsRefFromOtherDomains != null) {
            uniprotEnzymeIds.addAll(uniprotIdsRefFromOtherDomains);
        }

    }
    public void queryUniprotEbeyeForIds() throws EnzymeFinderException {
        //Query uniprot directly
        ParamOfGetResults uniprotNrOfResultParams = this.getUniprotNrOfRecords();
        resetNrOfResultsToLimit(uniprotNrOfResultParams);
        int uniprotResultSize = uniprotNrOfResultParams.getTotalFound();

        //Uniprot results are ranked in the first place.
        if (uniprotResultSize > 0) {
            uniprotEnzymeIds.addAll(ebeyeAdapter.getValueOfFields(uniprotNrOfResultParams));
        }
    }

    public List<String> filterUniprotIdPrefixesBySpecies(
            List<String> unfilteredIdPrefixes, List<String> speciesFilter) throws EnzymeFinderException {        
        List<String> filteredIdPrefixes = new ArrayList<String>();
        filteredIdPrefixes.addAll(
                filterUniprotIdBySpecies(unfilteredIdPrefixes, speciesFilter));
        return this.getIdPrefixes(filteredIdPrefixes);
     }

    public List<String> filterUniprotIdBySpecies(
            List<String> unfilteredIdPrefixes, List<String> speciesFilter) throws EnzymeFinderException {        
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();
        Set<String> filteredResults = null;
        //Ignore filters for new search
        if (speciesFilter.size() > 0 && !newSearch) {
                List<String> resultFields = new ArrayList<String>();
                resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
                List<String> filterQueries = createIdWildcardQueriesWithSpeciesFilter(unfilteredIdPrefixes, speciesFilter);
                List<ParamOfGetResults> filteredNrOfResults =
                        this.prepareParamsForQueryIN(uniprotDomain, filterQueries,
                        resultFields);
                ebeyeAdapter.getNumberOfResults(filteredNrOfResults);
                filteredResults = ebeyeAdapter.getValueOfFields(filteredNrOfResults);
        }
            return new ArrayList<String>(filteredResults);
     }
   
    
    public void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        speciesFilter = searchParams.getSpecies();
        LuceneParser luceneParser = new LuceneParser();
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(this.searchParams.getText());
        this.searchParams.setText(cleanedKeywords);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        List<String> compoundFilter = searchParams.getCompounds();
        //List<String> speciesFilter = searchParams.getSpecies();

        /**
         * There are 2 cases to treat the search as new search:
         * case 1 - the new text is different from the previous text
         * case 2 - all filters are empty
         */
        if (!previousText.equalsIgnoreCase(currentText) ||
                (compoundFilter.size() == 0 && speciesFilter.size() == 0)) {
             newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }

    }


    public void filterCompoundBySpecies(Set<String> uniprotFilteredIdPrefixes) throws EnzymeFinderException {
         
        this.uniprotEnzymeIds.clear();
        this.uniprotIdPrefixSet.clear();
        queryEbeyeChebiForUniprotIds();
        List<String> chebiUnfilteredIdPrefixes =
                this.getIdPrefixes(this.uniprotEnzymeIds);
        //workaround does not completely solve non ref from uniprot to chebi problem

        Set<String> chebiFilteredIdPrefixes =
                new LinkedHashSet<String>(this.filterUniprotIdPrefixesBySpecies(
                    chebiUnfilteredIdPrefixes, speciesFilter));

        //If chebi

        //uniprotFilteredIdPrefixes.containsAll(chebiIds);

        if (Collections.disjoint(uniprotFilteredIdPrefixes, chebiFilteredIdPrefixes)) {
              uniprotIdPrefixSet.addAll(uniprotFilteredIdPrefixes);
              chebiIds = new ArrayList<String>();
        } else {
              uniprotIdPrefixSet.addAll(chebiFilteredIdPrefixes);
            if (chebiResults.size() > 0) {
                chebiIds = new ArrayList<String>(chebiResults.keySet());
                //To avoid showing compounds that are not for the selected species
                //chebiIds.retainAll(uniprotFilteredIdPrefixes);
            }
        }
    }

    public SearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {        
        //setting variable values and validation
        //keywords before being cleaned
        String userKeywords = new String(searchParams.getText());
        processInputs(searchParams);
        List<String> compoundFilter = searchParams.getCompounds();
        //List<String> speciesFilter = searchParams.getSpecies();
        int speciesFilterSize = speciesFilter.size();        
        //List<String> idPrefixes = null;
        List<String> uniprotIdPrefixesFromChebi = new ArrayList<String>();
        //Set<String> chebiIds = null;
        if (newSearch) {
            queryEbeyeChebiForUniprotIds();
            queryOtherDomainEbeyeForIds();
            queryUniprotEbeyeForIds();
            uniprotIdPrefixSet.addAll(this.getIdPrefixes(this.uniprotEnzymeIds));
            chebiIds = new ArrayList<String>(chebiResults.keySet());
        } //Search with filters
        else {
            // compound is selected
            if (compoundFilter.size() > 0) {
                //combined filters
                if (speciesFilterSize > 0) {
                    queryUniprotEbeyeForIds();
                    //queryEbeyeChebiForUniprotIds();
                    queryOtherDomainEbeyeForIds();

                    //Only search in chebi because even though other domains have results
                    //only those available in chebi are shown
                    
                    //filer chebi results by species
                    List<String> unfilterIdPrefixes = new
                            ArrayList<String>(this.getIdPrefixes(this.uniprotEnzymeIds));
                    //queryUniprotEbeyeForIds();
                    //List<String> unFilteredIdPrefixes = this.getIdPrefixes(this.uniprotEnzymeIds);
                    //Add the id prefixes found in chebi
                    //unFilteredIdPrefixes.addAll(idPrefixes);
                    
                    Set<String> uniprotFilteredIdPrefixes = new LinkedHashSet<String>();


                    uniprotFilteredIdPrefixes.addAll(
                            this.filterUniprotIdPrefixesBySpecies(unfilterIdPrefixes, speciesFilter));

                    filterCompoundBySpecies(uniprotFilteredIdPrefixes);
                    
                } //species is not selected and compound is selected
                else {
                //When there is filter the query is created using the id only
                queryEbeyeChebiForUniprotIds();
                uniprotIdPrefixesFromChebi.addAll(this.getIdPrefixes(this.uniprotEnzymeIds));
                uniprotIdPrefixSet.addAll(uniprotIdPrefixesFromChebi);
                chebiIds = new ArrayList<String>(chebiResults.keySet());

                }
            } // compound is not selected
            else {
                if (speciesFilterSize > 0) {
                    queryUniprotEbeyeForIds();
                    //queryEbeyeChebiForUniprotIds();
                    queryOtherDomainEbeyeForIds();

                    //How to filter chebi uniprot ids?
                    List<String> unFilteredIdPrefixes = this.getIdPrefixes(this.uniprotEnzymeIds);

                     Set<String> uniprotFilteredIdPrefixes = new LinkedHashSet<String>();

                     uniprotFilteredIdPrefixes.addAll(
                             this.filterUniprotIdPrefixesBySpecies(unFilteredIdPrefixes, speciesFilter));

                    filterCompoundBySpecies(uniprotFilteredIdPrefixes);
                }
            }
          }
    

        this.setCounpoundFilter(chebiIds);
        //Process the pagination
        //int totalFound = uniprotResults.size();
        int totalFound = uniprotIdPrefixSet.size();
        int size = this.searchParams.getSize();
        int start =  this.searchParams.getStart();
        int subListIndex = start+size;
        if (totalFound<subListIndex) {
            subListIndex=totalFound;
        }
        List<String> idPrefixesList = new ArrayList<String>(uniprotIdPrefixSet);

        List<String> resultSubList = idPrefixesList.subList(
               start, subListIndex);

        enzymeSearchResults.setTotalfound(totalFound);

        enzymeSummaryList = this.getEnzymeSummary(resultSubList);
        /*
        IUniprotAdapter uniprotAdapter = new UniprotAdapter();
        List<String> queries = LuceneQueryBuilder
                .createUniprotAPIQueryByIdPrefixes(resultSubList, speciesFilter);

        
        enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);

        //Ma<String,Map<String,String>> chebiFilters =
           //     ebeyeAdapter.getUniprotXrefIdAndName(uniprotIds, IEbeyeAdapter.Domains.chebi.name());


        setIntenzSynonyms(enzymeSummaryList);
        */
        List<ParamOfGetResults> pdbeParams =
                prepareQueryForPdbeAccs(this.getUniprotEcs(enzymeSummaryList));
                Map<String,String> pdbeAccs =
        ebeyeAdapter.getMapOfFieldAndValue(pdbeParams);

        setPdbeAccession(pdbeAccs);


        enzymeSearchResults.getSummaryentries().addAll(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(totalFound);

        createSpeciesFilter(enzymeSearchResults);
        searchParams.setStart(start);
        searchParams.setText(userKeywords);
        searchParams.setPrevioustext(userKeywords);

        return enzymeSearchResults;

    }

    public List<EnzymeSummary> getEnzymeFromUniprotAPI(
            List<String> resultSubList) throws MultiThreadingException {        
        List<String> queries = LuceneQueryBuilder
                .createUniprotAPIQueryByIdPrefixes(resultSubList, speciesFilter);
        List<EnzymeSummary> enzymeList = uniprotAdapter
                .queryEnzymeByIdPrefixes(queries, IEnzymeFinder.DEFAULT_SPECIES);
        return enzymeList;
    }

    public List<EnzymeSummary> getEnzymeSummary(List<String> resultSubList) throws MultiThreadingException {
        List<EnzymeSummary> enzymeList = getEnzymeFromUniprotAPI(resultSubList);
        if (enzymeList != null) {
            addIntenzSynonyms(enzymeList);
        }
        return enzymeList;
    }
    
    public List<ParamOfGetResults> prepareQueryForPdbeAccs(Collection<String> ecNumbers) {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        for (String ec: ecNumbers) {
            List<String> field = new ArrayList<String>();
            field.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());
            ParamOfGetResults pdbeParam = new ParamOfGetResults(
                  IEbeyeAdapter.Domains.pdbe.name(), ec, field);
            params.add(pdbeParam);
        }
        return params;
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
    public void addIntenzSynonyms(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {

        Set<String> ecSet  = getUniprotEcs(enzymeSummaryList);
        Map<String,Set<String>> intenzSynonyms = intenzAdapter.getSynonyms(ecSet);

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

            //Remove the synonyms from uniprot because they have been merged with
            //synonyms from intenz
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


/*
    public void setPdbeAccession(Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            String pdbeAcc = pdbeAccs.get(uniprotId);
            enzymeSummary.getPdbeaccession().add(pdbeAcc);
        }       
    }
*/

    public void setPdbeAccession(Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            if (ecList.size() > 0) {
            String ec = enzymeSummary.getEc().get(0);
            String pdbeAcc = pdbeAccs.get(ec);
            if (pdbeAcc != null) {
                enzymeSummary.getPdbeaccession().add(pdbeAcc);
                }
            }
        }
    }

   public List<String> getUniprotIds(List<EnzymeSummary> enzymeSummaryList) {
        List<String> uniprotIds = new ArrayList<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            uniprotIds.add(uniprotId);
        }
        return uniprotIds;
    }

    public void createSpeciesFilter(SearchResults enzymeSearchResults) {        
        SearchFilters searchFilters = enzymeSearchResults.getSearchfilters();
        List<Species> uniprotSpeciesFilter = new ArrayList<Species>();

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
                uniprotSpeciesFilter.add(species);
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
                    uniprotSpeciesFilter.add(relSpecies);
                }
            }
        }

        searchFilters.getSpecies().addAll(uniprotSpeciesFilter);
        enzymeSearchResults.setSearchfilters(searchFilters);
        System.out.print(uniprotSpeciesFilter);

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
                String query = LuceneQueryBuilder.createFieldsQuery(
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
            String query = LuceneQueryBuilder.createFieldsQueryWithEnzymeFilter(domain, searchParams);
           ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(IEbeyeAdapter.Domains.uniprot.name(), query, resultFields);
        return paramOfGetAllResults;
    }

    public ParamOfGetResults  prepareChebiQueries() {
        Domain domain = Config.getDomain(IEbeyeAdapter.Domains.chebi.name());
        //List<String> searchFields = DataTypeConverter.getConfigSearchFields(domain);
            List<String> resultFields = new ArrayList<String>();
            //resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.name.name());
            resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.id.name());
            resultFields.add(IEbeyeAdapter.FieldsOfGetResults.UNIPROT.name());
            //The query has to be created differently if the compound filter is selected
            String query = null;
            List<String> compoundFilter = this.searchParams.getCompounds();
            if (compoundFilter.size() > 0) {
               query = LuceneQueryBuilder.createQueryIN(
                       IEbeyeAdapter.FieldsOfChebiNameMap.id.name(), false, compoundFilter);
            } else {
                query = LuceneQueryBuilder.createFieldsQuery(domain, searchParams);
            }

            //LuceneQueryBuilder.createQueryIN(query, false, resultFields)
           ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(IEbeyeAdapter.Domains.chebi.name(), query, resultFields);
        return paramOfGetAllResults;
    }     
  }
