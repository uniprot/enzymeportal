package uk.ac.ebi.ep.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.ProteinGroupService;
import uk.ac.ebi.ep.ebeye.enzyme.model.Entry;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupResult;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.web.utils.EnzymePage;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymePageController extends AbstractController {

    private static final Logger logger = Logger.getLogger(EnzymePageController.class);

    @Autowired
    public EnzymePageController(ProteinGroupService proteinGroupService,EnzymePortalService enzymePortalService, LiteratureService litService) {
       this.proteinGroupService = proteinGroupService;
        this.enzymePortalService = enzymePortalService;
        this.literatureService = litService;
    }

    @RequestMapping(value = "/search/ec/{ec}", method = RequestMethod.GET)
    public String showEnzyme(@PathVariable("ec") String ec, Model model, RedirectAttributes attributes) {

        int resultLimit = 7;
        long startTime = System.nanoTime();
        boolean isEc = searchUtil.validateEc(ec);
        if (isEc) {

            EnzymePage enzymePage = computeEnzymePage(ec, resultLimit);

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
            logger.warn("Time taken to find Enzyme " + ec + " :  (" + elapsedtime + " sec)");

            model.addAttribute("enzymePage", enzymePage);

            model.addAttribute("ec", ec);
            return "enzymePage";
        }

        return "error";
    }

    public EnzymePage computeEnzymePage(String ecNumber, int limit) {

        CompletableFuture<Entry> enzyme = CompletableFuture.supplyAsync(() -> findEnzymeByEcNumber(ecNumber));

        CompletableFuture<ProteinGroupResult> proteins = CompletableFuture.supplyAsync(() -> findProteinsByEcNumber(ecNumber, limit));

        CompletableFuture<List<Result>> citations = CompletableFuture.supplyAsync(() -> findCitations(ecNumber, limit));

        return enzyme.thenCombine(proteins, (theEnzyme, protein) -> addProteins(protein, theEnzyme))
                .thenCombine(citations, (finalResult, citation) -> addCitations(citation, finalResult))
                .join();

    }

    private List<Result> findCitations(String term, int limit) {

        EuropePMC epmc = literatureService.getCitationsBySearchTerm(term, limit);

        return epmc.getResultList().getResult();
    }

    private Entry findEnzymeByEcNumber(String ecNumber) {

        //return enzymePortalService.findIntenzEnzymesByEc(ecNumber);
        return getEbiSearchResultByEC(ecNumber).getEntries().stream().findAny().orElseGet(()-> new Entry());

    }
    
        private EBISearchResult getEbiSearchResultByEC(String ec) {
        String facets = "";
        int startPage = 0; int pageSize =1;
        return enzymeCentricService.findEbiSearchResultsByEC(ec, startPage, pageSize, facets, 0);

    }

//    private List<AssociatedProtein> findProteinsByEcNumber(String ecNumber, int limit) {
//        String entryType = "0";
//        return enzymePortalService.findAssociatedProteinsByEcNumber(ecNumber, limit);
//        // return enzymePortalService.findAssociatedProteinsByEcNumber(ecNumber, entryType, limit);
//    }
    private ProteinGroupResult findProteinsByEcNumber(String ecNumber, int limit) {
        int start = 0;
        int pageSize = limit;
        return proteinGroupService.findProteinGroupResultByEC(ecNumber, start, pageSize);

    }

    private EnzymePage addProteins(ProteinGroupResult pgr, Entry e) {

        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEc())
                .altNames(e.getFields().getAltNames())
                .cofactors(e.getFields().getIntenzCofactors())
                .catalyticActivities(e.getFields().getDescription().stream().findAny().orElse(""))
                .proteins(pgr)
                .build();

    }

    private EnzymePage addCitations(List<Result> cit, EnzymePage e) {
        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEc())
                .catalyticActivities(e.getCatalyticActivities())
                .altNames(e.getAltNames())
                .cofactors(e.getCofactors())
                .proteins(e.getProteins())
                .citations(cit)
                .build();

    }

}
