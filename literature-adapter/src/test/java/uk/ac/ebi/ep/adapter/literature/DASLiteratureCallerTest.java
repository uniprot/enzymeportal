package uk.ac.ebi.ep.adapter.literature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.cdb.webservice.Citation;
import uk.ac.ebi.cdb.webservice.Journal;
import uk.ac.ebi.cdb.webservice.JournalIssue;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;

public class DASLiteratureCallerTest {

	private DASLiteratureCaller dasCaller;
	
	@Before
	public void init(){
		dasCaller = new DASLiteratureCaller(IDASFeaturesAdapter.PDBE_DAS_URL, "1mbd");
	}
	
	@Test
	public void testCall() throws Exception {
		Set<Citation> citations = dasCaller.call();
		assertTrue(citations.size() >= 1);
		Citation cit = citations.iterator().next();
		assertEquals("MED", cit.getDataSource());
		assertEquals("7278969", cit.getExternalId());
		assertEquals("Neutron diffraction reveals oxygen-histidine hydrogen bond in oxymyoglobin.",
				cit.getTitle());
		assertEquals("Nature", cit.getJournalIssue().getJournal().getTitle());
		assertEquals("292", cit.getJournalIssue().getVolume());
	}
	
	@Test
	public void testGetCitationFromCitexplore() throws Exception {
		// No assertions, just to measure time (testCall is slow because of DAS)
		Citation citation = dasCaller.getCitationFromCitexplore("7278969");
	}

	@Test
	public void testGetPubmedId() {
		String url = "http://www.ebi.ac.uk/pdbe-srv/view/search/index/?Citation_pubmed_id=7278969";
		assertEquals("7278969", dasCaller.getPubMedId(url));
	}

	@Test
	public void testParseJournal() {
		String journal = "NATURE vol:292 page:81-82 (1981)";
		Citation citation = new Citation();
		JournalIssue jIssue = new JournalIssue();
		jIssue.setJournal(new Journal());
		citation.setJournalIssue(jIssue);
		dasCaller.parseJournal(journal, citation);
		assertEquals("NATURE", citation.getJournalIssue().getJournal().getTitle());
		assertEquals("292", citation.getJournalIssue().getVolume());
		assertEquals("81-82", citation.getPageInfo());
		assertEquals("1981", citation.getJournalIssue().getYearOfPublication().toString());
	}

}
