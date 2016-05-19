/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static uk.ac.ebi.ep.controller.AbstractController.BROWSE_VIDEO;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.dummy.DummyProtein;
import uk.ac.ebi.ep.dummy.EnzymePortalEnzyme;
import uk.ac.ebi.ep.dummy.Species;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.EnzymeView;
import uk.ac.ebi.ep.ebeye.model.Facet;
import uk.ac.ebi.ep.ebeye.model.ModelService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final String SHOW_ENZYMES = "/enzymes";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";
    private static final String SHOW_ENZYMES_V = "/eview";
    private static final String ENZYME_CENTRIC_PAGE_V = "eview";

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchResults(SearchModel searchModel, BindingResult result,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return postSearchResult(searchModel, model, request);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";

        String searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();
        EBISearchResult ebiSearchResult = getEbiSearchResult(searchKey);
        if (ebiSearchResult != null) {

            model.addAttribute("searchKey", searchKey);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            model.addAttribute("ebiResult", ebiSearchResult);
            model.addAttribute("enzymeView", ebiSearchResult.getEntries());
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            view = ENZYME_CENTRIC_PAGE_V;
        }

        return view;
    }

    @RequestMapping(value = SHOW_ENZYMES, method = RequestMethod.GET)
    public String showEnzymes(Model model) {

        List<EnzymePortalEnzyme> enzymes = createEnzymes(100);

        model.addAttribute("enzymes", enzymes);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
           String query = "kinase";
        ModelService service = new ModelService();

        EBISearchResult eBISearchResult = service.getModelSearchResult(query);

        model.addAttribute("ebiResult", eBISearchResult);
        model.addAttribute("enzymeView", eBISearchResult.getEntries());
        model.addAttribute("enzymeFacet", eBISearchResult.getFacets());

        return ENZYME_CENTRIC_PAGE;
    }

    private EBISearchResult getEbiSearchResult(String query) {
        ModelService service = new ModelService();

        return service.getModelSearchResult(query);
    }

    @RequestMapping(value = SHOW_ENZYMES_V, method = RequestMethod.GET)
    public String showEnzymesView(Model model) {
        String query = "kinase";
        ModelService service = new ModelService();

        EBISearchResult eBISearchResult = service.getModelSearchResult(query);

        model.addAttribute("ebiResult", eBISearchResult);
        model.addAttribute("enzymeView", eBISearchResult.getEntries());
        model.addAttribute("enzymeFacet", eBISearchResult.getFacets());

        // List<EnzymePortalEnzyme> enzymes = createEnzymes(100);
        // model.addAttribute("enzymes", enzymes);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);

        return ENZYME_CENTRIC_PAGE_V;
    }

    private static List<EnzymePortalEnzyme> createEnzymes(int num) {
        return IntStream.range(1, num)
                .mapToObj(index -> createDummyEnzymes(index))
                .collect(Collectors.toList());
    }

    private static EnzymePortalEnzyme createDummyEnzymes(long id) {
        int ecClass = randomEcClass(id);
        EnzymePortalEnzyme enzyme = new EnzymePortalEnzyme();
        enzyme.setInternalId(id);
        enzyme.setEcNumber(ecClass + ".1.1." + id);
        enzyme.setEnzymeName("cGMP-specific 3',5'-cyclic phosphodiesterase-" + id);

        if (ecClass == 1) {
            enzyme.getCatalyticActivity().add("Guanosine 3',5'-cyclic phosphate + H(2)O = guanosine 5'-phosphate-" + id);
            enzyme.getCatalyticActivity().add("mAdenosine 3',5'-cyclic phosphate + H(2)O = adenosine 5'-phosphate-" + id);
        } else {
            enzyme.getCatalyticActivity().add("Adenosine 3',5'-cyclic phosphate + H(2)O = adenosine 5'-phosphate-" + id);
        }
        //enzyme.setCatalyticActivity("catalytic activity-" + id);
        //build species
        //Set<Species> dummySpecies = dummySpecies(id);
        //enzyme.setSpecies(dummySpecies);

        //create and associate protein
        Set<DummyProtein> proteins = IntStream.rangeClosed(1, Long.valueOf(id).intValue())
                .mapToObj(index -> dummyProtein(index))
                .collect(Collectors.toSet());
        enzyme.setDummyProteinSet(proteins);

        return enzyme;
    }

    private static DummyProtein dummyProtein(int index) {
        DummyProtein dp = new DummyProtein("ACCESSION-12" + index);
        dp.setCommonName("common-name-" + index);
        dp.setProteinName("protein-name-" + index);
        Set<Species> dummySpecies = dummySpecies(index);
        dp.setSpeciesSet(dummySpecies);
        return dp;
    }

    private static Set<Species> dummySpecies(long id) {
        return IntStream.rangeClosed(1, Long.valueOf(id).intValue())
                .mapToObj(index -> createDummySpecies(new AtomicInteger(index).getAndIncrement()))
                .collect(Collectors.toSet());

    }

    private static Species createDummySpecies(int index) {
        Species s = new Species();
        if (index % 2 == 0) {
            if (index < 20) {

                s.setCommonName("Human");
                s.setScientificName("Homo Sapien");
                s.setTaxId(index);
            } else {

                s.setCommonName("Rat");
                s.setScientificName("Ratus Novegius");
                s.setTaxId(index);
            }
        } else {
            if (index < 20) {
                s.setCommonName("Bovine");
                s.setScientificName("Bos taurus");
            } else {
                s.setCommonName("Mouse");
                s.setScientificName("Mus musculus");
            }
        }
        return s;
    }

    public static void main(String[] args) {
        String query = "kinase";
        ModelService service = new ModelService();

        EBISearchResult result = service.getModelSearchResult(query);
        for (EnzymeView entry : result.getEntries()) {
            //System.out.println(""+ entry.getFields().getProteinName().size());
            //entry.getFields().getProteinName().stream().forEach(name -> System.out.println("protein "+ name));
            //System.out.println("ENTRRY " + entry.getEc() + " : " + entry.getEnzymeName() + " "+ entry.getEnzymeFamily() + " "+ entry.getNumEnzymeHits() + " "+ entry.getSpecies() );
            System.out.println(" protein " + entry.getProteins());
        }
        for (Facet facet : result.getFacets()) {
            System.out.println(facet.getTotal() + "FACETS " + facet.getId() + " " + facet.getLabel() + " value : " + facet.getFacetValues());
        }

//        List<EnzymePortalEnzyme> enzymes = createEnzymes(100);
//
//        enzymes.stream().forEach(e -> System.out.println("Data : " + e.getDummyProteinSet()));
////        for(EnzymePortalEnzyme e : enzymes){
////            for(DummyProtein p : e.getDummyProteinSet()){
////                System.out.println("SP "+ p.getSpeciesSet());
////            }
////        }
    }

    private static int randomEcClass(long id) {

        Random random = new Random(id);
//ec range 1 -> 6
        OptionalInt randomNumber = random.ints(1, 7).findFirst();
        return randomNumber.getAsInt();

    }
}
