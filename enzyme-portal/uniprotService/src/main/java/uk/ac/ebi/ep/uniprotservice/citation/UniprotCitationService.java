/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.citation;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import uk.ac.ebi.ep.uniprotservice.eupmc.AuthorList;
import uk.ac.ebi.ep.uniprotservice.eupmc.Journal;
import uk.ac.ebi.ep.uniprotservice.eupmc.JournalInfo;
import uk.ac.ebi.ep.uniprotservice.eupmc.Result;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Author;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Citation;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.CitationTypeEnum;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.CitationXrefs;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.JournalArticle;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.JournalName;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Page;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Volume;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;

/**
 *
 * @author joseph
 */
public class UniprotCitationService {

    //http://europepmc.org/RestfulWebService
    private static final Logger LOGGER = Logger.getLogger(UniprotCitationService.class);

    @Bean
    public UniProtService uniProtService() {
        ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
        UniProtService uniprotService = serviceFactoryInstance.getUniProtQueryService();
        uniprotService.start();
        return uniprotService;
    }

    public List<Result> computeCitation(String accession) throws ServiceException {
        EntryRetrievalService entryRetrievalService = UniProtJAPI.factory.getEntryRetrievalService();
        UniProtEntry entry = (UniProtEntry) entryRetrievalService.getUniProtEntry(accession);
        //UniProtEntry entry =   uniProtService().getEntry(accession);

        final HashSet<Result> results = new HashSet<>();

        List<Citation> citations = entry.getCitationsNew();

        citations.stream().forEach((Citation citation) -> {
//            if (c.getCitationXrefs().hasPubmedId()) {
//                System.out.println("Citation " + c.getTitle() + " Type: " + c.getCitationType().getDisplayName());
//                System.out.println("PUBMED " + c.getCitationXrefs().hasPubmedId());
//            }
//            

            if (citation != null) {
                if (citation.getCitationType() == CitationTypeEnum.JOURNAL_ARTICLE
                        || citation.getCitationType() == CitationTypeEnum.BOOK
                        || citation.getCitationType() == CitationTypeEnum.PATENT) {

                    Result result = buildCitation(citation);
                    results.add(result);
                    //System.out.println("Citation " + citation.getTitle() + " Type: " + citation.getCitationType().getDisplayName());

                }
            }

        });
        return results.stream().collect(Collectors.toList());
    }



    private Result buildCitation(Citation citation) {
        Result result
                = new Result();

        loadXrefs(citation, result);
        // Some UniProt citations - ex. submissions - don't have PMIDs:
        if (result.getId() == null) {
            return null;
        }
        JournalInfo issue = new JournalInfo();
        Journal journal = new Journal();
        issue.setJournal(journal);
        result.setJournalInfo(issue);
        loadCoreMetadata(citation, result);
        // Special bits:
        switch (citation.getCitationType()) {
//            case BOOK:
//                Book book = (Book) citation;
//                load(result, book);
//                break;
//            case PATENT:
//                Patent patent = (Patent) citation;
//                load(result, patent);
//                break;
            case JOURNAL_ARTICLE:
                JournalArticle journalArticle = (JournalArticle) citation;
                load(result, journalArticle);
                break;

        }
        return result;
    }

