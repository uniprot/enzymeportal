package uk.ac.ebi.ep.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.literature.CitexploreWSClientPool;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.SpeciesDefaultWrapper;
import uk.ac.ebi.ep.core.filter.CompoundsPredicate;
import uk.ac.ebi.ep.core.filter.DefaultPredicate;
import uk.ac.ebi.ep.core.filter.DiseasesPredicate;
import uk.ac.ebi.ep.core.filter.SpeciesPredicate;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.search.result.Pagination;

/**
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/> $LastChangedDate: 2012-03-26
 * 11:53:57 +0100 (Mon, 26 Mar 2012) $ <br/> $Author$
 * @author $Author$
 */
@Controller
public class SearchController {

    private static final Logger LOGGER = Logger.getLogger(SearchController.class);

    private enum ResponsePage {

        ENTRY, ERROR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    @Autowired
    private EbeyeConfig ebeyeConfig;
    @Autowired
    private UniprotConfig uniprotConfig;
    @Autowired
    private Config searchConfig;
    @Autowired
    private IntenzConfig intenzConfig;
    @Autowired
    private ReactomeConfig reactomeConfig;
    @Autowired
    private ChebiConfig chebiConfig;
    @Autowired
    private LiteratureConfig literatureConfig;

    @PostConstruct
    public void init(){
    	try {
			CitexploreWSClientPool.setSize(
					literatureConfig.getCitexploreClientPoolSize());
			LOGGER.info("CiteXplore client pool size set successfuly");
		} catch (Exception e) {
			LOGGER.error("Unable to set CiteXplore client pool size", e);
		}
    }
    
    /**
     * Process the entry page,
     *
     * @param accession The UniProt accession of the enzyme.
     * @param field the requested tab.
     * @param model
     * @return
     */
    @RequestMapping(value = "/search/{accession}/{field}")
    protected String getEnzymeModel(Model model,
            @PathVariable String accession, @PathVariable String field,
            HttpSession session) {
        Field requestedField = Field.valueOf(field);
        EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
        retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
        retriever.getUniprotAdapter().setConfig(uniprotConfig);
        retriever.getIntenzAdapter().setConfig(intenzConfig);
        EnzymeModel enzymeModel = null;
        String responsePage = ResponsePage.ENTRY.toString();
        try {
            switch (requestedField) {
                case proteinStructure:
                    enzymeModel = retriever.getProteinStructure(accession);
                    break;
                case reactionsPathways:
                    retriever.getReactomeAdapter().setConfig(reactomeConfig);
                    enzymeModel = retriever.getReactionsPathways(accession);
                    break;
                case molecules:
                    retriever.getChebiAdapter().setConfig(chebiConfig);
                    enzymeModel = retriever.getMolecules(accession);
                    break;
                case diseaseDrugs:
                    enzymeModel = retriever.getDiseases(accession);
                    break;
                case literature:
                	retriever.getLiteratureAdapter().setConfig(literatureConfig);
                    enzymeModel = retriever.getLiterature(accession);
                    break;
                default:
                    enzymeModel = retriever.getEnzyme(accession);
                    requestedField = Field.enzyme;
                    break;
            }
            enzymeModel.setRequestedfield(requestedField.name());
            model.addAttribute("enzymeModel", enzymeModel);
            addToHistory(session, accession);
        } catch (Exception ex) {
            LOGGER.error("Unable to retrieve the entry!", ex);
            // responsePage = ResponsePage.ERROR.toString();
            //responsePage = "errors";
            if (requestedField.getName().equalsIgnoreCase(Field.diseaseDrugs.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.name());
                Disease d = new Disease();
                d.setName("error");
                enzymeModel.getDisease().add(0, d);
                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Disease Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.molecules.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                Molecule molecule = new Molecule();
                molecule.setName("error");
                ChemicalEntity chemicalEntity = new ChemicalEntity();
                chemicalEntity.getDrugs().add(0, molecule);
                enzymeModel.setMolecule(chemicalEntity);
                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Molecules Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.enzyme.getName())) {

                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                Enzyme enzyme = new Enzyme();
                enzyme.getEnzymetype().add(0, "error");
                enzymeModel.setEnzyme(enzyme);

                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Enzymes");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.proteinStructure.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                ProteinStructure structure = new ProteinStructure();
                structure.setName("error");
                enzymeModel.getProteinstructure().add(0, structure);

                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving ProteinStructure");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.reactionsPathways.getName())) {
                enzymeModel = new EnzymeModel();

                enzymeModel.setRequestedfield(requestedField.getName());
                ReactionPathway pathway = new ReactionPathway();
                EnzymeReaction reaction = new EnzymeReaction();
                reaction.setName("error");
                pathway.setReaction(reaction);
                enzymeModel.getReactionpathway().add(0, pathway);


                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Reaction Pathways");

            }
            if (requestedField.getName().equalsIgnoreCase(Field.literature.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());

                enzymeModel.getLiterature().add(0, "error");

                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Literature Information");

            }

        } finally {
            retriever.closeResources();
        }
        return responsePage;
    }

