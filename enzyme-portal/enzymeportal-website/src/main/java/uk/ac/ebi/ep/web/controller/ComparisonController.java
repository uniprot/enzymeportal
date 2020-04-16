package uk.ac.ebi.ep.web.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.comparisonservice.domain.EnzymeComparison;
import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;
import uk.ac.ebi.ep.comparisonservice.model.Disease;
import uk.ac.ebi.ep.comparisonservice.model.Molecule;
import uk.ac.ebi.ep.comparisonservice.model.ReactionPathway;
import uk.ac.ebi.ep.comparisonservice.service.ComparisonService;
import uk.ac.ebi.ep.web.tools.Attribute;

/**
 * Controller for basket actions.
 *
 */
@Slf4j
@Controller
public class ComparisonController {

    private static final String COMPARISON = "comparison";

    private static final String PDB_STRUCTURE_COMPARE_URL = "https://www.ebi.ac.uk/msd-srv/ssm/cgi-bin/ssmserver?";
    private static final String PDB_IMG_URL = "https://www.ebi.ac.uk/pdbe/static/entry/{0}_deposited_chain_front_image-200x200.png";
    private static final String UNIPROT_ALIGN_URL = "https://www.uniprot.org/align/?redirect=yes&annotated=false&program=clustalo&query={0}+{1}";

    private final ComparisonService comparisonService;

    @Autowired
    public ComparisonController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;

    }

    @GetMapping(value = "/ajax/basket")
    @ResponseBody
    public String updateBasket(HttpServletResponse response, @RequestParam String id, @RequestParam Boolean checked, HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<String, ComparisonProteinModel> basket = (Map<String, ComparisonProteinModel>) session.getAttribute(Attribute.basket.name());
        if (basket == null) {
            basket = Collections.synchronizedMap(new LinkedHashMap<>());
            session.setAttribute(Attribute.basket.name(), basket);
        }
        for (String basketId : id.split(";")) {
            if (Boolean.TRUE.equals(checked)) {

                final ComparisonProteinModel summary = comparisonService.getComparisonProteinModel(basketId);
                if (summary != null) {
                    basket.put(basketId, summary);
                }

            } else {
                basket.remove(basketId);
            }
        }
        response.setContentType("text/plain");
        return String.valueOf(basket.size());
    }

    @GetMapping(value = "/basket")
    public String getBasket(Model model) {
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
     */
    @GetMapping(value = "/compare")
    public String getComparison(Model model, HttpSession session, @RequestParam(value = "acc") String[] accs) {

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
            ComparisonProteinModel [] models = new ComparisonProteinModel[2];
            log.debug("Getting enzyme models...");
            pool = Executors.newFixedThreadPool(2);
            CompletionService<ComparisonProteinModel> cs
                    = new ExecutorCompletionService<>(pool);
            for (String acc : theAccs) {
                cs.submit(new EnzymeModelCallable(acc));
            }
            for (int i = 0; i < 2; i++) {
                ComparisonProteinModel em = cs.take().get();
                if (em.getAccession().equals(theAccs[0])) {
                    models[0] = em;
                } else {
                    models[1] = em;
                }
            }
            log.debug("Comparison started...");
            EnzymeComparison comparison
                    = new EnzymeComparison(models[0], models[1]);
            log.debug("Comparison finished");
            model.addAttribute(COMPARISON, comparison);
            model.addAttribute("pdbImgUrl", PDB_IMG_URL);
            model.addAttribute("pdbStructureCompareUrl", PDB_STRUCTURE_COMPARE_URL);
            model.addAttribute("uniprotAlignUrl", UNIPROT_ALIGN_URL);

            return COMPARISON;
        } catch (InterruptedException | ExecutionException e) {
            String errorParam = theAccs[0] + "," + theAccs[1];
            log.error("Unable to compare enzymes: " + errorParam, e);
            model.addAttribute("errorCode", COMPARISON);
            model.addAttribute("errorParam", errorParam);
            Thread.currentThread().interrupt();
            return "error";
        } finally {
            if (pool != null) {
                pool.shutdownNow();
            }
        }
    }

    private class EnzymeModelCallable implements Callable<ComparisonProteinModel> {

        private final String acc;

        public EnzymeModelCallable(String acc) {
            this.acc = acc;
        }

        @Override
        public ComparisonProteinModel call() throws Exception {
            ComparisonProteinModel model = comparisonService.getComparisonProteinModel(acc);
            Molecule molecule = comparisonService.getCompareEnzymeMolecule(acc);
            ReactionPathway reactionPathway = comparisonService.getCompareEnzymeReactionPathay(acc);

            List<Disease> diseases = comparisonService.getCompareEnzymeDisease(acc);

            model.setDiseases(diseases);
            model.setMolecule(molecule);
            model.setReactionpathway(Arrays.asList(reactionPathway));

            return model;
        }

    }
}
