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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.literature.CitexploreWSClientPool;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
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

    @ModelAttribute("/about")
    public SearchModel getabout(Model model) {
        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return new SearchModel();
    }

    @RequestMapping(value = "/faq")
    public SearchModel getfaq(Model model) {

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return searchModelForm;

    }
    @RequestMapping(value="/search")
    public SearchModel getSearch(Model model){
        SearchModel searchModelForm =  searchform();
        model.addAttribute("searchModel", searchModelForm);
        return searchModelForm;
    }

    @ModelAttribute("searchModelForm")
    public SearchModel searchform() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

//    /**
//     * A wrapper of {@code postSearchResult} method, created to accept the
//     * search request using GET.
//     *
//     * @param searchModel
//     * @param result
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public String getSearchResult(SearchModel searchModel, BindingResult result,
//            Model model, HttpSession session) {
//        return postSearchResult(searchModel, result, model, session);
//    }
//
//    /**
//     * Processes the search request. When user enters a search text and presses
//     * the submit button the request is processed here.
//     *
//     * @param searchModelForm
//     * @param result
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/search", method = RequestMethod.POST)
//    public String postSearchResult(SearchModel searchModelForm, BindingResult result,
//            Model model, HttpSession session) {
//        String view = "search";
//        clearHistory(session);
//
//        if (searchModelForm != null) {
//            try {
//                SearchParams searchParameters = searchModelForm.getSearchparams();
//                searchParameters.setSize(searchConfig.getResultsPerPage());
//                SearchResults resultSet = null;
//                // See if it is already there, perhaps we are paginating:
//                @SuppressWarnings("unchecked")
//                Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) session.getServletContext().getAttribute("searches");
//                if (prevSearches == null) {
//                    // Map implementation which maintains the order of access:
//                    prevSearches = Collections.synchronizedMap(
//                            new LinkedHashMap<String, SearchResults>(
//                            searchConfig.getSearchCacheSize(), 1, true));
//                    session.getServletContext().setAttribute("searches", prevSearches);
//                }
//                resultSet = prevSearches.get(searchParameters.getText().toLowerCase());
//                if (resultSet == null) {
//                    // Make a new search:
//                    EnzymeFinder finder = new EnzymeFinder(searchConfig);
//                    finder.getEbeyeAdapter().setConfig(ebeyeConfig);
//                    finder.getUniprotAdapter().setConfig(uniprotConfig);
//                    finder.getIntenzAdapter().setConfig(intenzConfig);
//                    try {
//                        resultSet = finder.getEnzymes(searchParameters);
//                        // cache it in the session, making room if necessary:
//                        synchronized (prevSearches) {
//                            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
//                                // remove the eldest:
//                                prevSearches.remove(prevSearches.keySet().iterator().next());
//                            }
//                            prevSearches.put(searchParameters.getText().toLowerCase(), resultSet);
//                        }
//                    } catch (EnzymeFinderException ex) {
//                        LOGGER.error("Unable to create the result list because an error "
//                                + "has occurred in the find method! \n", ex);
//                    } finally {
//                        finder.closeResources();
//                    }
//                }
//
//                final int numOfResults = resultSet.getSummaryentries().size();
//                Pagination pagination = new Pagination(
//                        numOfResults, searchParameters.getSize());
//                pagination.setFirstResult(searchParameters.getStart());
//
//                // Filter:
//                List<String> speciesFilter = searchParameters.getSpecies();
//                List<String> compoundsFilter = searchParameters.getCompounds();
//                List<String> diseasesFilter = searchParameters.getDiseases();
//
////both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
//                SearchResults searchResults = resultSet;
//                List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
//                resetSelectedSpecies(defaultSpeciesList);
//
//                for (String selectedItems : searchParameters.getSpecies()) {
//
//                    for (Species theSpecies : defaultSpeciesList) {
//                        if (selectedItems.equals(theSpecies.getScientificname())) {
//                            theSpecies.setSelected(true);
//                        }
//
//                    }
//                }
//
//                List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
//                resetSelectedCompounds(defaultCompoundList);
//
//                for (String SelectedCompounds : searchParameters.getCompounds()) {
//                    for (Compound theCompound : defaultCompoundList) {
//                        if (SelectedCompounds.equals(theCompound.getName())) {
//                            theCompound.setSelected(true);
//                        }
//                    }
//                }
//
//                List<uk.ac.ebi.ep.search.model.Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
//                resetSelectedDisease(defaultDiseaseList);
//
//                for (String selectedDisease : searchParameters.getDiseases()) {
//                    for (uk.ac.ebi.ep.search.model.Disease disease : defaultDiseaseList) {
//                        if (selectedDisease.equals(disease.getName())) {
//                            disease.setSelected(true);
//                        }
//                    }
//                }
//
//
//
//                // list to hold all selected species both from the specie list and auto-complete
//                Set<String> allSelectedItems = new TreeSet<String>();
//
//                //if an item is seleted, then filter the list
//                if (!speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
//                    List<EnzymeSummary> filteredResults =
//                            new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());
//
//
//                    CollectionUtils.filter(filteredResults,
//                            new SpeciesPredicate(speciesFilter));
//                    CollectionUtils.filter(filteredResults,
//                            new CompoundsPredicate(compoundsFilter));
//                    CollectionUtils.filter(filteredResults,
//                            new DiseasesPredicate(diseasesFilter));
//
//
//
//
//
//                    allSelectedItems.addAll(compoundsFilter);
//
//
//                    allSelectedItems.addAll(diseasesFilter);
//
//
//                    allSelectedItems.addAll(speciesFilter);
//
//
//                    CollectionUtils.filter(filteredResults, new DefaultPredicate(allSelectedItems));
//
////filtering ends here
//
//                    // Create a new SearchResults, don't modify the one in session
//                    SearchResults sr = new SearchResults();
//
//                    // Update the number of results to paginate:
//                    pagination.setNumberOfResults(filteredResults.size());
//
//                    model.addAttribute("pagination", pagination);
//                    sr.setSearchfilters(resultSet.getSearchfilters());
//                    sr.setSummaryentries(filteredResults);
//                    // show the total number of hits (w/o filtering):
//                    sr.setTotalfound(resultSet.getTotalfound());
//                    searchModelForm.setSearchresults(sr);
//                } else {
//                    // Show all of them:
//                    searchModelForm.setSearchresults(resultSet);
//                }
//
//                model.addAttribute("searchModel", searchModelForm);
//                model.addAttribute("pagination", pagination);
//
//                addToHistory(session,
//                        "searchparams.text=" + searchParameters.getText());
//            } catch (Throwable e) {
//                LOGGER.error("Failed search", e);
//                view = "error";
//            }
//        }
//        return view;
//    }
//    static final Comparator<Species> SORT_SPECIES = new Comparator<Species>() {
//
//        public int compare(Species sp1, Species sp2) {
//            if (sp1.getCommonname() == null && sp2.getCommonname() == null) {
//
//                return sp1.getScientificname().compareTo(sp2.getScientificname());
//            }
//            int compare = sp1.getScientificname().compareTo(sp2.getScientificname());
//
//            return ((compare == 0) ? sp1.getScientificname().compareTo(sp2.getScientificname()) : compare);
//
//        }
//    };

    private void resetSelectedSpecies(List<Species> speciesList) {
        for (Species sp : speciesList) {

            sp.setSelected(false);

        }
    }

    private void resetSelectedCompounds(List<Compound> compounds) {
        for (Compound compound : compounds) {
            compound.setSelected(false);
        }
    }

    private void resetSelectedDisease(List<uk.ac.ebi.ep.search.model.Disease> diseases) {
        for (uk.ac.ebi.ep.search.model.Disease disease : diseases) {
            disease.setSelected(false);
        }
    }

//    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
//    public String getSearchResult(Model model) {
//        return "underconstruction";
//    }
//
//    @RequestMapping(value = "/about", method = RequestMethod.GET)
//    public String getAbout(Model model) {
//        return "about";
//    }
//
//       @RequestMapping(value = "/blastSearch", method = RequestMethod.GET)
//    public String getBlastSearch(Model model) {
//        return "blastSearch";
//    }
 
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
    
    // use working code and adapt the sequence search to it. 
    ////modfications
    
    /**
     * Processes the search request. When user enters a search text and presses
     * the submit button the request is processed here.
     *
     * @param searchModel
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModel, Model model,
    		HttpSession session) {
        String view = "error";
        try {
	        // See if it is already there, perhaps we are paginating:
	        Map<String, SearchResults> prevSearches =
	        		getPreviousSearches(session.getServletContext());
                //System.out.println("SEARCH MODEL "+ searchModel);
               // System.out.println("getparam "+ searchModel.getSearchparams());
                //SearchType t = searchModel.getSearchparams().getType();
               // System.out.println("the T "+ t.name());
                 
	        String searchKey = getSearchKey(searchModel.getSearchparams());
                
               
	        SearchResults results = prevSearches.get(searchKey);
	        if (results == null){
	        	// New search:
	            clearHistory(session);
                    //SearchParams.SearchType type = searchModel.getSearchparams().getType();
                   // System.out.println("second type "+ type.name());
	            switch (searchModel.getSearchparams().getType()) {
                         
	    		case KEYWORD:
	            	results = searchKeyword(searchModel.getSearchparams());
	                cacheSearch(session.getServletContext(), searchKey, results);
	                addToHistory(session, "searchparams.text=" + searchKey);
	    			break;
	    		case SEQUENCE:
	            	view = searchSequence(model, searchModel);
	    			break;
	    		case COMPOUND:
	//            	view = postCompoundSearch(model, searchModel);
	    			break;
                            default:
	    		}
	         }
               
	        if (results != null) { // something to show
	        	searchModel.setSearchresults(results);
	        	applyFilters(searchModel);
	        	model.addAttribute("searchModel", searchModel);
	        	model.addAttribute("pagination", getPagination(searchModel));
                        clearHistory(session);
                        addToHistory(session, "searchparams.text=" + searchKey);
	        	view = "search";
	        }
        } catch (Throwable t){
            LOGGER.error("one of the search params (Text or Sequence is :", t);
//        	LOGGER.error("Unable to search:\ntype="
//        			+ searchModel.getSearchparams().getType().name()
//        			+ "\ntext="
//        			+ searchModel.getSearchparams().getText()
//        			+ "\nsequence="
//        			+ searchModel.getSearchparams().getSequence(), t);
        }
        return view;
    }
    
    /**
     * Searches by keyword.
     * @param searchParameters the search parameteres.
     * @return the search results.
     */
    private SearchResults searchKeyword(SearchParams searchParameters){
    	SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        finder.getEbeyeAdapter().setConfig(ebeyeConfig);
        finder.getUniprotAdapter().setConfig(uniprotConfig);
        finder.getIntenzAdapter().setConfig(intenzConfig);
        try {
            results = finder.getEnzymes(searchParameters);
        } catch (EnzymeFinderException ex) {
            LOGGER.error("Unable to create the result list because an error "
                    + "has occurred in the find method! \n", ex);
        } finally {
            finder.closeResources();
        }
    	return results;
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
    public String getSearchResults(SearchModel searchModel, BindingResult result,
            Model model, HttpSession session) {
        return postSearchResult(searchModel, model, session);
    }
    
         @RequestMapping(value = "/advanceSearch", method = RequestMethod.GET)
    public String getAdvanceSearch(Model model) {
        return "advanceSearch";
    }
    
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public String getSearch(Model model) {
//        return "search";
//    }
    
    /**
     * Applies filters taken from the search parameters to the search results.
     * @param searchModel
     */
    /* THIS IS computingfacts ZONE */
    private void applyFilters(SearchModel searchModel){
//                SearchParams searchParameters = searchModel.getSearchparams();
//                searchParameters.setSize(searchConfig.getResultsPerPage());
//                SearchResults resultSet = searchModel.getSearchresults();
                
                       if (searchModel != null) {
            //try {
                SearchParams searchParameters = searchModel.getSearchparams();
                searchParameters.setSize(searchConfig.getResultsPerPage());
                SearchResults resultSet = searchModel.getSearchresults();
                // See if it is already there, perhaps we are paginating:
//                @SuppressWarnings("unchecked")
//                Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) session.getServletContext().getAttribute("searches");
//                if (prevSearches == null) {
//                    // Map implementation which maintains the order of access:
//                    prevSearches = Collections.synchronizedMap(
//                            new LinkedHashMap<String, SearchResults>(
//                            searchConfig.getSearchCacheSize(), 1, true));
//                    session.getServletContext().setAttribute("searches", prevSearches);
//                }
//                resultSet = prevSearches.get(searchParameters.getText().toLowerCase());
//                if (resultSet == null) {
//                    // Make a new search:
//                    EnzymeFinder finder = new EnzymeFinder(searchConfig);
//                    finder.getEbeyeAdapter().setConfig(ebeyeConfig);
//                    finder.getUniprotAdapter().setConfig(uniprotConfig);
//                    finder.getIntenzAdapter().setConfig(intenzConfig);
//                    try {
//                        resultSet = finder.getEnzymes(searchParameters);
//                        // cache it in the session, making room if necessary:
//                        synchronized (prevSearches) {
//                            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
//                                // remove the eldest:
//                                prevSearches.remove(prevSearches.keySet().iterator().next());
//                            }
//                            prevSearches.put(searchParameters.getText().toLowerCase(), resultSet);
//                        }
//                    } catch (EnzymeFinderException ex) {
//                        LOGGER.error("Unable to create the result list because an error "
//                                + "has occurred in the find method! \n", ex);
//                    } finally {
//                        finder.closeResources();
//                    }
//                }

                final int numOfResults = resultSet.getSummaryentries().size();
                Pagination pagination = new Pagination(
                        numOfResults, searchParameters.getSize());
                pagination.setFirstResult(searchParameters.getStart());

                // Filter:
                List<String> speciesFilter = searchParameters.getSpecies();
                List<String> compoundsFilter = searchParameters.getCompounds();
                List<String> diseasesFilter = searchParameters.getDiseases();

//both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
                SearchResults searchResults = resultSet;
                List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
                resetSelectedSpecies(defaultSpeciesList);

                for (String selectedItems : searchParameters.getSpecies()) {

                    for (Species theSpecies : defaultSpeciesList) {
                        if (selectedItems.equals(theSpecies.getScientificname())) {
                            theSpecies.setSelected(true);
                        }

                    }
                }

                List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
                resetSelectedCompounds(defaultCompoundList);

                for (String SelectedCompounds : searchParameters.getCompounds()) {
                    for (Compound theCompound : defaultCompoundList) {
                        if (SelectedCompounds.equals(theCompound.getName())) {
                            theCompound.setSelected(true);
                        }
                    }
                }

                List<uk.ac.ebi.ep.search.model.Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
                resetSelectedDisease(defaultDiseaseList);

                for (String selectedDisease : searchParameters.getDiseases()) {
                    for (uk.ac.ebi.ep.search.model.Disease disease : defaultDiseaseList) {
                        if (selectedDisease.equals(disease.getName())) {
                            disease.setSelected(true);
                        }
                    }
                }



                // list to hold all selected species both from the specie list and auto-complete
                Set<String> allSelectedItems = new TreeSet<String>();

                //if an item is seleted, then filter the list
                if (!speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
                    List<EnzymeSummary> filteredResults =
                            new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());


                    CollectionUtils.filter(filteredResults,
                            new SpeciesPredicate(speciesFilter));
                    CollectionUtils.filter(filteredResults,
                            new CompoundsPredicate(compoundsFilter));
                    CollectionUtils.filter(filteredResults,
                            new DiseasesPredicate(diseasesFilter));





                    allSelectedItems.addAll(compoundsFilter);


                    allSelectedItems.addAll(diseasesFilter);


                    allSelectedItems.addAll(speciesFilter);


                    CollectionUtils.filter(filteredResults, new DefaultPredicate(allSelectedItems));
                    
                    
                    //adapting the sequece code
                    
                       // Create a new SearchResults, don't modify the one in session
                    SearchResults sr = new SearchResults();

                    // Update the number of results to paginate:
                    pagination.setNumberOfResults(filteredResults.size());

                    //model.addAttribute("pagination", pagination);
                    sr.setSearchfilters(resultSet.getSearchfilters());
                    sr.setSummaryentries(filteredResults);
                    // show the total number of hits (w/o filtering):
                    sr.setTotalfound(resultSet.getTotalfound());
                    searchModel.setSearchresults(sr);

//filtering ends here

//                    // Create a new SearchResults, don't modify the one in session
//                    SearchResults sr = new SearchResults();
//
//                    // Update the number of results to paginate:
//                    pagination.setNumberOfResults(filteredResults.size());
//
//                    model.addAttribute("pagination", pagination);
//                    sr.setSearchfilters(resultSet.getSearchfilters());
//                    sr.setSummaryentries(filteredResults);
//                    // show the total number of hits (w/o filtering):
//                    sr.setTotalfound(resultSet.getTotalfound());
//                    searchModelForm.setSearchresults(sr);
//                } else {
//                    // Show all of them:
//                    searchModelForm.setSearchresults(resultSet);
//                }
//
//                model.addAttribute("searchModel", searchModelForm);
//                model.addAttribute("pagination", pagination);
//
//                addToHistory(session,
//                        "searchparams.text=" + searchParameters.getText());
//            } catch (Throwable e) {
//                LOGGER.error("Failed search", e);
//                view = "error";
//            }
        }
                

//                // Filter:
//                //List<String> speciesFilter = searchParameters.getSpecies();
//                //List<String> compoundsFilter = searchParameters.getCompounds();
//                //List<String> diseasesFilter = searchParameters.getDiseases();
//
//                List<EnzymeSummary> summaryEntryFilteredResults = new LinkedList<EnzymeSummary>();
//
//                Set<String> speciesFilter = new TreeSet<String>();
//                for (String s : searchParameters.getSpecies()) {
//                    if (s != null || !s.isEmpty() || !s.equals("") || !s.equals(" ")) {
//                        speciesFilter.add(s);
//                    }
//                }
//                Set<String> diseasesFilter = new TreeSet<String>();
//                for (String d : searchParameters.getDiseases()) {
//                    if (d != null || !d.isEmpty() || !d.equals("") || !d.equals(" ")) {
//                        diseasesFilter.add(d);
//                    }
//                }
//
//
//
//                Set<String> compoundsFilter = new TreeSet<String>();
//                for (String c : searchParameters.getCompounds()) {
//                    if (c != null || !c.isEmpty() || !c.equals("") || !c.equals(" ")) {
//                        compoundsFilter.add(c);
//                    }
//                }
//
//
////AUTO COMPLETE STARTS HERE
//                //selected species via auto complete
//                //List<String> selectedSpecies_autoComplete = searchParameters.getSelectedSpecies();
//                List<String> selectedCompounds_autocomplete = searchParameters.getSelectedCompounds();
//                List<String> selectedDisease_autocomplete = searchParameters.getSelectedDiseases();
//
//                Set<String> selectedSpecies_autoComplete = new TreeSet<String>();
//                for (String selectedSp : searchParameters.getSelectedSpecies()) {
//                    if (selectedSp != null || !selectedSp.isEmpty() || !selectedSp.equals("") || !selectedSp.equals(" ")) {
//                        selectedSpecies_autoComplete.add(selectedSp);
//                    }
//                }
//
//                // list to hold all selected species both from the specie list and auto-complete
//                Set<String> allSelectedItems = new TreeSet<String>();
//
//                Species specie_to_tempList;// = null;
//
//                //compound to be held temporary
//                Compound compound_to_tempList;// = null;
//
//                //disease to be held temporary
//                uk.ac.ebi.ep.search.model.Disease disease_to_tempList;
//
//                //a temp list to hold species selected from the auto-complete
//
//                List<Species> tempSpecieList = resultSet.getSearchfilters().getTempSpecies();
//
//
//                //a temporary list to hold all selected compounds from auto-complete
//                List<Compound> tempCompoundList = resultSet.getSearchfilters().getTempCompounds();
//
//                //a temporary List to hold all selected disease from auto complete
//                List<uk.ac.ebi.ep.search.model.Disease> tempDiseaseList = resultSet.getSearchfilters().getTempDiseases();
//
//                SearchResults searchResults = resultSet;
//                // the default species in the specie List
//                List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
//
//                //default compound list
//                List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
//
//
//                List<uk.ac.ebi.ep.search.model.Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
//
//                //if an item is seleted, then filter the list
//                if (!selectedDisease_autocomplete.isEmpty() || !selectedCompounds_autocomplete.isEmpty() || !selectedSpecies_autoComplete.isEmpty() || !speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
//                    List<EnzymeSummary> filteredResults =
//                            new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());
//
//
//                    speciesFilter.addAll(selectedSpecies_autoComplete);
//                    compoundsFilter.addAll(selectedCompounds_autocomplete);
//                    diseasesFilter.addAll(selectedDisease_autocomplete);
//
//                    CollectionUtils.filter(filteredResults,
//                            new SpeciesPredicate(speciesFilter));
//                    CollectionUtils.filter(filteredResults,
//                            new CompoundsPredicate(compoundsFilter));
//                    CollectionUtils.filter(filteredResults,
//                            new DiseasesPredicate(diseasesFilter));
//
//
//
//
//
//                    allSelectedItems.addAll(compoundsFilter);
//
//
//                    allSelectedItems.addAll(diseasesFilter);
//
//
//                    allSelectedItems.addAll(speciesFilter);
//
//
//
//                    //auto complete filtering
//                    //if specie(s) is selected from auto-complete, then the following executes.
//                    if (selectedSpecies_autoComplete.size() > 0) {
//
//                        for (String scienceName : selectedSpecies_autoComplete) {
//
//                            //loop thru the species and see if the science name matches with the selected item
//                            for (Species species_in_defaultList : defaultSpeciesList) {
//                                if (!scienceName.isEmpty() && scienceName.equalsIgnoreCase(species_in_defaultList.getScientificname()) || scienceName.equalsIgnoreCase(species_in_defaultList.getCommonname())) {
//
//                                    //create a specie based on the scienctific name selected
//                                    specie_to_tempList = species_in_defaultList;
//
//                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
//                                    if (!tempSpecieList.contains(specie_to_tempList)) {
//
//                                        tempSpecieList.add(specie_to_tempList);
//
//
//                                    }
//
//
//
//                                }
//                            }
//
//                        }
//
//
//                    }
//
//                    //same as above for compounds
//
//                    if (selectedCompounds_autocomplete.size() > 0) {
//                        List<String> selections = new LinkedList<String>();
//                        for (String compoundName : selectedCompounds_autocomplete) {
//
//                            //loop thru the species and see if the science name matches with the selected item
//                            for (Compound compounds_in_defaultList : defaultCompoundList) {
//                                if (!compoundName.isEmpty() && compoundName.equalsIgnoreCase(compounds_in_defaultList.getName())) {
//
//                                    //create a specie based on the scienctific name selected
//                                    compound_to_tempList = compounds_in_defaultList;
//
//                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
//                                    if (!tempCompoundList.contains(compound_to_tempList)) {
//
//                                        tempCompoundList.add(compound_to_tempList);
//
//                                    }
//
//
//
//                                }
//                            }
//
//                        }
//
//
//                    }
//
//
//                    //same as above for disease
//
//                    if (selectedDisease_autocomplete.size() > 0) {
//
//                        for (String diseaseName : selectedDisease_autocomplete) {
//
//                            //loop thru the species and see if the science name matches with the selected item
//                            for (uk.ac.ebi.ep.search.model.Disease disease_in_defaultList : defaultDiseaseList) {
//                                if (!diseaseName.isEmpty() && diseaseName.equalsIgnoreCase(disease_in_defaultList.getName())) {
//
//                                    //create a specie based on the scienctific name selected
//                                    disease_to_tempList = disease_in_defaultList;
//
//                                    //if the specie is not already in the temp List, then add it and filter the enzyme summary list based on the selected species.
//                                    if (!tempDiseaseList.contains(disease_to_tempList)) {
//
//                                        tempDiseaseList.add(disease_to_tempList);
//
//                                    }
//
//
//
//                                }
//                            }
//
//                        }
//
//                    }
//
//
//
//
//                    CollectionUtils.filter(filteredResults, new DefaultPredicate(allSelectedItems));
//
//                    //a check so that we don't get an empty page. so if the result is null, we display a no result found for the selection to the end user
//                    if (filteredResults.size() <= 0) {
//                        EnzymeSummary es = new EnzymeSummary();
//
//                        filteredResults.add(es);
//                    }
//
//                    //AUTO-COMPLETE ENDS.................
//
//                    // Create a new SearchResults, don't modify the one in session
//                    SearchResults sr = new SearchResults();
//
//                    /**
//                     * filter the result based on the species selected by user
//                     */
//                    LinkedList<String> checkBoxParams = new LinkedList<String>(searchParameters.getSpecies());
//                    LinkedList<String> checkBoxParamsAuto = new LinkedList<String>(searchParameters.getSelectedSpecies());
//                    for (EnzymeSummary enzymeSummary : filteredResults) {
//
//                        if(checkBoxParams != null && !checkBoxParams.isEmpty() && checkBoxParams.size() > 0){
//                       
//                            String selected = checkBoxParams.getFirst();
//                              for (EnzymeAccession enzymeAccession : enzymeSummary.getRelatedspecies()) {
//
//                                if (selected.equalsIgnoreCase(enzymeAccession.getSpecies().getScientificname())) {
//                                    enzymeSummary.getUniprotaccessions().add(0, enzymeAccession.getUniprotaccessions().get(0));
//                                    //enzymeSummary.setSpecies(enzymeAccession.getSpecies());
//                                    enzymeSummary.setSpecies(new SpeciesDefaultWrapper(enzymeAccession.getSpecies()).getSpecies());
//                                }
//
//                            }
//                        
//                    }
//                      if(checkBoxParamsAuto != null && checkBoxParamsAuto.size() > 0){
//                       
//                          String selected = checkBoxParamsAuto.getFirst();
//                        
//                            for (EnzymeAccession enzymeAccession : enzymeSummary.getRelatedspecies()) {
//
//                                if (selected.equalsIgnoreCase(enzymeAccession.getSpecies().getScientificname())) {
//                                    enzymeSummary.getUniprotaccessions().add(0, enzymeAccession.getUniprotaccessions().get(0));
//                                    //enzymeSummary.setSpecies(enzymeAccession.getSpecies());
//                                    enzymeSummary.setSpecies(new SpeciesDefaultWrapper(enzymeAccession.getSpecies()).getSpecies());
//                                }
//
//                            }
//                        //}
//                    }
//                        // adding the updated enzyme summaries to the filtered result
//                        summaryEntryFilteredResults.add(enzymeSummary);
//                    }
//
//
//                    sr.setSearchfilters(resultSet.getSearchfilters());
//                    //sr.setSummaryentries(filteredResults);
//                    sr.setSummaryentries(summaryEntryFilteredResults);
//                    // show the total number of hits (w/o filtering):
//                    sr.setTotalfound(resultSet.getTotalfound());
//                    searchModel.setSearchresults(sr);
//                } else {
//                    // Show all of them:
//                    searchModel.setSearchresults(resultSet);
                }

    }
    
    /**
     * Retrieves any previous searches stored in the application context.
     * @param servletContext the application context.
     * @return a map of searches to results.
     */
    @SuppressWarnings("unchecked")
	private Map<String, SearchResults> getPreviousSearches(
			ServletContext servletContext){
        Map<String, SearchResults> prevSearches = (Map<String, SearchResults>)
        		servletContext.getAttribute("searches");
        if (prevSearches == null) {
            // Map implementation which maintains the order of access:
            prevSearches = Collections.synchronizedMap(
                    new LinkedHashMap<String, SearchResults>(
                    searchConfig.getSearchCacheSize(), 1, true));
            servletContext.setAttribute("searches", prevSearches);
        }
        return prevSearches;
    }
    
    /**
     * Stores a search result in the application context.
     * @param servletContext the application context.
     * @param searchKey the key to use for the search results in the table.
     * @param searchResult the search results.
     */
    private void cacheSearch(ServletContext servletContext, String searchKey,
    		SearchResults searchResult){
        Map<String, SearchResults> prevSearches =
        		getPreviousSearches(servletContext);
    	synchronized (prevSearches) {
            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
                // remove the eldest:
                prevSearches.remove(prevSearches.keySet().iterator().next());
            }
            prevSearches.put(searchKey, searchResult);
    	}
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
    
   
    
    /**
     * Sends a search to a BLAST server. The job ID returned is stored in the
     * model as <code>jobId</code> attribute.
     * @param model the Spring model.
     * @param searchModel the EP search model.
     * @return the view according to the search: <code>running</code> if
     * 		everything went ok and the search job is running, <code>error</code>
     * 		otherwise.
     */
    private String searchSequence(Model model, SearchModel searchModel){
    	String view = "error";
    	try {
        	EnzymeFinder finder = new EnzymeFinder(searchConfig);
			String sequence = searchModel.getSearchparams().getSequence()
					.trim().toUpperCase();
			String jobId = finder.blast(sequence);
			searchModel.getSearchparams().setPrevioustext(sequence);
			model.addAttribute("jobId", jobId);
			LOGGER.debug("BLAST job running: " + jobId);
			view = "running";
		} catch (NcbiBlastClientException e) {
			LOGGER.error(e);
		}
    	return view;
    }
    
    @RequestMapping(value = "/checkJob", method = RequestMethod.POST)
    public String checkJob (@RequestParam String jobId, Model model,
    		SearchModel searchModel, HttpSession session){
    	String view = null;
    	try {
	    	EnzymeFinder enzymeFinder = new EnzymeFinder(searchConfig);
            enzymeFinder.getEbeyeAdapter().setConfig(ebeyeConfig);
            enzymeFinder.getUniprotAdapter().setConfig(uniprotConfig);
            enzymeFinder.getIntenzAdapter().setConfig(intenzConfig);
			NcbiBlastClient.Status status = enzymeFinder.getBlastStatus(jobId);
	    	switch (status) {
			case ERROR:
			case FAILURE:
			case NOT_FOUND:
				LOGGER.error("Blast job returned status " + status);
				view = "error";
				break;
			case FINISHED:
				LOGGER.debug("BLAST job finished");
				SearchResults results = enzymeFinder.getBlastResult(jobId);
                SearchParams searchParams = searchModel.getSearchparams();
				String resultKey = getSearchKey(searchParams);
				cacheSearch(session.getServletContext(), resultKey, results);
                addToHistory(session, "searchparams.sequence=" + resultKey);
                searchParams.setSize(searchConfig.getResultsPerPage());
                searchModel.setSearchresults(results);
                model.addAttribute("searchModel", searchModel);
                model.addAttribute("pagination", getPagination(searchModel));
				view = "search";
				break;
			case RUNNING:
				model.addAttribute("jobId", jobId);
				view = "running";
				break;
                        default:
			}
                
    	} catch (Throwable t){
    		LOGGER.error("While checking BLAST job", t);
    		view = "error";
    	}
    	return view;
    }
    
    /**
     * Processes a string to normalise it to use as a key in the application
     * cache.
     * @param searchParams the search parameters, including the original search
     * 		text from the user.
     * @return A normalised string.
     */
    private String getSearchKey(SearchParams searchParams){
        
    	String key = null;
       
    	switch (searchParams.getType()) {
		case KEYWORD:
			key = searchParams.getText().trim().toLowerCase();
			break;
		case SEQUENCE:
			key = searchParams.getSequence().trim().toUpperCase()
					.replaceAll("[\n\r]", "");
			break;
		case COMPOUND:
			throw new NotImplementedException("Compound structure search");
                    default:
                        key = searchParams.getText().trim().toLowerCase(); 
		}
    	return key;
    }

        /**
     * Adds a pagination object to the model, suitable to the search results and
     * search parameters.
     * @param searchModel the search model including the search parameters
     * 		(including pagination start).
     * @return a pagination.
     */
	private Pagination getPagination(SearchModel searchModel) {
        Pagination pagination = new Pagination(
		        searchModel.getSearchresults().getSummaryentries().size(),
		        searchConfig.getResultsPerPage());
		pagination.setFirstResult(searchModel.getSearchparams().getStart());
		return pagination;
	}

//    private void addToHistory(HttpSession session, String s) {
//        @SuppressWarnings("unchecked")
//        List<String> history = (List<String>) session.getAttribute("history");
//        if (history == null) {
//            history = new ArrayList<String>();
//            session.setAttribute("history", history);
//        }
//        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
//            history.add(s);
//        }
//    }
//
//    private void clearHistory(HttpSession session) {
//        @SuppressWarnings("unchecked")
//        List<String> history = (List<String>) session.getAttribute("history");
//        if (history == null) {
//            history = new ArrayList<String>();
//            session.setAttribute("history", history);
//        } else {
//            history.clear();
//        }
//    }
        
        
        
        
        

}