    /**
     * This method is an entry point that accepts the request when the search
     * home page is loaded. It then forwards the request to the search page.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
    public String viewSearchHome(Model model, HttpSession session) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        model.addAttribute("searchModel", searchModelForm);
        clearHistory(session);
        return "index";
    }

    /**
     * A wrapper of {@code postSearchResult} method, created to accept the
     * search request using GET.
     *
     * @param searchModel
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchResult(SearchModel searchModel, BindingResult result,
            Model model, HttpSession session) {
        return postSearchResult(searchModel, result, model, session);
    }

    /**
     * Processes the search request. When user enters a search text and presses
     * the submit button the request is processed here.
     *
     * @param searchModelForm
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModelForm, BindingResult result,
            Model model, HttpSession session) {
        String view = "search";
        clearHistory(session);

        if (searchModelForm != null) {
            try {
                SearchParams searchParameters = searchModelForm.getSearchparams();
                searchParameters.setSize(searchConfig.getResultsPerPage());
                SearchResults resultSet = null;
                // See if it is already there, perhaps we are paginating:
                @SuppressWarnings("unchecked")
                Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) session.getServletContext().getAttribute("searches");
                if (prevSearches == null) {
                    // Map implementation which maintains the order of access:
                    prevSearches = Collections.synchronizedMap(
                            new LinkedHashMap<String, SearchResults>(
                            searchConfig.getSearchCacheSize(), 1, true));
                    session.getServletContext().setAttribute("searches", prevSearches);
                }
                resultSet = prevSearches.get(searchParameters.getText().toLowerCase());
                if (resultSet == null) {
                    // Make a new search:
                    EnzymeFinder finder = new EnzymeFinder(searchConfig);
                    finder.getEbeyeAdapter().setConfig(ebeyeConfig);
                    finder.getUniprotAdapter().setConfig(uniprotConfig);
                    finder.getIntenzAdapter().setConfig(intenzConfig);
                    try {
                        resultSet = finder.getEnzymes(searchParameters);
                        // cache it in the session, making room if necessary:
                        synchronized (prevSearches) {
                            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
                                // remove the eldest:
                                prevSearches.remove(prevSearches.keySet().iterator().next());
                            }
                            prevSearches.put(searchParameters.getText().toLowerCase(), resultSet);
                        }
                    } catch (EnzymeFinderException ex) {
                        LOGGER.error("Unable to create the result list because an error "
                                + "has occurred in the find method! \n", ex);
                    } finally {
                        finder.closeResources();
                    }
                }

                final int numOfResults = resultSet.getSummaryentries().size();
                Pagination pagination = new Pagination(
                        numOfResults, searchParameters.getSize());
                pagination.setFirstResult(searchParameters.getStart());

                // Filter:
                //List<String> speciesFilter = searchParameters.getSpecies();
                //List<String> compoundsFilter = searchParameters.getCompounds();
                //List<String> diseasesFilter = searchParameters.getDiseases();

                List<EnzymeSummary> summaryEntryFilteredResults = new LinkedList<EnzymeSummary>();

                Set<String> speciesFilter = new TreeSet<String>();
                for (String s : searchParameters.getSpecies()) {
                    if (s != null || !s.isEmpty() || !s.equals("") || !s.equals(" ")) {
                        speciesFilter.add(s);
                    }
                }
                Set<String> diseasesFilter = new TreeSet<String>();
                for (String d : searchParameters.getDiseases()) {
                    if (d != null || !d.isEmpty() || !d.equals("") || !d.equals(" ")) {
                        diseasesFilter.add(d);
                    }
                }



                Set<String> compoundsFilter = new TreeSet<String>();
                for (String c : searchParameters.getCompounds()) {
                    if (c != null || !c.isEmpty() || !c.equals("") || !c.equals(" ")) {
                        compoundsFilter.add(c);
                    }
                }


//AUTO COMPLETE STARTS HERE
                //selected species via auto complete
                //List<String> selectedSpecies_autoComplete = searchParameters.getSelectedSpecies();
                List<String> selectedCompounds_autocomplete = searchParameters.getSelectedCompounds();
                List<String> selectedDisease_autocomplete = searchParameters.getSelectedDiseases();

                Set<String> selectedSpecies_autoComplete = new TreeSet<String>();
                for (String selectedSp : searchParameters.getSelectedSpecies()) {
                    if (selectedSp != null || !selectedSp.isEmpty() || !selectedSp.equals("") || !selectedSp.equals(" ")) {
                        selectedSpecies_autoComplete.add(selectedSp);
                    }
                }

                // list to hold all selected species both from the specie list and auto-complete
                Set<String> allSelectedItems = new TreeSet<String>();

                Species specie_to_tempList;// = null;

                //compound to be held temporary
                Compound compound_to_tempList;// = null;

                //disease to be held temporary
                uk.ac.ebi.ep.search.model.Disease disease_to_tempList;

                //a temp list to hold species selected from the auto-complete

                List<Species> tempSpecieList = resultSet.getSearchfilters().getTempSpecies();


                //a temporary list to hold all selected compounds from auto-complete
                List<Compound> tempCompoundList = resultSet.getSearchfilters().getTempCompounds();

                //a temporary List to hold all selected disease from auto complete
                List<uk.ac.ebi.ep.search.model.Disease> tempDiseaseList = resultSet.getSearchfilters().getTempDiseases();

                SearchResults searchResults = resultSet;
                // the default species in the specie List
                List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();

                //default compound list
                List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();


                List<uk.ac.ebi.ep.search.model.Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();

                //if an item is seleted, then filter the list
                if (!selectedDisease_autocomplete.isEmpty() || !selectedCompounds_autocomplete.isEmpty() || !selectedSpecies_autoComplete.isEmpty() || !speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
                    List<EnzymeSummary> filteredResults =
                            new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());


                    speciesFilter.addAll(selectedSpecies_autoComplete);
                    compoundsFilter.addAll(selectedCompounds_autocomplete);
                    diseasesFilter.addAll(selectedDisease_autocomplete);

                    CollectionUtils.filter(filteredResults,
                            new SpeciesPredicate(speciesFilter));
                    CollectionUtils.filter(filteredResults,
                            new CompoundsPredicate(compoundsFilter));
                    CollectionUtils.filter(filteredResults,
                            new DiseasesPredicate(diseasesFilter));





                    allSelectedItems.addAll(compoundsFilter);


                    allSelectedItems.addAll(diseasesFilter);


                    allSelectedItems.addAll(speciesFilter);



                    //auto complete filtering
                    //if specie(s) is selected from auto-complete, then the following executes.
                    if (selectedSpecies_autoComplete.size() > 0) {

                        for (String scienceName : selectedSpecies_autoComplete) {

                            //loop thru the species and see if the science name matches with the selected item
                            for (Species species_in_defaultList : defaultSpeciesList) {
                                if (!scienceName.isEmpty() && scienceName.equalsIgnoreCase(species_in_defaultList.getScientificname()) || scienceName.equalsIgnoreCase(species_in_defaultList.getCommonname())) {

                                    //create a specie based on the scienctific name selected
                                    specie_to_tempList = species_in_defaultList;

                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
                                    if (!tempSpecieList.contains(specie_to_tempList)) {

                                        tempSpecieList.add(specie_to_tempList);


                                    }



                                }
                            }

                        }


                    }

                    //same as above for compounds

                    if (selectedCompounds_autocomplete.size() > 0) {
                        List<String> selections = new LinkedList<String>();
                        for (String compoundName : selectedCompounds_autocomplete) {

                            //loop thru the species and see if the science name matches with the selected item
                            for (Compound compounds_in_defaultList : defaultCompoundList) {
                                if (!compoundName.isEmpty() && compoundName.equalsIgnoreCase(compounds_in_defaultList.getName())) {

                                    //create a specie based on the scienctific name selected
                                    compound_to_tempList = compounds_in_defaultList;

                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
                                    if (!tempCompoundList.contains(compound_to_tempList)) {

                                        tempCompoundList.add(compound_to_tempList);

                                    }



                                }
                            }

                        }


                    }


                    //same as above for disease

                    if (selectedDisease_autocomplete.size() > 0) {

                        for (String diseaseName : selectedDisease_autocomplete) {

                            //loop thru the species and see if the science name matches with the selected item
                            for (uk.ac.ebi.ep.search.model.Disease disease_in_defaultList : defaultDiseaseList) {
                                if (!diseaseName.isEmpty() && diseaseName.equalsIgnoreCase(disease_in_defaultList.getName())) {

                                    //create a specie based on the scienctific name selected
                                    disease_to_tempList = disease_in_defaultList;

                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
                                    if (!tempDiseaseList.contains(disease_to_tempList)) {

                                        tempDiseaseList.add(disease_to_tempList);

                                    }



                                }
                            }

                        }

                    }




                    CollectionUtils.filter(filteredResults, new DefaultPredicate(allSelectedItems));

                    //a check so that we don't get an empty page. so if the result is null, we display a no result found for the selection to the end user
                    if (filteredResults.size() <= 0) {
                        EnzymeSummary es = new EnzymeSummary();

                        filteredResults.add(es);
                    }

                    //AUTO-COMPLETE ENDS.................

                    // Create a new SearchResults, don't modify the one in session
                    SearchResults sr = new SearchResults();

                    /**
                     * filter the result based on the species selected by user
                     */
                    LinkedList<String> checkBoxParams = new LinkedList<String>(searchParameters.getSpecies());
                    LinkedList<String> checkBoxParamsAuto = new LinkedList<String>(searchParameters.getSelectedSpecies());
                    for (EnzymeSummary enzymeSummary : filteredResults) {

                        if(checkBoxParams != null && !checkBoxParams.isEmpty() && checkBoxParams.size() > 0){
                       
                            String selected = checkBoxParams.getFirst();
                              for (EnzymeAccession enzymeAccession : enzymeSummary.getRelatedspecies()) {

                                if (selected.equalsIgnoreCase(enzymeAccession.getSpecies().getScientificname())) {
                                    enzymeSummary.getUniprotaccessions().add(0, enzymeAccession.getUniprotaccessions().get(0));
                                    //enzymeSummary.setSpecies(enzymeAccession.getSpecies());
                                    enzymeSummary.setSpecies(new SpeciesDefaultWrapper(enzymeAccession.getSpecies()).getSpecies());
                                }

                            }
                        
                    }
                      if(checkBoxParamsAuto != null && checkBoxParamsAuto.size() > 0){
                       
                          String selected = checkBoxParamsAuto.getFirst();
                        
                            for (EnzymeAccession enzymeAccession : enzymeSummary.getRelatedspecies()) {

                                if (selected.equalsIgnoreCase(enzymeAccession.getSpecies().getScientificname())) {
                                    enzymeSummary.getUniprotaccessions().add(0, enzymeAccession.getUniprotaccessions().get(0));
                                    //enzymeSummary.setSpecies(enzymeAccession.getSpecies());
                                    enzymeSummary.setSpecies(new SpeciesDefaultWrapper(enzymeAccession.getSpecies()).getSpecies());
                                }

                            }
                        //}
                    }
                        // adding the updated enzyme summaries to the filtered result
                        summaryEntryFilteredResults.add(enzymeSummary);
                    }


