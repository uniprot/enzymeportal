/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.ebeyeSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.epcore.CommonSpecies;
import uk.ac.ebi.ep.data.model.EnzymeSummary;
import uk.ac.ebi.ep.data.model.SearchFilters;
import uk.ac.ebi.ep.data.model.SearchParams;
import uk.ac.ebi.ep.data.model.SearchResults;
import uk.ac.ebi.ep.data.model.Species;
import uk.ac.ebi.ep.data.service.UniprotEntryService;

/**
 *
 * @author joseph
 */
public class EnzymeFinder {

    private final Logger LOGGER = Logger.getLogger(EnzymeFinder.class);
    protected SearchParams searchParams;
    SearchResults enzymeSearchResults;
    List<String> uniprotEnzymeIds;
    boolean newSearch;
    Set<String> uniprotIdPrefixSet;
    List<String> speciesFilter;
    List<String> compoundFilter;
    List<EnzymeSummary> enzymeSummaryList;

    private UniprotEntryService service;

    public EnzymeFinder(UniprotEntryService service) {
        this.service = service;

        enzymeSearchResults = new SearchResults();
        //ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new ArrayList<>();
        uniprotIdPrefixSet = new LinkedHashSet<>();
        enzymeSummaryList = new ArrayList<>();
        //intenzAdapter = new IntenzAdapter();
    }

    /**
     * Escapes the keywords, validates the filters and sets the global variables
     * to be used in other methods.
     *
     * @param searchParams
     */
    private void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        speciesFilter = searchParams.getSpecies();
        LuceneParser luceneParser = new LuceneParser();
        String keyword = luceneParser.escapeLuceneSpecialChars(this.searchParams.getText());
        String cleanKeyword = HtmlUtility.cleanText(keyword);

        this.searchParams.setText(cleanKeyword);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        compoundFilter = searchParams.getCompounds();
        //List<String> speciesFilter = searchParams.getSpecies();

