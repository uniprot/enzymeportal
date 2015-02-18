package uk.ac.ebi.ep.adapter.literature;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.cdb.webservice.AuthorsList;
import uk.ac.ebi.cdb.webservice.Journal;
import uk.ac.ebi.cdb.webservice.JournalInfo;
import uk.ac.ebi.cdb.webservice.PatentApplication;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.AgricolaId;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Author;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Book;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Citation;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.CitationTypeEnum;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.CitationXrefs;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.ElectronicArticle;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.JournalArticle;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.JournalName;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Page;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Patent;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.PubMedId;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Volume;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;

/**
 * Caller to retrieve citations from UniProt using the UniProtJAPI. Citations
 * are "filled" with data from CiteXplore web services whenever possible,
 * otherwise (in case of connection timeouts, for example) with the data
 * available from UniProt.
 *
 * @author $Author$
 *
 */
public class UniprotJapiLiteratureCaller
        implements Callable<Set<uk.ac.ebi.cdb.webservice.Result>> {

    private static final Logger LOGGER = Logger.getLogger(UniprotJapiLiteratureCaller.class);

    private String uniprotId;
    private Integer connectTimeout;
    private Integer readTimeout;

    /**
     * Constructor with a UniProt ID to search.
     *
     * @param uniprotId The UniProt ID to get citations from.
     */
    public UniprotJapiLiteratureCaller(String uniprotId) {
        this(uniprotId, 0, 0);
    }

    /**
     * Complete constructor.
     *
     * @param uniprotId The UniProt ID to search.
     * @param connectTimeout the connection timeout for CiteXplore WS.
     * @param readTimeout the read timeout for CiteXplore WS.
     * @since 1.0.7
     */
    public UniprotJapiLiteratureCaller(String uniprotId,
            Integer connectTimeout, Integer readTimeout) {
        this.uniprotId = uniprotId;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public Set<uk.ac.ebi.cdb.webservice.Result> call()
            throws Exception {
        // CiteXplore citations:
        HashSet<uk.ac.ebi.cdb.webservice.Result> cxCits = cxCits = new HashSet<>();;
  
        EntryRetrievalService service = UniProtJAPI.factory.getEntryRetrievalService();
  
        UniProtEntry entry = service.getUniProtEntry(uniprotId);

        for(Citation citation : entry.getCitationsNew()){
           
            if(citation != null){ 
            if(citation.getCitationType() == CitationTypeEnum.JOURNAL_ARTICLE
                    || citation.getCitationType() ==CitationTypeEnum.BOOK || 
                    citation.getCitationType() == CitationTypeEnum.PATENT){
               
            uk.ac.ebi.cdb.webservice.Result cxCit = buildCitation(citation); 
                cxCits.add(cxCit);
               
            }
            }
                    
        }
        return cxCits;
    }

    /**
     * Builds a citation (citeXplore model) from a UniProt citation.
     *
     * @param upCit the UniProt citation.
     * @return a citation in the citeXplore model, or <code>null</code> if the
     * citation from UniProt lacks of a database ID (see {@link
     *      #loadXrefs(uk.ac.ebi.kraken.interfaces.uniprot.Citation, uk.ac.ebi.cdb.webservice.Result)}).
     */
    private uk.ac.ebi.cdb.webservice.Result buildCitation(Citation upCit) {
        uk.ac.ebi.cdb.webservice.Result cxCit
                = new uk.ac.ebi.cdb.webservice.Result();
        loadXrefs(upCit, cxCit);
        // Some UniProt citations - ex. submissions - don't have PMIDs:
        if (cxCit.getId() == null) {
            return null;
        }
        JournalInfo issue = new JournalInfo();
        Journal journal = new Journal();
        issue.setJournal(journal);
        cxCit.setJournalInfo(issue);
        loadCoreMetadata(upCit, cxCit);
        // Special bits:
        switch (upCit.getCitationType()) {
            case BOOK:
                Book book = (Book) upCit;
                load(cxCit, book);
                break;
            case PATENT:
                Patent patent = (Patent) upCit;
                load(cxCit, patent);
                break;
            case JOURNAL_ARTICLE:
                JournalArticle journalArticle = (JournalArticle) upCit;
                load(cxCit, journalArticle);
                break;
            /* Ignoring other cases */
        }
        return cxCit;
    }

    /**
     * Loads core metadata - title, abstract, authors, year - from a UniProt
     * citation into a CiteXplore citation.
     *
     * @param upCit
     * @param cxCit
     */
    private void loadCoreMetadata(Citation upCit,
            uk.ac.ebi.cdb.webservice.Result cxCit) {
        // Title:
        if (upCit.hasTitle()) {
            cxCit.setTitle(upCit.getTitle().getValue());
        }
		// Abstract:
		/* No info from old Citation objects (wait for citationNew package)
         List<CitationSummary> upAbstracts = upCit.getCitationSummary();
         if (upAbstracts != null && !upAbstracts.isEmpty()){
         cxCit.setAbstractText(upAbstracts.get(0).getValue());
         }
         */
        // Authors:
        if (upCit.getAuthors() != null || !upCit.getAuthors().isEmpty()) {
            cxCit.setAuthorList(new AuthorsList());
            for (Author upAuthor : upCit.getAuthors()) {
                uk.ac.ebi.cdb.webservice.Authors cxAuthor
                        = new uk.ac.ebi.cdb.webservice.Authors();
                cxAuthor.setFullName(upAuthor.getValue());
                cxCit.getAuthorList().getAuthor().add(cxAuthor);
            }
        }
        // Year:
        if (upCit.getPublicationDate() != null) {
            // UniProt's publication date might come as 'JUL-2010':
            Pattern p = Pattern.compile("(?:\\w{3}-)?(\\d{4})(?:-\\d{2}(?:-\\d{2}(?:.*)?)?)?");
            String upDate = upCit
                    .getPublicationDate().getValue();
            Matcher m = p.matcher(upDate);
            if (m.matches()) {
                final String pubYear = m.group(1);
                cxCit.getJournalInfo().setYearOfPublication(
                        Short.valueOf(pubYear));
                cxCit.setPubYear(pubYear);
            } else {
                LOGGER.warn("Date pattern not recognised: " + upDate);
            }
        }
        // Volume:
//		if (upCit. instanceof HasVolume){
//			Volume volume = ((HasVolume) upCit).getVolume();
//			if (volume != null){
//				cxCit.getJournalInfo().setVolume(volume.getValue());
//			}
//		}
//		// Journal name:
//		if (upCit instanceof HasJournalName){
//			cxCit.getJournalInfo().getJournal().setTitle(
//					((HasJournalName) upCit).getJournalName().getValue());
//		}
//		// Pages:
//		if (upCit instanceof HasPages){
//			setPages(cxCit, ((HasPages) upCit).getFirstPage(),
//					((HasPages) upCit).getLastPage());
//		}
    }

    /**
     * Loads metadata from a UniProt Book object into a CiteXplore Result
     * object.
     *
     * @param cxCit The CiteXplore Result to load metadata into.
     * @param book The UniProt object to get metadata from.
     */
    private void load(uk.ac.ebi.cdb.webservice.Result cxCit, Book book) {
        cxCit.setTitle(book.getBookName().getValue());
        cxCit.getBookOrReportDetails().setPublisher(
                book.getPublisher().getValue());
        if (book.getVolume() != null && !book.getVolume().getValue().isEmpty()) {
            cxCit.getJournalInfo().setVolume(book.getVolume().getValue());
        }

    }

    /**
     * Loads metadata from a UniProt ElectronicArticle object into a CiteXplore
     * Result object.
     *
     * @param cxCit The CiteXplore Result to load metadata into.
     * @param eArticle The UniProt object to get metadata from.
     */
    private void load(uk.ac.ebi.cdb.webservice.Result cxCit,
            ElectronicArticle eArticle) {
        // TODO
    }

    /**
     * Loads metadata from a UniProt JournalArticle object into a CiteXplore
     * Result object.
     *
     * @param cxCit The CiteXplore Result to load metadata into.
     * @param jArticle The UniProt object to get metadata from.
     */
    private void load(uk.ac.ebi.cdb.webservice.Result cxCit,
            JournalArticle jArticle) {
        Page firstPage = jArticle.getFirstPage();
        Page lastPage =jArticle.getLastPage();
        Volume volume =jArticle.getVolume();
        JournalName journalName =jArticle.getJournalName();
        cxCit.setPageInfo(firstPage.getValue() + "-"+ lastPage.getValue());
        cxCit.setJournalTitle(journalName.getValue());
        cxCit.setJournalVolume(volume.getValue());
      
        
                
    }

    /**
     * Loads metadata from a UniProt Patent object into a CiteXplore Result
     * object.
     *
     * @param cxCit The CiteXplore Result to load metadata into.
     * @param patent The UniProt object to get metadata from.
     */
    private void load(uk.ac.ebi.cdb.webservice.Result cxCit, Patent patent) {
        PatentApplication app = new PatentApplication();
        app.setApplicationNumber(patent.getPatentNumber().getValue());
        cxCit.getPatentDetails().setApplication(app);
    }

    private void setPages(uk.ac.ebi.cdb.webservice.Result cxCit, Page firstPage,
            Page lastPage) {
		if (firstPage != null){
            StringBuilder pages = new StringBuilder(firstPage.getValue());
			if (lastPage != null){
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
    private void loadXrefs(Citation upCit,
            uk.ac.ebi.cdb.webservice.Result cxCit) {
     
        CitationXrefs xref = upCit.getCitationXrefs();
        if (xref.hasPubmedId()) {
            cxCit.setSource(DataSource.MED.name());
            cxCit.setId(xref.getPubmedId().getValue());
        } else if (xref.hasAgricolaId()) {

            cxCit.setSource(DataSource.AGR.name());
            cxCit.setId(xref.getAgricolaId().getValue());
        }
      
    }

    private uk.ac.ebi.cdb.webservice.Result getCitationFromCitexplore(Citation upCit) {
        uk.ac.ebi.cdb.webservice.Result cxCit = null;
        CitexploreWSClient citexploreClient = null;
        String citId = null;
        try {
            citexploreClient
                    = CitexploreWSClientPool.getInstance().borrowObject();
            citexploreClient.setConnectTimeout(connectTimeout);
            citexploreClient.setReadTimeout(readTimeout);
             CitationXrefs xref = upCit.getCitationXrefs();
            if (xref.hasPubmedId()) {
                PubMedId pubmedId = xref.getPubmedId();
                if (pubmedId != null) {
                    citId = pubmedId.getValue();
                    if (citId != null && citId.length() > 0) {
                        cxCit = citexploreClient.retrieveCitation(
			DataSource.MED, citId);
                    }
                }
            } else if (xref.hasAgricolaId()) {
                AgricolaId agricolaId = xref.getAgricolaId();
                if (agricolaId != null) {
                    citId = agricolaId.getValue();
                    if (citId != null && citId.length() > 0) {
                        cxCit = citexploreClient.retrieveCitation(
                                DataSource.AGR, citId);
                    }
                }
            }
//            else if(xref.hasMedlineId()){
//                   MedlineId medlineId = xref.getMedlineId();
//                if (medlineId != null) {
//                    citId = medlineId.getValue();
//                    if (citId != null && citId.length() > 0) {
//                        cxCit = citexploreClient.retrieveCitation(
//                                DataSource.MED, citId);
//                    }
//                } 
//            }
        } catch (Exception e) {
            LOGGER.error("Unable to get citation from CiteXplore: " + citId, e);
        } finally {
            if (citexploreClient != null) {
                try {
                    CitexploreWSClientPool.getInstance()
                            .returnObject(citexploreClient);
                } catch (Exception e) {
                    LOGGER.error("Unable to return CiteXplore client", e);
                }
            }
        }
        return cxCit;
    }

}
