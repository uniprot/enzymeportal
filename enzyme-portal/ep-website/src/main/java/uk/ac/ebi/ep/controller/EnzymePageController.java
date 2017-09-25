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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static uk.ac.ebi.ep.controller.AbstractController.SEARCH_VIDEO;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.ProteinGroupService;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.web.utils.EnzymePage;
import uk.ac.ebi.ep.web.utils.KeywordType;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymePageController extends AbstractController {

    private static final Logger logger = Logger.getLogger(EnzymePageController.class);
    private static final int CITATION_LIMIT = 11;

    @Autowired
    public EnzymePageController(ProteinGroupService proteinGroupService, EnzymePortalService enzymePortalService, LiteratureService litService) {
        this.proteinGroupService = proteinGroupService;
        this.enzymePortalService = enzymePortalService;
        this.literatureService = litService;
    }

    @RequestMapping(value = "/search/ec/{ec}", method = RequestMethod.GET)
    public String showEnzyme(@PathVariable("ec") String ec, @RequestParam(value = "enzymeName", required = false) String enzymeName, Model model, SearchModel searchModel, RedirectAttributes attributes) {

        int resultLimit = 7;// ASSOCIATED_PROTEIN_LIMIT;

        boolean isEc = true;// searchUtil.validateEc(ec);//TODO
        if (isEc) {
            long startTime = System.nanoTime();
            if(enzymeName == null){
                enzymeName = ec;
            }
            EnzymePage enzymePage = computeEnzymePage(ec, enzymeName, resultLimit);

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
            logger.warn("Time taken to find Enzyme " + ec + " :  (" + elapsedtime + " sec)");

            model.addAttribute("enzymePage", enzymePage);

            SearchModel searchModelForm = new SearchModel();
            SearchParams searchParams = new SearchParams();
            searchParams.setStart(0);
            searchParams.setType(SearchParams.SearchType.EC2PROTEIN);
            searchParams.setPrevioustext("");
            searchModelForm.setSearchparams(searchParams);

            model.addAttribute("ec", ec);
            model.addAttribute("searchKey", ec);
            model.addAttribute("searchTerm", ec);
            model.addAttribute("keywordType", KeywordType.EC2PROTEIN.name());
            model.addAttribute("searchId", ec);
            model.addAttribute("searchModel", searchModelForm);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            return "enzymePage";
        }

        return "error";
    }

    public EnzymePage computeEnzymePage(String ecNumber, String enzymeName, int limit) {

        CompletableFuture<EnzymeEntry> enzyme = CompletableFuture.supplyAsync(() -> findEnzymeByEcNumber(ecNumber));

        CompletableFuture<ProteinGroupSearchResult> proteins = CompletableFuture.supplyAsync(() -> findProteinsByEcNumber(ecNumber, limit));

        CompletableFuture<List<Result>> citations = CompletableFuture.supplyAsync(() -> findCitations(enzymeName, CITATION_LIMIT));

        return enzyme.thenCombine(proteins, (theEnzyme, protein) -> addProteins(protein, theEnzyme))
                .thenCombine(citations, (finalResult, citation) -> addCitations(citation, finalResult))
                .join();

    }

    private List<Result> findCitations(String enzymeName, int limit) {

        EuropePMC epmc = literatureService.getCitationsBySearchTerm(enzymeName, limit);

        return epmc
                .getResultList()
                .getResult();
    }

    private EnzymeEntry findEnzymeByEcNumber(String ecNumber) {

        return getEbiSearchResultByEC(ecNumber)
                .getEntries()
                .stream()
                .findAny()
                .orElseGet(() -> new EnzymeEntry());

    }

    private EnzymeSearchResult getEbiSearchResultByEC(String ec) {
        return enzymeCentricService.findEnzymeByEC(ec);

    }

    private ProteinGroupSearchResult findProteinsByEcNumber(String ecNumber, int limit) {
        int start = 0;
        int pageSize = limit;
        ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultByEC(ecNumber, start, pageSize);
        if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
            result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
        }
        return result;
    }

    private EnzymePage addProteins(ProteinGroupSearchResult pgr, EnzymeEntry e) {

        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEc())
                .altNames(e.getFields().getAltNames())
                .cofactors(e.getFields().getIntenzCofactors())
                .catalyticActivities(e.getFields().getDescription().stream().findAny().orElse(""))
                .proteins(pgr)
                .numProteins(pgr.getHitCount())
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
                .numProteins(e.getNumProteins())
                .citations(cit)
                .numCitations(cit.size())
                .build();

    }

}