        /*
         * There are 2 cases to treat the search as new search: case 1 - the new
         * text is different from the previous text case 2 - all filters are
         * empty
         */
        if (!previousText.equalsIgnoreCase(currentText)
                || (compoundFilter.size() == 0 && speciesFilter.size() == 0)) {
            newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }
    }

    private void queryEbeyeRest() {

    }

    private EbeyeSearchResult getEbeyeSearchResult() {

        RestTemplate restTemplate = new RestTemplate();
        String query = searchParams.getText();
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query=" + query + "&format=json&size=100";

        EbeyeSearchResult searchResult = restTemplate.getForObject(url, EbeyeSearchResult.class);
        return searchResult;
    }

    private void getResultsFromUniProt() {
        EbeyeSearchResult searchResult = getEbeyeSearchResult();
        List<UniProtDomain> results = searchResult.getUniProtDomains().stream().distinct()
                .collect(Collectors.toList());

        results.stream().forEach((result) -> {
            // prefixes.add("ACE");
            //prefixes.add(result.getUniport_name());
            //accessions.add(result.getUniprot_accession());
            //uniprotEnzymeIds.add(result.getUniprot_accession());
            uniprotEnzymeIds.add(result.getUniport_name());
            //System.out.println("names "+ result.getUniport_name());
        });

    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeForUniprotIds() {
        getResultsFromUniProt();
    }

    Set<Species> uniqueSpecies = new TreeSet<>();
    Set<EnzymePortalDisease> uniqueDiseases = new TreeSet<>();

    //this is the part that takes ages.....
    private List<EnzymeSummary> getEnzymesFromUniprotAPI(
            List<String> prefixes, List<String> paramList) {

        List<EnzymeSummary> enzymeList = new ArrayList<>();
        List<UniprotEntry> enzymes = service.findByNamePrefixes(prefixes);
        // List<UniprotEntry> enzymes = service.findEnzymesByAccessions(prefixes);

        for (UniprotEntry e : enzymes) {

            Species s = new Species();
            s.setCommonname(e.getCommonName());
            s.setScientificname(e.getScientificName());
            s.setSelected(false);
            uniqueSpecies.add(s);

            for (EnzymePortalDisease d : e.getEnzymePortalDiseaseList()) {
                EnzymePortalDisease disease = new EnzymePortalDisease();
                disease.setDiseaseId(d.getDiseaseId());
                disease.setDiseaseName(d.getDiseaseName());
                disease.setUrl(d.getUrl());
                disease.setDefinition(d.getDefinition());
                disease.setEvidence(d.getEvidence());
                disease.setUniprotAccession(d.getUniprotAccession());
                uniqueDiseases.add(disease);
            }

            EnzymeSummary summary = new EnzymeSummary();
            summary.setName(e.getProteinName());
            summary.getSynonym().add(e.getSynonymName());
            summary.setCompounds(e.getEnzymePortalCompoundSet());
            summary.setDiseases(e.getEnzymePortalDiseaseList());
            summary.setUniprotid(e.getName());
            summary.getUniprotaccessions().add(e.getAccession());
            summary.setSpecies(s);
            

            enzymeList.add(summary);

        }

        return enzymeList;
    }

    /**
     * Retrieves full enzyme summaries.
     *
     * @param uniprotIdPrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */
    private List<EnzymeSummary> getEnzymeSummaries(
            List<String> uniprotIdPrefixes, List<String> paramList) {
        List<EnzymeSummary> summaries = getEnzymesFromUniprotAPI(uniprotIdPrefixes, paramList);
//        if (summaries != null && !summaries.isEmpty()) {
//            EnzymeSummaryProcessor[] processors = {
//                new SynonymsProcessor(summaries, intenzAdapter)
//            };
//            for (EnzymeSummary summary : summaries) {
//                for (EnzymeSummaryProcessor processor : processors) {
//                    processor.process(summary);
//        }
//            }
//        }
        return summaries;
    }

    public SearchResults getEnzymes(SearchParams searchParams) {

        processInputs(searchParams);

        /*
         * First time search or when user inserts a new keyword, the filter is
         * reset then the search is performed across all domains without
         * considering the filter.
         */
        if (newSearch) {
            // Search in EBEye for Uniprot ids, the search is filtered by ec:*
            LOGGER.debug("Starting new search");
            // try {
            queryEbeyeForUniprotIds();
            //LOGGER.debug("UniProt IDs from UniProt: " + uniprotEnzymeIds.size());
            //} catch (EnzymeFinderException e){
            //LOGGER.error("Unable to search EB-Eye uniprot domain", e);
            //}
            // Search in Intenz, Rhea, Reactome, PDBe etc. for Uniprot ids.
            //try {
            //queryEbeyeOtherDomainForIds();
            LOGGER.debug("UniProt IDs from UniProt+ChEBI+others: "
                    + uniprotEnzymeIds.size());
            //} catch (EnzymeFinderException e){
            // LOGGER.error("Unable to search EB-Eye other domains", e);
            //}

            uniprotIdPrefixSet.addAll(uniprotEnzymeIds);
        }

        List<String> idPrefixesList
                = new ArrayList<>(uniprotIdPrefixSet);

        LOGGER.debug("Getting enzyme summaries...");
        enzymeSummaryList = getEnzymeSummaries(idPrefixesList, searchParams.getSpecies());
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        if (uniprotIdPrefixSet.size() != enzymeSummaryList.size()) {
            LOGGER.warn((uniprotIdPrefixSet.size() - enzymeSummaryList.size())
                    + " UniProt ID prefixes have been lost");
        }
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        LOGGER.debug("Finished search");
        //closeResources();
        return enzymeSearchResults;
    }

    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults) {
        //  String[] commonSpecie = {"Human", "Mouse", "Rat", "Fruit fly", "Worm", "Yeast", "Ecoli"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

       // Set<Species> uniqueSpecies = new TreeSet<>();
        //Set<EnzymePortalDisease> uniqueDiseases = new TreeSet<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        for (Species sp : uniqueSpecies) {
            //Species sp = wrapper.getSpecies();

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // Human, Mouse, Rat, Fly, Worm, Yeast, Ecoli 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Worm","Saccharomyces cerevisiae","Ecoli"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Human.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Mouse.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Rat.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Fruit_fly.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Worm.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Ecoli.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
                    priorityMapper.put(customKey.getAndIncrement(), sp);

                }
            } else {

                priorityMapper.put(key.getAndIncrement(), sp);

            }
        }

        Set<Species> speciesFilters = new HashSet<>();
        for (Map.Entry<Integer, Species> map : priorityMapper.entrySet()) {
            speciesFilters.add(map.getValue());

        }

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        // filters.setCompounds(compoundFilters);
        filters.setDiseases(uniqueDiseases);
        searchResults.setSearchfilters(filters);
    }
}