                    // Update the number of results to paginate:
                    pagination.setNumberOfResults(filteredResults.size());
                    model.addAttribute("pagination", pagination);
                    sr.setSearchfilters(resultSet.getSearchfilters());
                    //sr.setSummaryentries(filteredResults);
                    sr.setSummaryentries(summaryEntryFilteredResults);
                    // show the total number of hits (w/o filtering):
                    sr.setTotalfound(resultSet.getTotalfound());
                    searchModelForm.setSearchresults(sr);
                } else {
                    // Show all of them:
                    searchModelForm.setSearchresults(resultSet);
                }

                model.addAttribute("searchModel", searchModelForm);
                model.addAttribute("pagination", pagination);

                addToHistory(session,
                        "searchparams.text=" + searchParameters.getText());
            } catch (Throwable e) {
                LOGGER.error("Failed search", e);
                view = "error";
            }
        }
        return view;
    }
    static final Comparator<Species> SORT_SPECIES = new Comparator<Species>() {

        public int compare(Species sp1, Species sp2) {
            if (sp1.getCommonname() == null && sp2.getCommonname() == null) {

                return sp1.getScientificname().compareTo(sp2.getScientificname());
            }
            int compare = sp1.getScientificname().compareTo(sp2.getScientificname());

            return ((compare == 0) ? sp1.getScientificname().compareTo(sp2.getScientificname()) : compare);

        }
    };

    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
    public String getSearchResult(Model model) {
        return "underconstruction";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String getAbout(Model model) {
        return "about";
    }

    private void addToHistory(HttpSession session, String s) {
        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<String>();
            session.setAttribute("history", history);
        }
        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
            history.add(s);
        }
    }

    private void clearHistory(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<String>();
            session.setAttribute("history", history);
        } else {
            history.clear();
        }
    }
}
