package uk.ac.ebi.ep.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.adapter.bioportal.BioportalConfig;
import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.literature.CitexploreWSClientPool;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.filter.CompoundsPredicate;
import uk.ac.ebi.ep.core.filter.DiseasesPredicate;
import uk.ac.ebi.ep.core.filter.SpeciesPredicate;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.core.search.HtmlUtility;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.enzyme.model.CountableMolecules;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.enzymes.EnzymeEntry;
import uk.ac.ebi.ep.enzymes.EnzymeSubSubclass;
import uk.ac.ebi.ep.enzymes.EnzymeSubclass;
import uk.ac.ebi.ep.enzymes.IntenzEnzyme;
import uk.ac.ebi.ep.mm.CustomXRef;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchParams.SearchType;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/> $LastChangedDate: 2012-03-26 11:53:57
 * +0100 (Mon, 26 Mar 2012) $ <br/> $Author$
 * @author $Author$
 */
@Controller
public class SearchController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SearchController.class);
    private static final String ENZYME_MODEL = "enzymeModel";
    private static final String ERROR = "error";

    private enum ResponsePage {

        ENTRY, ERROR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    @Autowired
    private ReactomeConfig reactomeConfig;
    @Autowired
    private ChebiConfig chebiConfig;
    @Autowired
    private LiteratureConfig literatureConfig;
    @Autowired
    private BioportalConfig bioportalConfig;

    @PostConstruct
    public void init() {
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
                    retriever.getBioportalAdapter().setConfig(bioportalConfig);
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
            model.addAttribute(ENZYME_MODEL, enzymeModel);
            addToHistory(session, accession);
            // If we got here from a bookmark, the summary might not be cached:
            String summId = Functions.getSummaryBasketId(enzymeModel);
            @SuppressWarnings("unchecked")
            final Map<String, EnzymeSummary> sls = (Map<String, EnzymeSummary>) session.getAttribute(Attribute.lastSummaries.getName());
            if (sls == null) {
                setLastSummaries(session, Collections.singletonList(
                        (EnzymeSummary) enzymeModel));
            } else if (sls.get(summId) == null) {
                sls.put(summId, enzymeModel);
            }
        } catch (Exception ex) {
            // FIXME: this is an odd job to signal an error for the JSP!
            LOGGER.error("Unable to retrieve the entry!", ex);
            if (requestedField.getName().equalsIgnoreCase(Field.diseaseDrugs.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setName("Diseases");
                enzymeModel.setRequestedfield(requestedField.name());
                Disease d = new Disease();
                d.setName(ERROR);
                enzymeModel.getDisease().add(0, d);
                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Disease Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.molecules.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Small Molecues");
                Molecule molecule = new Molecule();
                molecule.setName(ERROR);
                ChemicalEntity chemicalEntity = new ChemicalEntity();
                chemicalEntity.setDrugs(new CountableMolecules());
                chemicalEntity.getDrugs().setMolecule(new ArrayList<Molecule>());
                chemicalEntity.getDrugs().getMolecule().add(molecule);
                enzymeModel.setMolecule(chemicalEntity);
                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Molecules Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.enzyme.getName())) {

                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Enzymes");
                Enzyme enzyme = new Enzyme();
                enzyme.getEnzymetype().add(0, ERROR);
                enzymeModel.setEnzyme(enzyme);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Enzymes");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.proteinStructure.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Protein Structures");
                ProteinStructure structure = new ProteinStructure();
                structure.setName(ERROR);
                enzymeModel.getProteinstructure().add(0, structure);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving ProteinStructure");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.reactionsPathways.getName())) {
                enzymeModel = new EnzymeModel();

                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Reactions and Pathways");
                ReactionPathway pathway = new ReactionPathway();
                EnzymeReaction reaction = new EnzymeReaction();
                reaction.setName(ERROR);
                pathway.setReaction(reaction);
                enzymeModel.getReactionpathway().add(0, pathway);


                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Reaction Pathways");

            }
            if (requestedField.getName().equalsIgnoreCase(Field.literature.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Literatures");

                enzymeModel.getLiterature().add(0, ERROR);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
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
        //searchParams.setText("Enter a name to search");
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

    @RequestMapping(value = "/search")
    public SearchModel getSearch(Model model) {
        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return searchModelForm;
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
            Model model, HttpSession session, HttpServletRequest request) {
        return postSearchResult(searchModel, model, session, request);

    }

    /**
     * Processes the search request. When user enters a search text and presses
     * the submit button the request is processed here.
     *
     * @param searchModel
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModel, Model model,
            HttpSession session, HttpServletRequest request) {
        String view = "error";

        String searchKey = null;
        SearchResults results = null;


        try {


            // See if it is already there, perhaps we are paginating:
            Map<String, SearchResults> prevSearches =
                    getPreviousSearches(session.getServletContext());
            searchKey = getSearchKey(searchModel.getSearchparams());
            results = prevSearches.get(searchKey);
            if (results == null) {
                // New search:
                clearHistory(session);

                switch (searchModel.getSearchparams().getType()) {
                    case KEYWORD:
                        results = searchKeyword(searchModel.getSearchparams());
                        break;
                    case SEQUENCE:
                        view = searchSequence(model, searchModel);
                        break;
                    case COMPOUND:
                        results = searchCompound(model, searchModel);
                        break;
                    default:
                }
            }
            //}
            if (results != null) { // something to show
                cacheSearch(session.getServletContext(), searchKey, results);
                setLastSummaries(session, results.getSummaryentries());
                searchModel.setSearchresults(results);
                applyFilters(searchModel, request);
                model.addAttribute("searchModel", searchModel);
                model.addAttribute("pagination", getPagination(searchModel));
                clearHistory(session);
                addToHistory(session, searchModel.getSearchparams().getType(),
                        searchKey);
                view = "search";
            }
        } catch (Throwable t) {
            LOGGER.error("one of the search params (Text or Sequence is :", t);
        }

        return view;
    }

    /**
     * Uses a finder to search by compound ID.
     *
     * @param model the view model.
     * @param searchModel the search model, including a compound ID as the
     * <code>text</code> parameter.
     * @return the search results, or <code>null</code> if nothing found.
     * @since 1.0.27
     */
    private SearchResults searchCompound(Model model, SearchModel searchModel) {
        SearchResults results = null;
        EnzymeFinder finder = null;
        try {
            finder = new EnzymeFinder(searchConfig);
            finder.getUniprotAdapter().setConfig(uniprotConfig);
            finder.getIntenzAdapter().setConfig(intenzConfig);
            results = finder.getEnzymesByCompound(searchModel.getSearchparams());
            searchModel.setSearchresults(results);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute("pagination", getPagination(searchModel));
        } catch (Exception e) {
            LOGGER.error("Unable to get enzymes by compound", e);
        } finally {
            if (finder != null) {
                finder.closeResources();
            }
        }
        return results;
    }

    @RequestMapping(value = "/advanceSearch",
    method = {RequestMethod.GET, RequestMethod.POST})
    public String getAdvanceSearch(Model model) {
        return "advanceSearch";
    }

    public String resolveSpecialCharacters(String data) {

        SpecialCharacters xchars = SpecialCharacters.getInstance(null);
        EncodingType[] encodings = {
            EncodingType.CHEBI_CODE,
            EncodingType.COMPOSED,
            EncodingType.EXTENDED_HTML,
            EncodingType.GIF,
            EncodingType.HTML,
            EncodingType.HTML_CODE,
            EncodingType.JPG,
            EncodingType.SWISSPROT_CODE,
            EncodingType.UNICODE
        };

        if (!xchars.validate(data)) {
            LOGGER.warn("SPECIAL CHARACTER PARSING ERROR : This is not a valid xchars string!" + data);

        }


        return xchars.xml2Display(data, EncodingType.CHEBI_CODE);
    }

    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
    public String getUnderconstruction(Model model) {
        return "underconstruction";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String getAbout(Model model) {
        return "about";
    }

    /**
     * Sends a search to a BLAST server. The job ID returned is stored in the
     * model as
     * <code>jobId</code> attribute.
     *
     * @param model the Spring model.
     * @param searchModel the EP search model.
     * @return the view according to the search: <code>running</code> if
     * everything went ok and the search job is running, <code>error</code>
     * otherwise.
     */
    private String searchSequence(Model model, SearchModel searchModel) {
        String view = "error";
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        try {
            String sequence = searchModel.getSearchparams().getSequence()
                    .trim().toUpperCase();
            String jobId = finder.blast(sequence);
            searchModel.getSearchparams().setPrevioustext(sequence);
            model.addAttribute("jobId", jobId);
            LOGGER.debug("BLAST job running: " + jobId);
            view = "running";
        } catch (NcbiBlastClientException e) {
            LOGGER.error(e);
        } finally {
            finder.closeResources();
        }
        return view;
    }

    @RequestMapping(value = "/checkJob", method = RequestMethod.POST)
    public String checkJob(@RequestParam String jobId, Model model,
            SearchModel searchModel, HttpSession session) {
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
                    //addToHistory(session, "searchparams.sequence=" + resultKey);
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

        } catch (Throwable t) {
            LOGGER.error("While checking BLAST job", t);
            view = "error";
        }
        return view;
    }
}
