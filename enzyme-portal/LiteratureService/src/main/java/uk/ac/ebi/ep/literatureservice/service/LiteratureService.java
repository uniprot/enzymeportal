package uk.ac.ebi.ep.literatureservice.service;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.uniprotservice.transferObjects.CitationLabel;
import uk.ac.ebi.ep.uniprotservice.transferObjects.LabelledCitation;

/**
 *
 * @author joseph
 */
@Service
public class LiteratureService {

    private final PmcRestService pmcRestService;
    private static final Comparator<LabelledCitation> SORT_BY_DATE_ASC = (LabelledCitation key1, LabelledCitation key2) -> -(key1.getCitation().getDateOfCreation().compareTo(key2.getCitation().getDateOfCreation()));

    public LiteratureService(PmcRestService pmcRestService) {
        this.pmcRestService = pmcRestService;
    }

    private Optional<EuropePMC> findCitationsByAccession(String accession, int limit) {

        return pmcRestService.findPublicationsByAccession(accession, limit);

    }

    private Optional<EuropePMC> findCitationsByKeyword(String keyword) {

        return pmcRestService.findPublicationsByKeyword(keyword);
    }

    private Optional<EuropePMC> findCitationsByKeyword(String keyword, int limit) {

        return pmcRestService.findPublicationsByKeyword(keyword, limit);
    }

    /**
     * searches EUPMC service using a keyword (term)
     *
     * @param term could be (enzyme|protein name)
     * @return EuropePMC
     */
    public EuropePMC getCitationsBySearchTerm(String term) {
        return findCitationsByKeyword(term).orElse(new EuropePMC());

    }

    /**
     * searches EUPMC service using a keyword (term)
     *
     * @param term could be (enzyme|protein name)
     * @param limit number of citations to return
     * @return EuropePMC
     */
    public EuropePMC getCitationsBySearchTerm(String term, int limit) {
        return findCitationsByKeyword(term, limit).orElse(new EuropePMC());

    }

    public List<LabelledCitation> getCitationsByAccession(String accession, int limit) {

        EuropePMC pmc = findCitationsByAccession(accession, limit).orElse(new EuropePMC());
        return getLabelledCitations(pmc, limit);

    }

    public EuropePMC findCitationsByUniprotAccession(String accession, int limit) {

        return findCitationsByAccession(accession, limit).orElse(new EuropePMC());

    }

    private List<LabelledCitation> getLabelledCitations(EuropePMC pmc, int limit) {
        return computeLabelledCitation(pmc)
                .stream()
                .distinct()
                .sorted(SORT_BY_DATE_ASC)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<LabelledCitation> computeLabelledCitation(EuropePMC pmc) {
        List<LabelledCitation> citations = new LinkedList<>();
        pmc.getResultList().getResult().stream().forEach(result -> {
            EnumSet<CitationLabel> labels = getLabels(result);
            LabelledCitation citation = new LabelledCitation(result, labels);
//add only citations with label
            if (citations.contains(citation)) {
                if (!citation.getLabels().isEmpty()) {
                    citations.get(citations.indexOf(citation))
                            .addLabels(labels);

                }
            } else {
                if (!citation.getLabels().isEmpty()) {
                    citations.add(citation);
                }
            }
        });

        return citations;
    }

    /**
     * Chooses the proper labels for a citation according to the database
     * cross-references it has got.
     *
     * @param citation a citation from PudMed.
     * @return a set of labels appropriate for the citation.
     */
    private EnumSet<CitationLabel> getLabels(Result citation) {
        EnumSet<CitationLabel> labels = EnumSet.noneOf(CitationLabel.class);

        if (citation.getDbCrossReferenceList() != null) {
            citation.getDbCrossReferenceList().getDbName().stream()
                    .map(dbName -> Optional.ofNullable(CitationLabel.forDatabase(dbName)))
                    .filter(label -> label.isPresent())
                    .forEach(label -> labels.add(label.get()));
        }
        return labels;
    }

}
