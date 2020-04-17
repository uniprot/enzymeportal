package uk.ac.ebi.ep.model.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.WebStatComponent;
import uk.ac.ebi.ep.model.WebStatXref;
import uk.ac.ebi.ep.model.dao.WebStatXrefView;
import uk.ac.ebi.ep.model.repositories.EnzymePortalChebiCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalMetaboliteRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.model.repositories.WebStatComponentRepository;
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

    @Autowired
    private EnzymePortalReactionRepository enzymePortalReactionRepository;

    @Autowired
    private EnzymePortalChebiCompoundRepository enzymePortalChebiCompoundRepository;

    @Autowired
    private EnzymePortalMetaboliteRepository enzymePortalMetaboliteRepository;

    @Autowired
    private WebStatXrefRepository webStatXrefRepository;

    @Autowired
    private WebStatComponentRepository webStatComponentRepository;

    @Modifying(clearAutomatically = true)
    @Transactional
    public void loadWebStatXrefToDatabase() {
        WebStatXref webStatXref = computeWebStatXref(null);
        WebStatXrefView statView = webStatXrefRepository.findWebStatXrefViewByReleaseId(webStatXref.getReleaseId());
        if (Objects.isNull(statView.getReleaseId())) {
            webStatXrefRepository.saveAndFlush(webStatXref);
        }

    }

    @Modifying(clearAutomatically = true)
    @Transactional
    public void loadWebStatComponentToDatabase() {
        WebStatComponent webStatComponent = computeWebStatComponent(null);
        WebStatComponent data = webStatComponentRepository.findByReleaseId(webStatComponent.getReleaseId());
        if (Objects.isNull(data.getReleaseId())) {
            webStatComponentRepository.saveAndFlush(webStatComponent);
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

    public WebStatXref computeWebStatXref(Month month) {
        if (month == null) {
            month = LocalDate.now().getMonth();
        }
        return computeWebStatXref(month.getValue());
    }

    private WebStatXref computeWebStatXref(int month) {
        CompletableFuture<Long> uniprotFuture = CompletableFuture.supplyAsync(() -> uniprotEntryRepository.countUniprotEntries());

        long intenz = uniprotEntryRepository.countINTENZ();

        long omim = uniprotEntryRepository.countUniqueOmimIds();

        long pdb = xrefRepository.countUniquePdbIds();

        long metabolights = enzymePortalMetaboliteRepository.countUniqueMetabolightsIds();

        CompletableFuture<Long> keggFuture = CompletableFuture.supplyAsync(() -> enzymePortalReactionRepository.countUniqueKeggIds());

        long rhea = enzymePortalReactionRepository.countUniqueRheaIds();

        long reactome = xrefRepository.countUniqueReactomeIds();

        long chembl = compoundRepository.countUniqueChemblIds();

        CompletableFuture<Long> chebiFuture = CompletableFuture.supplyAsync(() -> enzymePortalChebiCompoundRepository.countUniqueChebiIds());

        long uniprot = uniprotFuture.join();

        long chebi = chebiFuture.join();

        long kegg = keggFuture.join();

        WebStatXref xref = new WebStatXref();
        xref.setBrenda(0);
        xref.setMcsa(0);
        xref.setEuropepmc(0);
        xref.setChebi(chebi);
        xref.setChembl(chembl);
        xref.setIntenz(intenz);
        xref.setKegg(kegg);
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
        if (monthInNumber > 13 || monthInNumber < 1) {
            return getReleaseId();
        }

        int year = LocalDate.now().getYear();
        Month month = Month.of(monthInNumber);
        return String.format("%d-%d", month.getValue(), year);
    }

    private String getReleaseId() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return String.format("%d-%d", month, year);
    }

    public static String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public WebStatComponent computeWebStatComponent(Month month) {
        if (month == null) {
            month = LocalDate.now().getMonth();
        }
        return computeWebStatComponent(month.getValue());
    }

    private WebStatComponent computeWebStatComponent(int month) {

        //pdb
        long proteinStructure = xrefRepository.countEntriesWithProteinStructure();

        //disease
        long disease = uniprotEntryRepository.countEntriesWithDiseases();

        long pathways = pathwaysRepository.countEntriesWithPathways();

        //catalytic
        CompletableFuture<Long> activityFuture = CompletableFuture.supplyAsync(() -> uniprotEntryRepository.countEntriesWithCatalyticActivities());

        //reaction
        CompletableFuture<Long> rheaFuture = CompletableFuture.supplyAsync(() -> enzymePortalReactionRepository.countEntriesWithRheaReaction());
        long rheaReaction = rheaFuture.join();

        //cofactor
        long cofactors = compoundRepository.countEntriesWithCofactors();

        //inhibitors
        CompletableFuture<Long> inhibitorFuture = CompletableFuture.supplyAsync(() -> compoundRepository.countEntriesWithInhibitors());

        //activators
        CompletableFuture<Long> activatorFuture = CompletableFuture.supplyAsync(() -> compoundRepository.countEntriesWithActivators());

        //metabolites
        CompletableFuture<Long> metaboliteFuture = CompletableFuture.supplyAsync(() -> enzymePortalMetaboliteRepository.countEntriesWithMetabolites());

        //exp_evidence
        long exp_evidence = uniprotEntryRepository.countEntriesWithExpEvidence();

        //reviewed
        long reviewed = uniprotEntryRepository.countReviewedEntries();

        //unreviewed
        long unreviewed = uniprotEntryRepository.countUnreviewedEntries();

        long metabolites = metaboliteFuture.join();

        long catalyticActivities = activityFuture.join();

        long inhibitors = inhibitorFuture.join();

        long activators = activatorFuture.join();

        WebStatComponent component = new WebStatComponent();
        component.setActivators(activators);
        component.setCatalyticActivities(catalyticActivities);
        component.setCitations(0);
        component.setCofactors(cofactors);
        component.setDiseases(disease);
        component.setExpEvidence(exp_evidence);
        component.setInhibitors(inhibitors);
        component.setMetabolites(metabolites);
        component.setPathways(pathways);
        component.setProteinStructure(proteinStructure);
        component.setReactionMechanisms(0);
        component.setReactionParameters(0);
        component.setReactions(rheaReaction);
        component.setReleaseDate(new Date());
        component.setReviewed(reviewed);
        component.setUnreviewed(unreviewed);

        String releaseId = getReleaseId(month);
        component.setReleaseId(releaseId);

        return component;

    }

}
