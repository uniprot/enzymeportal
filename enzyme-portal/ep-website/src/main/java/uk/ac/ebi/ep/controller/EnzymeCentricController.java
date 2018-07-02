package uk.ac.ebi.ep.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static uk.ac.ebi.ep.controller.AbstractController.SEARCH_VIDEO;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeFields;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeView;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.web.utils.EnzymePage;
import uk.ac.ebi.ep.web.utils.KeywordType;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final Logger logger = Logger.getLogger(EnzymeCentricController.class);

    private static final String SEARCH = "/enzymes";
    private static final String FILTER = "/search/filter";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";
    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 1_0;
    //private static final int ASSOCIATED_PROTEIN_LIMIT = 8_000;
    private static final int PAGE_SIZE = 10;

    @RequestMapping(value = SEARCH, method = RequestMethod.GET)
    public String getSearchResults(@RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            SearchModel searchModel, BindingResult result,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return postSearchResult(searchKey, filters, servicePage, keywordType, searchId, searchModel, model, request);
    }

    @RequestMapping(value = SEARCH, method = RequestMethod.POST)
    public String postSearchResult(@RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";
        int startPage = 0;
        if (servicePage != null) {
            if (servicePage < 0) {
                servicePage = 1;
            }
            startPage = servicePage - 1;//EBI search paging index starts at 0
        }

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
        int associatedProteinLimit = 7;// ASSOCIATED_PROTEIN_LIMIT;
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (searchModel.getSearchparams().getText() != null) {
            searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();

        }

        String searchTerm = Jsoup.clean(searchKey, Whitelist.basic());
        if (filters.contains("")) {
            filters.remove("");

        }
        KeywordType type = KeywordType.valueOf(keywordType);
        switch (type) {
            case KEYWORD:

                boolean isEc = searchUtil.validateEc(searchTerm);
                if (isEc) {
                    view = findEnzymesByEC(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                } else {
                    // searchTerm = UrlUtil.encode(searchTerm);

                    if (searchTerm.contains("/")) {
                        searchTerm = searchTerm.replaceAll("/", " ");

                    }
                    //searchTerm = UrlUtil.encode(searchTerm);
                    view = findEnzymesBySearchTerm(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                }
                break;
            case DISEASE:
                view = findEnzymesByOmimId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case TAXONOMY:
                view = findEnzymesByTaxId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case PATHWAYS:
                view = findEnzymesByPathwayId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case EC:
                view = findEnzymesByEC(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            default:
                return view;

        }
        return view;
    }

    private EnzymeSearchResult getEbiSearchResult(String query, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.getQuerySearchResult(query, startPage, pageSize, facets, facetCount);
    }

    private EnzymeSearchResult getEbiSearchResultByOmimId(String omimId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByOmimId(omimId, startPage, pageSize, facets, facetCount);

    }

    private EnzymeSearchResult getEbiSearchResultByTaxId(String taxId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));

        return enzymeCentricService.findEbiSearchResultsByTaxId(taxId, startPage, pageSize, facets, facetCount);

    }

    private EnzymeSearchResult getEbiSearchResultByEC(String ec, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByEC(ec, startPage, pageSize, facets, facetCount);

    }

    private EnzymeSearchResult getEbiSearchResultByPathwayId(String pathwayId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByPathwayId(pathwayId, startPage, pageSize, facets, facetCount);

    }

    private String findEnzymesBySearchTerm(String searchTerm, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {
        EnzymeSearchResult ebiSearchResult = getEbiSearchResult(searchTerm, startPage * pageSize, pageSize, facetCount, filters);
        int LOWEST_BEST_MATCHED_RESULT_SIZE = 2;
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<EnzymeEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<EnzymeEntry> entries = page.getContent();

            //final Set<EnzymeEntry> enzymeView = ebiSearchResult.getEntries()
            //  .stream().collect(Collectors.toSet());
            // List<EnzymeEntry> enzymeView = new LinkedList<>();
            Set<EnzymeEntry> enzymeView = new LinkedHashSet<>();

            int start = 0;
            int limit = associatedProteinLimit;

            entries.stream().forEach(entry -> {
                ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultBySearchTermAndEC(entry.getEc(), searchTerm, start, limit);

                if (result.getHitCount() == 0) {
                    result = proteinGroupService.findProteinGroupResultByEC(entry.getEc(), start, LOWEST_BEST_MATCHED_RESULT_SIZE);
                    //limit asscoated protein result
                    if (result.getHitCount() > LOWEST_BEST_MATCHED_RESULT_SIZE) {
                        result.setHitCount(LOWEST_BEST_MATCHED_RESULT_SIZE);
                    }
                }

                addProteinEntryToEnzymeView(result, entry, enzymeView);

            });

//            if (enzymeView.isEmpty() && !ebiSearchResult.getEntries().isEmpty()) {
//                logger.error(ebiSearchResult.getEntries().size()
//                        + " results are found in Enzyme-centric index for query " + searchTerm + " But none in Protein-centric index");
//                ebiSearchResult = new EnzymeSearchResult();
//                ebiSearchResult.setFacets(new ArrayList<>());
//                ebiSearchResult.setHitCount(0);
//                ebiSearchResult.setEntries(new ArrayList<>());
//                page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, 0);
//
//            }
            return constructModel(ebiSearchResult, enzymeView, page, filters, searchTerm, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByOmimId(String omimId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EnzymeSearchResult ebiSearchResult = getEbiSearchResultByOmimId(omimId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<EnzymeEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<EnzymeEntry> entries = page.getContent();

            // List<EnzymeEntry> enzymeView = new LinkedList<>();
            Set<EnzymeEntry> enzymeView = new LinkedHashSet<>();

            entries.stream()
                    .forEach(entry -> {
                        ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByOmimIdAndEc(omimId, entry.getEc(), associatedProteinLimit);
//                        if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
//                            result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
//                        }

                        addProteinEntryToEnzymeView(result, entry, enzymeView);
//                        List<Protein> proteins = ebeyeRestService.findUniqueProteinsByOmimIdAndEc(omimId, entry.getEc(), associatedProteinLimit)
//                        .stream()
//                        .sorted()
//                        .collect(Collectors.toList());
//                        
//                        
//                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);

                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, omimId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByTaxId(String taxId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EnzymeSearchResult ebiSearchResult = getEbiSearchResultByTaxId(taxId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<EnzymeEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<EnzymeEntry> entries = page.getContent();
            // List<EnzymeEntry> enzymeView = new LinkedList<>();
            Set<EnzymeEntry> enzymeView = new LinkedHashSet<>();

            entries.stream()
                    .forEach(entry -> {
                        ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByTaxIdAndEc(taxId, entry.getEc(), associatedProteinLimit);
//                        if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
//                            result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
//                        }

                        addProteinEntryToEnzymeView(result, entry, enzymeView);

//                        List<Protein> proteins = ebeyeRestService.findUniqueProteinsByTaxIdAndEc(taxId, entry.getEc(), associatedProteinLimit)
//                        .stream()
//                        .sorted()
//                        .collect(Collectors.toList());
//
//                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);
                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, taxId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private static final int CITATION_LIMIT = 11;

    private EnzymeEntry findEnzymeByEcNumber(String ecNumber) {

        return getEbiSearchResultByEC(ecNumber)
                .getEntries()
                .stream()
                .findAny()
                .orElseGet(() -> new EnzymeEntry(ecNumber, new EnzymeFields()));

    }

    private EnzymeSearchResult getEbiSearchResultByEC(String ec) {
        return enzymeCentricService.findEnzymeByEC(ec);

    }

    private ProteinGroupSearchResult findProteinsByEcNumber(String ecNumber, int limit) {
        int start = 0;
        int pageSize = limit;
        ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultByEC(ecNumber, start, pageSize);
//        if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
//            result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
//        }
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

    private List<Result> findCitations(String enzymeName, int limit) {

        EuropePMC epmc = literatureService.getCitationsBySearchTerm(enzymeName, limit);

        return epmc
                .getResultList()
                .getResult();
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

    public EnzymePage computeEnzymePage(String ecNumber, String enzymeName, int limit) {

        CompletableFuture<EnzymeEntry> enzyme = CompletableFuture.supplyAsync(() -> findEnzymeByEcNumber(ecNumber));

        CompletableFuture<ProteinGroupSearchResult> proteins = CompletableFuture.supplyAsync(() -> findProteinsByEcNumber(ecNumber, limit));

        CompletableFuture<List<Result>> citations = CompletableFuture.supplyAsync(() -> findCitations(enzymeName, CITATION_LIMIT));

        return enzyme.thenCombine(proteins, (theEnzyme, protein) -> addProteins(protein, theEnzyme))
                .thenCombine(citations, (finalResult, citation) -> addCitations(citation, finalResult))
                .join();

    }

    private String findEnzymesByEC(String ec, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        String validEc = searchUtil.transformIncompleteEc(ec);
        EnzymeSearchResult ebiSearchResult = getEbiSearchResultByEC(validEc, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();

            if (hitCount == 1) {
                int resultLimit = 7;
                String enzymeName = Optional.ofNullable( ebiSearchResult
                        .getEntries()
                        .stream()
                        .findAny()
                        .orElseGet(() -> new EnzymeEntry(validEc, new EnzymeFields()))
                        .getEnzymeName()).orElse(validEc);
                
                EnzymePage enzymePage = computeEnzymePage(validEc, enzymeName, resultLimit);
                long startTime = System.nanoTime();
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
                logger.warn("Time taken to find Enzyme " + ec + " :  (" + elapsedtime + " sec)");

                model.addAttribute("enzymePage", enzymePage);

                SearchModel searchModelForm = new SearchModel();
                SearchParams searchParams = new SearchParams();
                searchParams.setStart(0);
                searchParams.setType(SearchParams.SearchType.EC);
                searchParams.setPrevioustext("");
                searchModelForm.setSearchparams(searchParams);

                model.addAttribute("ec", ec);
                model.addAttribute("searchKey", ec);
                //model.addAttribute("searchTerm", ec);
                model.addAttribute("keywordType", KeywordType.EC.name());
                model.addAttribute("searchId", ec);
                model.addAttribute("searchModel", searchModelForm);
                model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
                return "enzymePage";

            }
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<EnzymeEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<EnzymeEntry> entries = page.getContent();
            Set<EnzymeEntry> enzymeView = new LinkedHashSet<>();

            entries.stream()
                    .forEach(entry -> {
                        ProteinGroupSearchResult result = proteinGroupService.findProteinGroupResultByEC(entry.getEc(), 0, associatedProteinLimit);
//                        if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
//                            result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
//                        }

                        addProteinEntryToEnzymeView(result, entry, enzymeView);

//                        List<Protein> proteins = ebeyeRestService.queryForUniqueProteins(entry.getEc(), associatedProteinLimit)
//                        .stream()
//                        .sorted()
//                        .collect(Collectors.toList());
//
//                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);
                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, ec, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByPathwayId(String pathwayId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EnzymeSearchResult ebiSearchResult = getEbiSearchResultByPathwayId(pathwayId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<EnzymeEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<EnzymeEntry> entries = page.getContent();
            //List<EnzymeEntry> enzymeView = new LinkedList<>();
            Set<EnzymeEntry> enzymeView = new LinkedHashSet<>();

            entries.stream().forEach(entry -> {

                ProteinGroupSearchResult result = proteinGroupService.findUniqueProteinsByPathwayIdAndEc(pathwayId, entry.getEc(), associatedProteinLimit);
//                if (result.getHitCount() > MAX_PROTEIN_DISPLAY_LIMIT) {
//                    result.setHitCount(MAX_PROTEIN_DISPLAY_LIMIT);
//                }

                addProteinEntryToEnzymeView(result, entry, enzymeView);

//                List<Protein> proteins = ebeyeRestService.findUniqueProteinsByPathwayIdAndEc(pathwayId, entry.getEc(), associatedProteinLimit)
//                        .stream()
//                        .sorted()
//                        .collect(Collectors.toList());
//
//                addProteinEntryToEnzymeView(proteins, entry, enzymeView);
            });

            return constructModel(ebiSearchResult, enzymeView, page, filters, pathwayId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String constructModel(EnzymeSearchResult ebiSearchResult, Set<EnzymeEntry> enzymeView, Page page, List<String> filters, String searchId, String searchKey, String keywordType, Model model, SearchModel searchModel) {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("enzymeView", enzymeView);
        model.addAttribute("filtersApplied", filters);

        model.addAttribute("searchKey", searchKey);
        model.addAttribute("keywordType", keywordType);
        model.addAttribute("searchId", searchId);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
        return ENZYME_CENTRIC_PAGE;

    }

    private void addProteinEntryToEnzymeView(ProteinGroupSearchResult result, EnzymeEntry entry, Set<EnzymeEntry> enzymeView) {
        int proteinHits = result.getHitCount();
        if (proteinHits > 0) {
            entry.setProteinGroupEntry(result.getEntries());
            entry.setNumProteins(result.getHitCount());
            entry.setNumEnzymeHits(result.getHitCount());

            enzymeView.add(entry);
        }
//        else{
//             entry.setProteinGroupEntry(new ArrayList<>());
//            entry.setNumProteins(0);
//            entry.setNumEnzymeHits(0);
//
//            enzymeView.add(entry);  
//        }
    }

//    @Deprecated
//    private void addProteinEntryToEnzymeView(List<Protein> proteins, Entry entry, List<Entry> enzymeView) {
//
//        int proteinHits = proteins.size();
//
//        if (proteinHits > 0) {
//            entry.setProteins(proteins);
//            entry.setNumProteins(proteinHits);
//            //entry.setNumEnzymeHits(hitCount);
//            entry.setNumEnzymeHits(proteinHits);
//            enzymeView.add(entry);
//        }
//
//    }
    @RequestMapping(value = "/enzymes/download/txt", method = RequestMethod.GET)
    public void downloadEnzymes(HttpServletResponse response, Model model, HttpServletRequest request, RedirectAttributes attributes)
            throws IOException {

        //use this
        //https://stackoverflow.com/questions/5673260/downloading-a-file-from-spring-controllers
        //https://aboullaite.me/spring-boot-excel-csv-and-pdf-view-example/
        //https://stackoverflow.com/questions/5673260/downloading-a-file-from-spring-controllers 
        //https://stackoverflow.com/questions/16652760/return-generated-pdf-using-spring-mvc
        //https://stackoverflow.com/questions/858980/file-to-byte-in-java
        //https://stackoverflow.com/questions/45985929/download-large-file-through-spring-mvc-controller
        //String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);
        stopWatch.stop();
        logger.info("Status Check .txt took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

//        if (entry == null) {
//            status = "DOWN";
//        }
        model.addAttribute("status", entry);
        request.setAttribute("status", entry);

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=status.txt");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(entry.toString());
            out.flush();
        }
    }

    @RequestMapping(value = "/enzymes/download/csv")
    public void downloadCSV(HttpServletResponse response) throws IOException {

        //better approach ?
        //https://github.com/greyseal/spring-boot-csv-download/blob/master/src/main/java/com/sample/app/controller/SampleController.java
        response.setContentType("text/csv");
        String enzymes = "enzymes.csv" + new Date();
        response.setHeader("Content-disposition", "attachment;filename=" + enzymes);

        int startPage = 0;

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
        String searchTerm = "sildenafil";

        List<String> filters = new ArrayList<>();

        EnzymeSearchResult ebiSearchResult = getEbiSearchResult(searchTerm, startPage * pageSize, pageSize, facetCount, filters);

        List<EnzymeEntry> entries = ebiSearchResult.getEntries();

        ArrayList<String> rows = new ArrayList<>();
        rows.add("EC,Name, \t Family, \t Catalytic Activity, \t Cofactor");
        rows.add("\n");

        for (EnzymeEntry entry : entries) {
            rows.add(entry.getEc() + "," + entry.getEnzymeName() + "," + entry.getEnzymeFamily() + "," + entry.getCatalyticActivities() + "," + entry.getIntenzCofactors());
            rows.add("\n");
        }

        Iterator<String> iter = rows.iterator();
        while (iter.hasNext()) {
            String outputString = (String) iter.next();
            response.getOutputStream().print(outputString);
        }

        response.getOutputStream().flush();

    }

    @RequestMapping(value = "/enzymes/download/open", produces = "text/csv")
    public void downloadOpenCSV(HttpServletResponse response) throws IOException {

        int startPage = 0;

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
        String searchTerm = "sildenafil";

        List<String> filters = new ArrayList<>();

        EnzymeSearchResult ebiSearchResult = getEbiSearchResult(searchTerm, startPage * pageSize, pageSize, facetCount, filters);

        List<EnzymeEntry> entries = ebiSearchResult.getEntries();

        writeEntriesCsv(response.getWriter(), entries);
    }

    //@RequestMapping(value = "/enzymes/download/entries={entries}&compress={compress}&format={format}", produces = "text/csv")
    @RequestMapping(value = "/enzymes/download/DELETE", produces = "text/csv")
    public void downloadEnzymes(@RequestParam("entries") List<EnzymeEntry> entries,
            @RequestParam("compress") String compress,
            @RequestParam("format") String format,
            HttpServletResponse response) throws IOException {

        int startPage = 0;

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;

//          String searchTerm = "sildenafil";
//        for(String c : ec){
//            searchTerm ="ec:"+c;
//        }
//      
//
//        List<String> filters = new ArrayList<>();
//           System.out.println("SEARCH TERM "+ searchTerm);
//        EnzymeSearchResult ebiSearchResult = getEbiSearchResult(searchTerm, startPage * pageSize, pageSize, facetCount, filters);
//
//        List<EnzymeEntry> entries = ebiSearchResult.getEntries();
        //List<EnzymeEntry> entries = new ArrayList<>();
        //entries.add(entries1);
        System.out.println("THE ENTRIES " + entries);
        System.out.println("COMPRESS " + compress);
        System.out.println("FORMAT " + format);

        writeEntriesCsv(response.getWriter(), entries);
    }

    public void writeEntriesCsv(PrintWriter writer, List<EnzymeEntry> entries) {

        try {

            ColumnPositionMappingStrategy mapStrategy
                    = new ColumnPositionMappingStrategy();

            mapStrategy.setType(EnzymeView.class);
            mapStrategy.generateHeader();

            String[] columns = new String[]{"EC", "Name", "Family", "Catalytic Activity", "Cofactor"};

            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(entries);

        } catch (CsvException ex) {

            logger.error("Error mapping Bean to CSV", ex);
        }
    }

    private static void downloadFile(String webPage, Path path) throws MalformedURLException, IOException {

        URI u = URI.create(webPage);
        try (InputStream in = u.toURL().openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }

    }

}
