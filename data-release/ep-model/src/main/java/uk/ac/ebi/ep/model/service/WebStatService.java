package uk.ac.ebi.ep.model.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.model.WebStatXref;
import uk.ac.ebi.ep.model.dao.WebStatXrefView;
import uk.ac.ebi.ep.model.repositories.EnzymePortalChebiCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalMetaboliteRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.model.repositories.WebStatXrefRepository;

/**
 *
 * @author joseph
 */
@Service
public class WebStatService {
    
    @Autowired
    private UniprotXrefRepository xrefRepository;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;
    
    @Autowired
    private EnzymePortalCompoundRepository compoundRepository;
    
    @Autowired
    private EnzymePortalPathwaysRepository pathwaysRepository;

//    @Autowired
//    private ChebiCompoundRepository chebiCompoundRepository;
    @Autowired
    private EnzymePortalReactionRepository enzymePortalReactionRepository;

//    @Autowired
//    private EnzymeReactionInfoRepository enzymeReactionInfoRepository;
//
//    @Autowired
//    private EnzymePortalReactantRepository enzymePortalReactantRepository;
    @Autowired
    private EnzymePortalChebiCompoundRepository enzymePortalChebiCompoundRepository;
    
    @Autowired
    private EnzymePortalMetaboliteRepository enzymePortalMetaboliteRepository;
    
    @Autowired
    private WebStatXrefRepository webStatXrefRepository;

//    @Autowired
//    private EnzymePortalCofactorRepository enzymePortalCofactorRepository;
    public void LoadWebStatXref() {
        for (int x = 1; x < 7; x++) {
            WebStatXref xref = queryXrefStat(x);
            webStatXrefRepository.save(xref);
        }
    }
    
    public WebStatXrefView findWebStatisticsXrefByReleaseId(String releaseId) {
        return webStatXrefRepository.findWebStatXrefViewByReleaseId(releaseId);
    }
    
    public List<WebStatXrefView> findWebStatisticsXref() {
        return webStatXrefRepository.findWebStatXrefView();
    }
    
        public List<WebStatXrefView> findWebStatXrefReleaseMonths() {
        return webStatXrefRepository.findWebStatXrefReleaseMonths();
    }
    
    private WebStatXref queryXrefStat(int month) {
        CompletableFuture<Long> uniprotFuture = CompletableFuture.supplyAsync(() -> uniprotEntryRepository.countUniprotEntries());
        
        long intenz = uniprotEntryRepository.countINTENZ();
        System.out.println("intenz " + intenz);
        long omim = uniprotEntryRepository.countUniqueOmimIds();
        System.out.println("OMIM " + omim);
        long pdb = xrefRepository.countUniquePdbIds();
        System.out.println("pdb " + pdb);
        long metabolights = enzymePortalMetaboliteRepository.countUniqueMetabolightsIds();
        System.out.println("metabolite " + metabolights);
        CompletableFuture<Long> keggFuture = CompletableFuture.supplyAsync(() -> enzymePortalReactionRepository.countUniqueKeggIds());
        
        long rhea = enzymePortalReactionRepository.countUniqueRheaIds();
        System.out.println("rhea " + rhea);
        long reactome = xrefRepository.countUniqueReactomeIds();
        System.out.println("reactome " + reactome);
        long chembl = compoundRepository.countUniqueChemblIds();
        System.out.println("chembl " + chembl);
        CompletableFuture<Long> chebiFuture = CompletableFuture.supplyAsync(() -> enzymePortalChebiCompoundRepository.countUniqueChebiIds());
        
        long uniprot = uniprotFuture.join();// uniprotEntryRepository.countUniprot();
        System.out.println("uniprot " + uniprot);
        long chebi = chebiFuture.join();// enzymePortalChebiCompoundRepository.countChEBI();
        System.out.println("chebi " + chebi);
        long kegg = keggFuture.join();// enzymePortalReactionRepository.countKegg();
        System.out.println("kegg " + kegg);
        
        WebStatXref xref = new WebStatXref();
        xref.setBrenda(0);
        xref.setChebi(chebi);
        xref.setChembl(chembl);
        xref.setEuropepmc(0);
        xref.setIntenz(intenz);
        xref.setKegg(kegg);
        xref.setMcsa(0);
        xref.setMetabolights(metabolights);
        xref.setOmim(omim);
        xref.setPdbe(pdb);
        xref.setReactome(reactome);
        xref.setReleaseDate(new Date());
        
        String releaseId = getReleaseId(month);
        xref.setReleaseId(releaseId);
        xref.setRhea(rhea);
        xref.setUniprot(uniprot);
        
        return xref;
        
    }
    