    /**
     * Loads core metadata - title, abstract, authors, year - from a UniProt
     * citation into a CiteXplore citation.
     *
     * @param citation
     * @param result
     */
    private void loadCoreMetadata(Citation citation,
            Result result) {
        // Title:
        if (citation.hasTitle()) {
            result.setTitle(citation.getTitle().getValue());
        }
		// Abstract:
		/*
         * No info from old Citation objects (wait for citationNew package)
         * List<CitationSummary> upAbstracts = citation.getCitationSummary(); if
         * (upAbstracts != null && !upAbstracts.isEmpty()){
         * result.setAbstractText(upAbstracts.get(0).getValue()); }
         */
        // Authors:
        if (citation.getAuthors() != null || !citation.getAuthors().isEmpty()) {
            result.setAuthorList(new AuthorList());
            for (Author upAuthor : citation.getAuthors()) {
                //Author cxAuthor = new Author();
                // cxAuthor.setFullName(upAuthor.getValue());
                //result.getAuthorList().getAuthor().add(cxAuthor);
                uk.ac.ebi.ep.uniprotservice.eupmc.Author au = new uk.ac.ebi.ep.uniprotservice.eupmc.Author();
                au.setFullName(upAuthor.getValue());
                result.getAuthorList().getAuthor().add(au);
            }
        }
        // Year:
        if (citation.getPublicationDate() != null) {
            // UniProt's publication date might come as 'JUL-2010':
            Pattern p = Pattern.compile("(?:\\w{3}-)?(\\d{4})(?:-\\d{2}(?:-\\d{2}(?:.*)?)?)?");
            String upDate = citation
                    .getPublicationDate().getValue();
            Matcher m = p.matcher(upDate);
            if (m.matches()) {
                final String pubYear = m.group(1);
                result.getJournalInfo().setYearOfPublication(Integer.valueOf(pubYear));
                //result.setPubYear(pubYear);
            } else {
                LOGGER.warn("Date pattern not recognised: " + upDate);
            }
        }

    }

    /**
     * Loads metadata from a UniProt Book object into a CiteXplore Result
     * object.
     *
     * @param result The CiteXplore Result to load metadata into.
     * @param book The UniProt object to get metadata from.
     */
//    private void load(Result result, Book book) {
//        result.setTitle(book.getBookName().getValue());
//        result.getBookOrReportDetails().setPublisher(
//                book.getPublisher().getValue());
//        if (book.getVolume() != null && !book.getVolume().getValue().isEmpty()) {
//            result.getJournalInfo().setVolume(book.getVolume().getValue());
//        }
//
//    }
    /**
     * Loads metadata from a UniProt JournalArticle object into a CiteXplore
     * Result object.
     *
     * @param result The CiteXplore Result to load metadata into.
     * @param jArticle The UniProt object to get metadata from.
     */
    private void load(Result result,
            JournalArticle jArticle) {
        Page firstPage = jArticle.getFirstPage();
        Page lastPage = jArticle.getLastPage();
        Volume volume = jArticle.getVolume();
        JournalName journalName = jArticle.getJournalName();
        result.setPageInfo(firstPage.getValue() + "-" + lastPage.getValue());

        Journal journal = new Journal();
        journal.setTitle(journalName.getValue());
        JournalInfo info = new JournalInfo();
        info.setJournal(journal);
        info.setVolume(volume.getValue());
//        result.setJ.setJournalTitle(journalName.getValue());
//        result.setJournalVolume(volume.getValue());
//      

    }

    /**
     * Loads metadata from a UniProt Patent object into a CiteXplore Result
     * object.
     *
     * @param cxCit The CiteXplore Result to load metadata into.
     * @param patent The UniProt object to get metadata from.
     */
//    private void load(Result cxCit, Patent patent) {
//        PatentApplication app = new PatentApplication();
//        app.setApplicationNumber(patent.getPatentNumber().getValue());
//        cxCit..getPatentDetails().setApplication(app);
//    }
    private void setPages(Result cxCit, Page firstPage,
            Page lastPage) {
        if (firstPage != null) {
            StringBuilder pages = new StringBuilder(firstPage.getValue());
            if (lastPage != null) {
                pages.append('-').append(lastPage.getValue());
            }
            cxCit.setPageInfo(pages.toString());
        }
    }

    /**
     * Loads bibliography database xrefs from a UniProt citation into a
     * CiteXplore citation. Currently, only PubMed and Agricola are supported.
     *
     * @param upCit
     * @param cxCit
     */
    private void loadXrefs(Citation citation,
            Result r) {

        CitationXrefs xref = citation.getCitationXrefs();

        if (xref.hasPubmedId()) {
            r.setSource("MED");
            r.setId(xref.getPubmedId().getValue());
        } else if (xref.hasAgricolaId()) {

            r.setSource("AGR");
            r.setId(xref.getAgricolaId().getValue());
        }

    }
}
