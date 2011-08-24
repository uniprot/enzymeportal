package uk.ac.ebi.ep.adapter.literature;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import uk.ac.ebi.cdb.webservice.Journal;
import uk.ac.ebi.cdb.webservice.JournalIssue;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Author;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Book;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Citation;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.CitationSummary;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Page;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Volume;
import uk.ac.ebi.kraken.uuw.services.remoting.Attribute;
import uk.ac.ebi.kraken.uuw.services.remoting.AttributeIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;

public class UniprotLiteratureCaller
implements Callable<Collection<uk.ac.ebi.cdb.webservice.Citation>> {

	private String uniprotId;

	public UniprotLiteratureCaller(String uniprotId){
		this.uniprotId = uniprotId;
	}
	
	public Collection<uk.ac.ebi.cdb.webservice.Citation> call()
	throws Exception {
		// CiteXplore citations:
		HashSet<uk.ac.ebi.cdb.webservice.Citation> cxCits = null;
		UniProtQueryService uniProtQueryService =
				UniProtJAPI.factory.getUniProtQueryService();
		Query query = UniProtQueryBuilder.buildIDListQuery(
				Arrays.asList(new String[]{ uniprotId }));
		AttributeIterator<UniProtEntry> it =
				uniProtQueryService.getAttributes(query, "ognl:citations");
		if (it.hasNext()) cxCits =
				new HashSet<uk.ac.ebi.cdb.webservice.Citation>();
		for (Attribute att: it){
			// UniProt citations:
			@SuppressWarnings("unchecked")
			List<Citation> upCits = (List<Citation>) att.getValue();
			for (Citation upCit :upCits){
				uk.ac.ebi.cdb.webservice.Citation cxCit =
						new uk.ac.ebi.cdb.webservice.Citation();
				// Title:
				cxCit.setTitle(upCit.getTitle().getValue());
				// Abstract:
				List<CitationSummary> upAbstracts = upCit.getCitationSummary();
				if (upAbstracts != null && !upAbstracts.isEmpty()){
					cxCit.setAbstractText(upAbstracts.get(0).getValue());
				}
				// Authors:
				for (Author upAuthor: upCit.getAuthors()){
					uk.ac.ebi.cdb.webservice.Author cxAuthor =
							new uk.ac.ebi.cdb.webservice.Author();
					cxAuthor.setFullName(upAuthor.getValue());
					cxCit.getAuthorCollection().add(cxAuthor);
				}
				// Journal/book, issue...:
				JournalIssue issue = new JournalIssue();
				Journal journal = new Journal();
				issue.setJournal(journal);
				cxCit.setJournalIssue(issue);
				switch (upCit.getCitationType()){
				case BOOK:
					Book book = (Book) upCit;
					cxCit.getJournalIssue().getJournal()
							.setTitle(book.getBookName().getValue());
					Volume volume = book.getVolume();
					if (volume != null){
						cxCit.getJournalIssue().setVolume(volume.getValue());
					}
					cxCit.getJournalIssue().setYearOfPublication(Short.valueOf(
							book.getPublicationDate().getValue()));
					Page firstPage = book.getFirstPage();
					Page lastPage = book.getLastPage();
					if (firstPage != null){
						StringBuilder pages = new StringBuilder(firstPage.getValue());
						if (lastPage != null){
							pages.append('-').append(lastPage.getValue());
						}
						cxCit.setPageInfo(pages.toString());
					}
					break;
				case ELECTRONIC_ARTICLE:
					break;
				case JOURNAL_ARTICLE:
					break;
				case PATENT:
					break;
				/* Ignoring other cases */
				}
				
				
				
				cxCit.setJournalIssue(issue);
				// Page info:
				cxCits.add(cxCit);
			}
		}
		return cxCits;
	}

}
