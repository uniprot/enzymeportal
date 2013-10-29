package uk.ac.ebi.ep.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.ebi.ep.adapter.bioportal.BioportalConfig;
import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.compare.EnzymeComparison;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;

/**
 * Controller for enzyme comparisons.
 * @author rafa
 *
 */
@Controller
public class CompareController {

    /**
     * The name of the session attribute holding the two enzymes to compare.
     */
    private static final String ENZYMES_TO_COMPARE = "enzymesToCompare";

    private final Logger LOGGER = Logger.getLogger(CompareController.class);
    
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
    private BioportalConfig bioportalConfig;

    /**
     * Updates the {@link #ENZYMES_TO_COMPARE pair of enzymes to compare}.
     * @param response the HTTP response to write to.
     * @param addAcc the UniProt accession to add. May be <code>null</code>.
     * @param enzymeName the enzyme name (only used for displaying purposes when
     *      <code>addAcc</code> is not <code>null</code>).
     * @param removeAcc the UniProt accession to remove. May be
     *      <code>null</code>.
     * @param session the user session storing the  {@link #ENZYMES_TO_COMPARE
     *      pair of enzymes to compare}.
     * @return a string (written straight to the response) containing the
     *      current list of selected enzymes to compare and the list of any
     *      accessions removed as a result of having reached the limit of
     *      comparable enzymes.
     */
    @RequestMapping(value="/ajax/compare")
    @ResponseBody
    protected String updateComparableEnzymes(HttpServletResponse response,
            @RequestParam(required = false) String addAcc,
            @RequestParam(required = false) String enzymeName,
            @RequestParam(required = false) String removeAcc,
            HttpSession session){
        @SuppressWarnings("unchecked")
        Map<String, String> enzymesToCompare =
                (Map<String, String>) session.getAttribute(ENZYMES_TO_COMPARE);
        if (enzymesToCompare == null){
            enzymesToCompare = Collections.synchronizedMap(
                    new LinkedHashMap<String, String>());
            session.setAttribute(ENZYMES_TO_COMPARE, enzymesToCompare);
        }
        if (removeAcc != null){
            String removed = enzymesToCompare.remove(removeAcc);
            if (removed == null){
                LOGGER.warn(addAcc + " deselected but not previously selected");
            }
        }
        List<String> removedAccs = new ArrayList<String>();
        if (addAcc != null){
            while (enzymesToCompare.size() > 1){
                // remove one of the existing accessions (first, oldest one):
                String removedAcc = enzymesToCompare.keySet().iterator().next();
                String removedEnzymeName = enzymesToCompare.remove(removedAcc);
                LOGGER.info("Already 2 enzymes to compare, removed "
                        + removedEnzymeName);
                removedAccs.add(removedAcc);
            }
            enzymesToCompare.put(addAcc, enzymeName);
        }
        // Write JSON:
        response.setContentType("application/json");
        JsonArrayBuilder selectedBuilder = Json.createArrayBuilder();
        for (String acc : enzymesToCompare.keySet()) {
            selectedBuilder.add(Json.createObjectBuilder()
                    .add("acc", acc)
                    .add("name", enzymesToCompare.get(acc)));
        }
        JsonArrayBuilder deselectedBuilder = Json.createArrayBuilder();
        for (String acc : removedAccs) {
            deselectedBuilder.add(Json.createObjectBuilder().add("acc", acc));
        }
        JsonObjectBuilder responseBuilder = Json.createObjectBuilder()
                .add("selected", selectedBuilder)
                .add("deselected", deselectedBuilder);
        return responseBuilder.build().toString();
    }

    /**
     * Compares the two enzymes (accessions) stored in the user session (see
     * {@link #ENZYMES_TO_COMPARE}.
     * @param model the model to populate with the comparison.
     * @param session the user session containing the enzymes' accessions.
     * @return <code>"comparison"</code> if everything goes well,
     *      <code>"error"</code> otherwise (less than two enzymes selected to
     *      compare, for example).
     * @throws EnzymeRetrieverException in case of problem retrieving the
     *      enzymes, before the comparison.
     */
    @RequestMapping(value="/compare")
    protected String getComparison(Model model, HttpSession session)
    throws EnzymeRetrieverException{
        LOGGER.debug("Creating enzyme retriever...");
        EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
        retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
        retriever.getUniprotAdapter().setConfig(uniprotConfig);
        retriever.getIntenzAdapter().setConfig(intenzConfig);
        retriever.getReactomeAdapter().setConfig(reactomeConfig);
        retriever.getChebiAdapter().setConfig(chebiConfig);
        retriever.getBioportalAdapter().setConfig(bioportalConfig);
        
        @SuppressWarnings("unchecked")
        Map<String, String> enzymesToCompare =
                (Map<String, String>) session.getAttribute(ENZYMES_TO_COMPARE);
        if (enzymesToCompare == null || enzymesToCompare.size() < 2){
            LOGGER.warn("Trying to compare less than 2 enzymes");
            return "error";
        }
        EnzymeModel models[] = new EnzymeModel[2];
        int i = 0;
        for (String acc : enzymesToCompare.keySet()) {
            models[i++] = getWholeEnzymeModel(acc, retriever);
        }
        LOGGER.debug("Comparison started...");
        EnzymeComparison comparison = new EnzymeComparison(models[0], models[1]);
        LOGGER.debug("Comparison finished");
        model.addAttribute("comparison", comparison);
        model.addAttribute("searchModel", newEmptySearchModel());
        return "comparison";
    }

    private SearchModel newEmptySearchModel() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }
    
    private EnzymeModel getWholeEnzymeModel(String acc,
            EnzymeRetriever retriever)
    throws EnzymeRetrieverException{
        LOGGER.debug("Retrieving enzyme model...");
        EnzymeModel model = retriever.getEnzyme(acc);
        model.setProteinstructure(
                retriever.getProteinStructure(acc).getProteinstructure());
        model.setReactionpathway(
                retriever.getReactionsPathways(acc).getReactionpathway());
        model.setMolecule(retriever.getMolecules(acc).getMolecule());
        model.setDisease(retriever.getDiseases(acc).getDisease());
        LOGGER.debug("Retrieved");
        return model;
    }
}
