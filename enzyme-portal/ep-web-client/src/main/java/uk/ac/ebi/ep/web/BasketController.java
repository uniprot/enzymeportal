package uk.ac.ebi.ep.web;

import java.util.Collections;
import java.util.LinkedHashMap;
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
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;

/**
 * Controller for basket actions.
 * @author rafa
 * @since 1.1.0
 */
@Controller
public class BasketController {

    private final Logger LOGGER = Logger.getLogger(BasketController.class);
    
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
     * Updates the basket with enzymes (summaries) to compare or download.
     * @param response the HTTP response to write to.
     * @param id the basket ID to add/remove (see
     *      {@link Functions#getSummaryBasketId(EnzymeSummary)}.
     * @param checked if <code>true</code>, add the accession; if
     *      <code>false</code>, remove it.
     * @param session the user session.
     * @return a string with the current number of enzymes in the basket.
     */
    @RequestMapping(value="/ajax/basket")
    @ResponseBody
    protected String updateBasket(HttpServletResponse response,
            @RequestParam String id, @RequestParam Boolean checked,
            HttpSession session){
        @SuppressWarnings("unchecked")
        Map<String, EnzymeSummary> lastSummaries = (Map<String, EnzymeSummary>)
                session.getAttribute(Attribute.lastSummaries.name());
        @SuppressWarnings("unchecked")
        Map<String, EnzymeSummary> basket = (Map<String, EnzymeSummary>)
                session.getAttribute(Attribute.basket.name());
        if (basket == null){
            basket = Collections.synchronizedMap(
                    new LinkedHashMap<String, EnzymeSummary>());
            session.setAttribute(Attribute.basket.name(), basket);
        }
        final EnzymeSummary summary = lastSummaries.get(id);
        final String basketId = Functions.getSummaryBasketId(summary);
        if (checked){
            basket.put(basketId, summary);
        } else {
            basket.remove(basketId);
        }
        response.setContentType("text/plain");
        return String.valueOf(basket.size());
    }
    
    @RequestMapping(value="/basket")
    protected String getBasket(Model model){
        model.addAttribute("searchModel", newEmptySearchModel());
        return Attribute.basket.name();
    }

    /**
     * Compares the two enzymes (accessions) stored in the user session (see
     * {@link #Attribute.basket.name()}.
     * @param model the model to populate with the comparison.
     * @param session the user session containing the enzymes' accessions.
     * @param accs UniProt accessions of the enzymes to be compared (exactly 2).
     * @return <code>"comparison"</code> if everything goes well,
     *      <code>"error"</code> otherwise (less than two enzymes selected to
     *      compare, for example).
     * @throws EnzymeRetrieverException in case of problem retrieving the
     *      enzymes, before the comparison.
     */
    @RequestMapping(value="/compare")
    protected String getComparison(Model model, HttpSession session,
            @RequestParam(value="acc") String[] accs)
    throws EnzymeRetrieverException{
        model.addAttribute("searchModel", newEmptySearchModel());
        try {
            LOGGER.debug("Creating enzyme retriever...");
            EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
            retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
            retriever.getUniprotAdapter().setConfig(uniprotConfig);
            retriever.getIntenzAdapter().setConfig(intenzConfig);
            retriever.getReactomeAdapter().setConfig(reactomeConfig);
            retriever.getChebiAdapter().setConfig(chebiConfig);
            retriever.getBioportalAdapter().setConfig(bioportalConfig);

            EnzymeModel models[] = new EnzymeModel[2];
            int i = 0;
            for (String acc : accs) {
                if (acc.length() == 0) continue;
                // TODO: improve performance here
                models[i++] = getWholeEnzymeModel(acc, retriever);
                if (i == 2) break;
            }
            LOGGER.debug("Comparison started...");
            EnzymeComparison comparison =
                    new EnzymeComparison(models[0], models[1]);
            LOGGER.debug("Comparison finished");
            model.addAttribute("comparison", comparison);
            return "comparison";
        } catch (Exception e){
            LOGGER.error(e);
            model.addAttribute("errorCode", "comparison");
            model.addAttribute("errorParam", accs[0] + "," + accs[1]);
            return "error";
        }
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