    private String getReleaseId(int monthInNumber) {
        int year = LocalDate.now().getYear();
        // Month month = LocalDate.now().getMonth();
        Month month = Month.of(monthInNumber);
        Locale locale = Locale.getDefault();
       // return String.format("%s-%d", month.getDisplayName(TextStyle.SHORT, locale), year);
        return String.format("%d-%d", month.getValue(), year);
    }
    
    private String getReleaseId() {
        int year = LocalDate.now().getYear();
         int month = LocalDate.now().getMonthValue();
        //Month month = LocalDate.of(2019, Month.DECEMBER, 25).getMonth();
       // Locale locale = Locale.getDefault();
        //return String.format("%s-%d", month.getDisplayName(TextStyle.SHORT, locale), year);
        return String.format("%d-%d", month, year);
    }
    
    public static String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
    
    public void stat() {
        //queryXrefStat();
        queryComponentStat();
    }
//02:06 min

    private void queryComponentStat() {

        //pdb
        long proteinStructure = xrefRepository.countEntriesWithProteinStructure();
        System.out.println("PDB " + proteinStructure);
        //disease
        long disease = uniprotEntryRepository.countEntriesWithDiseases();
        System.out.println("DISEASE " + disease);
        
        long pathways = pathwaysRepository.countEntriesWithPathways();
        System.out.println("PATHWAYS " + pathways);

        //catalytic
        CompletableFuture<Long> activityFuture = CompletableFuture.supplyAsync(() -> uniprotEntryRepository.countEntriesWithCatalyticActivities());

        //reaction
        CompletableFuture<Long> rheaFuture = CompletableFuture.supplyAsync(() -> enzymePortalReactionRepository.countEntriesWithRheaReaction());
        long rheaReaction = rheaFuture.join();// enzymePortalReactionRepository.countRhea(); //
        System.out.println("REACTION " + rheaReaction);

        //cofactor
        long cofactors = compoundRepository.countEntriesWithCofactors();
        System.out.println("COFACTOR " + cofactors);
        //inhibitors
        CompletableFuture<Long> inhibitorFuture = CompletableFuture.supplyAsync(() -> compoundRepository.countEntriesWithInhibitors());

        //activators
        CompletableFuture<Long> activatorFuture = CompletableFuture.supplyAsync(() -> compoundRepository.countEntriesWithActivators());

        //metabolites
        CompletableFuture<Long> metaboliteFuture = CompletableFuture.supplyAsync(() -> enzymePortalMetaboliteRepository.countEntriesWithMetabolites());

        //exp_evidence
        long exp_evidence = uniprotEntryRepository.countEntriesWithExpEvidence();
        System.out.println("EVIDENCE " + exp_evidence);
        //reviewed
        long reviewed = uniprotEntryRepository.countReviewedEntries();
        System.out.println("REVIEWS " + reviewed);
        //unreviewed
        long unreviewed = uniprotEntryRepository.countUnreviewedEntries();
        System.out.println("TREMBL " + unreviewed);
        long metabolites = metaboliteFuture.join();
        System.out.println("METABOLITES " + metabolites);
        long catalyticActivities = activityFuture.join();// uniprotEntryRepository.countCatalyticActivities();
        System.out.println("catalytic " + catalyticActivities);
        long inhibitors = inhibitorFuture.join();// compoundRepository.countInhibitors();
        System.out.println("INHIBOTORS " + inhibitors);
        long activators = activatorFuture.join();// compoundRepository.countActivators();
        System.out.println("ACTIVATORS " + activators);
        
    }
    
}
