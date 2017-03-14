package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
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
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
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
    
    @RequestMapping(value = "/page/{ec}", method = RequestMethod.GET)
    public String showEnzyme(@PathVariable("ec") String ec, Model model, RedirectAttributes attributes) {
        
        int accessionLimit = 7;
        long startTime = System.nanoTime();
        
        EnzymePage enzymePage = computeEnzymePage(ec, accessionLimit);
        //IntenzEnzymes intenz = findEnzymeByEcNumber(ec);

        //EnzymePage enzymePage = addEnzyme(intenz);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        logger.warn("Time taken to find Enzyme " + ec + " :  (" + elapsedtime + " sec)");
        
        model.addAttribute("enzymePage", enzymePage);
        
        model.addAttribute("ec", ec);
        
        return "enzymePage";
    }
    
    @Autowired
    public EnzymePageController(EnzymePortalService enzymePortalService, LiteratureService litService) {
        this.enzymePortalService = enzymePortalService;
        this.literatureService = litService;
    }
    
    private List<Result> findCitations(String term, int limit) {
        
        EuropePMC epmc = literatureService.getCitationsBySearchTerm(term, limit);
        
        return epmc.getResultList().getResult();
    }
    
    private IntenzEnzymes findEnzymeByEcNumber(String ecNumber) {
        
        return enzymePortalService.findIntenzEnzymesByEc(ecNumber);
        
    }
    
    private List<String> findAccessionsByEcNumber(String ecNumber, int limit) {
        return enzymePortalService.findAccessionsByEc(ecNumber, limit);
    }
    
    private EnzymePage addAccessions(List<String> accs, IntenzEnzymes e) {
        
        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEcNumber())
                .catalyticActivities(e.getCatalyticActivity())
                .accessions(accs)
                .build();
        
    }
    
    private EnzymePage addAccessions(List<String> accs, EnzymePage e) {
        
        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEc())
                .catalyticActivities(e.getCatalyticActivities())
                .accessions(accs)
                .build();
        
    }
    
    private EnzymePage addCitations(List<Result> cit, EnzymePage e) {
        return EnzymePage
                .enzymePageBuilder()
                .enzymeName(e.getEnzymeName())
                .ec(e.getEc())
                .catalyticActivities(e.getCatalyticActivities())
                .accessions(e.getAccessions())
                .citations(cit)
                .build();
        
    }
    
    private EnzymePage addEnzyme(IntenzEnzymes e) {
        final List<String> cofactors = new ArrayList<>();
        final List<String> altNames = new ArrayList<>();
        if (e != null) {
            e.getIntenzCofactorsSet()
                    .stream()
                    .forEach(cofactor -> cofactors.add(cofactor.getCofactor()));
            e.getIntenzAltNamesSet()
                    .stream()
                    .forEach(altName -> altNames.add(altName.getAltName()));
            return EnzymePage
                    .enzymePageBuilder()
                    .enzymeName(e.getEnzymeName())
                    .ec(e.getEcNumber())
                    .catalyticActivities(e.getCatalyticActivity())
                    .cofactor(cofactors)
                    .altName(altNames)
                    .build();
            
        }
        return EnzymePage.enzymePageBuilder().build();
        
    }

//
//    private EnzymePage createDisplay(String accessions, Result citations) {
//        EnzymePage page = EnzymePage.enzymePageBuilder().build();
//               
//        return page;
//
//    }
//
//
//    private EnzymePage compute(String ecNumber, int limit) {
//        //IntenzEnzymes   enzyme = findEnzymeByEcNumber(ecNumber);
//        Single<IntenzEnzymes> enzyme = Single.just(findEnzymeByEcNumber(ecNumber));
//
//        Observable<String> accessions = Observable.from(findAccessionsByEcNumber(ecNumber, limit));
//
//        Observable<Result> citations = Observable.from(findCitations(ecNumber, limit));
//
//        Observable<EnzymePage> display = Observable.zip(accessions, citations, this::createDisplay);
//
//        return display.toBlocking().single();
//    }
    private EnzymePage computeEnzymePage(String ecNumber, int limit) {
        
        CompletableFuture<IntenzEnzymes> enzyme = CompletableFuture.supplyAsync(() -> findEnzymeByEcNumber(ecNumber));
        
        CompletableFuture<List<String>> accessions = CompletableFuture.supplyAsync(() -> findAccessionsByEcNumber(ecNumber, limit));
        
        CompletableFuture<List<Result>> citations = CompletableFuture.supplyAsync(() -> findCitations(ecNumber, limit));

//
//        return enzyme.thenCombine(accessions, (theEnzyme, acc) -> addAccessions(acc, theEnzyme))
//                //.thenApply(theEnzyme -> addEnzyme(theEnzyme))
//                //.thenCombine(accessions, (firstResult, acc) -> addAccessions(acc, firstResult))
//                
//                //.thenCombine(citations, (finalResult, citation) -> addCitations(citation, finalResult))
//                .join();
//        // }
//        
        return addEnzyme(enzyme.join());
        
    }

//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment().setActiveProfiles("uzpdev");
////        context.register(DataConfig.class, GlobalConfig.class);
////        context.scan("uk.ac.ebi.ep.data.dataconfig");
//
//        context.register(DataConfig.class, EbeyeConfig.class, PmcConfig.class);
//        context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.ebeye.config");
//        context.refresh();
//
//        EnzymePortalService enzymePortalService = context.getBean(EnzymePortalService.class);
//
//        LiteratureService literatureService = context.getBean(LiteratureService.class);
//
//        EnzymePageController epc = new EnzymePageController(enzymePortalService, literatureService);
//
//        String ec = "6.1.1.1";
//        EnzymePage page = epc.computeEnzymePage(ec, 11);
//
//        System.out.println(
//                "ENZYME " + page.getEnzymeName());
//        System.out.println(
//                "PAGE size" + page.getAccessions().size());
//        System.out.println(
//                "PAGE size" + page.getCitations().size());
//        //System.out.println("Data " + page);
//
//        for (Result rs : page.getCitations()) {
//            System.out.println("RESULT " + rs.getTitle());
//        }
//
//    }
}
