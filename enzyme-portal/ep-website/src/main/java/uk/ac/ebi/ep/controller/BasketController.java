package uk.ac.ebi.ep.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.base.comparison.EnzymeComparison;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.exceptions.EnzymeRetrieverException;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;

/**
 * Controller for basket actions.
 *
 * @author rafa
 * @since 1.1.0
 */
@Controller
public class BasketController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(BasketController.class);


    /**
     * Updates the basket with enzymes (summaries) to compare or download.
     *
     * @param response the HTTP response to write to.
     * @param id the basket ID to add/remove (see
     * {@link Functions#getSummaryBasketId(EnzymeSummary)}. It may be a
     * semicolon-separated list of basket IDs, which will be processed in block.
     * @param checked if <code>true</code>, add the accession; if
     * <code>false</code>, remove it.
     * @param session the user session.
     * @return a string with the current number of enzymes in the basket.
     */
    @RequestMapping(value = "/ajax/basket")
    @ResponseBody
    protected String updateBasket(HttpServletResponse response,
            @RequestParam String id, @RequestParam Boolean checked,
            HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<String, UniprotEntry> lastSummaries = (Map<String, UniprotEntry>) session.getAttribute(Attribute.lastSummaries.name());
        @SuppressWarnings("unchecked")
        Map<String, UniprotEntry> basket = (Map<String, UniprotEntry>) session.getAttribute(Attribute.basket.name());
        if (basket == null) {
            basket = Collections.synchronizedMap(
                    new LinkedHashMap<String, UniprotEntry>());
            session.setAttribute(Attribute.basket.name(), basket);
        }
        for (String basketId : id.split(";")) {
            if (checked && lastSummaries != null) {
                final UniprotEntry summary = lastSummaries.get(basketId);
                if (summary != null) {
                    basket.put(basketId, summary);
                }
                if (summary == null) {
                    // build a fresh one:
                    // TODO
                }
            } else {
                basket.remove(basketId);
            }
        }
        response.setContentType("text/plain");
        return String.valueOf(basket.size());
    }

    @RequestMapping(value = "/basket")
    protected String getBasket(Model model) {
        model.addAttribute("searchModel", newEmptySearchModel());
        model.addAttribute("searchConfig", searchConfig);
        return Attribute.basket.name();
    }

    /**
     * Compares the two enzymes (accessions) stored in the user session (see
     * {@link #Attribute.basket.name()}.
     *
     * @param model the model to populate with the comparison.
     * @param session the user session containing the enzymes' accessions.
     * @param accs UniProt accessions of the enzymes to be compared (exactly 2).
     * @return <code>"comparison"</code> if everything goes well,
     * <code>"error"</code> otherwise (less than two enzymes selected to
     * compare, for example).
     * @throws EnzymeRetrieverException in case of problem retrieving the
     * enzymes, before the comparison.
     */
    @RequestMapping(value = "/compare")
    protected String getComparison(Model model, HttpSession session,
            @RequestParam(value = "acc") String[] accs)
            throws EnzymeRetrieverException {
        model.addAttribute("searchModel", newEmptySearchModel());
        // Filter the incoming accessions, keep only two non-empty:
        String[] theAccs = new String[2];
        int j = 0;
        for (String acc : accs) {
            if (acc.length() == 0) {
                continue;
            }
            theAccs[j++] = acc;
            if (j == 2) {
                break;
            }
        }
        ExecutorService pool = null;
        try {
            EnzymeModel models[] = new EnzymeModel[2];
            LOGGER.debug("Getting enzyme models...");
            pool = Executors.newFixedThreadPool(2);
            CompletionService<EnzymeModel> cs
                    = new ExecutorCompletionService<>(pool);
            for (String acc : theAccs) {
                cs.submit(new EnzymeModelCallable(acc));
            }
            for (int i = 0; i < 2; i++) {
                EnzymeModel em = cs.take().get();
                if (em.getUniprotaccessions().get(0).equals(theAccs[0])) {
                    models[0] = em;
                } else {
                    models[1] = em;
                }
            }
            LOGGER.debug("Comparison started...");
            EnzymeComparison comparison
                    = new EnzymeComparison(models[0], models[1]);
            LOGGER.debug("Comparison finished");
            model.addAttribute("comparison", comparison);
            //pdbImgUrl = http://www.ebi.ac.uk/pdbe/static/entry/{0}_deposited_chain_front_image-200x200.png
            model.addAttribute("pdbImgUrl", pdbImgUrl);
            model.addAttribute("pdbStructureCompareUrl", pdbStructureCompareUrl);
            model.addAttribute("uniprotAlignUrl", uniprotAlignUrl);
            model.addAttribute("reactomeConfig", reactomeConfig);
            model.addAttribute("intenzConfig", intenzConfig);

            return "comparison";
        } catch (InterruptedException | ExecutionException e) {
            String errorParam = theAccs[0] + "," + theAccs[1];
            LOGGER.error("Unable to compare enzymes: " + errorParam, e);
            model.addAttribute("errorCode", "comparison");
            model.addAttribute("errorParam", errorParam);
            return "error";
        } finally {
            if (pool != null) {
                pool.shutdownNow();
            }
        }
    }

    private SearchModel newEmptySearchModel() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setPrevioustext("");
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

    private class EnzymeModelCallable implements Callable<EnzymeModel> {

        private final String acc;

        public EnzymeModelCallable(String acc) {
            this.acc = acc;
        }

        @Override
        public EnzymeModel call() throws Exception {
            enzymeRetriever.setEnzymePortalService(enzymePortalService);
            enzymeRetriever.setLiteratureService(literatureService);
            enzymeRetriever.setIntenzAdapter(intenzAdapter);
            enzymeRetriever.setChebiAdapter(chebiAdapter);

            return enzymeRetriever.getWholeModel(acc);

        }

    }
}
