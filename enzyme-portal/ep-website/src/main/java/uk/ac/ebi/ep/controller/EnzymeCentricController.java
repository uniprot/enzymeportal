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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static uk.ac.ebi.ep.controller.AbstractController.BROWSE_VIDEO;
import uk.ac.ebi.ep.dummy.DummyProtein;
import uk.ac.ebi.ep.dummy.EnzymePortalEnzyme;
import uk.ac.ebi.ep.dummy.Species;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final String SHOW_ENZYMES = "/enzymes";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";

    @RequestMapping(value = SHOW_ENZYMES, method = RequestMethod.GET)
    public String showEnzymes(Model model) {

        List<EnzymePortalEnzyme> enzymes = createEnzymes(100);

        model.addAttribute("enzymes", enzymes);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);

        return ENZYME_CENTRIC_PAGE;
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

//    public static void main(String[] args) {
//        List<EnzymePortalEnzyme> enzymes = createEnzymes(100);
//
//        enzymes.stream().forEach(e -> System.out.println("Data : " + e.getDummyProteinSet()));
////        for(EnzymePortalEnzyme e : enzymes){
////            for(DummyProtein p : e.getDummyProteinSet()){
////                System.out.println("SP "+ p.getSpeciesSet());
////            }
////        }
//    }

    private static int randomEcClass(long id) {

        Random random = new Random(id);
//ec range 1 -> 6
        OptionalInt randomNumber = random.ints(1, 7).findFirst();
        return randomNumber.getAsInt();

    }
}
