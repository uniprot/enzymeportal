package uk.ac.ebi.ep.adapter.literature;

import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.cdb.webservice.Journal;
import uk.ac.ebi.cdb.webservice.JournalInfo;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;
@Ignore
public class DASLiteratureCallerTest {

	private DASLiteratureCaller dasCaller;
	
	@Before
	public void init(){
		dasCaller = new DASLiteratureCaller(IDASFeaturesAdapter.PDBE_DAS_URL, "1mbd");
	}
	
	@Test
	public void testCall() throws Exception {
		Set<Result> citations = dasCaller.call();
		assertTrue(citations.size() >= 1);
		Result cit = citations.iterator().next();
		assertEquals("MED", cit.getSource());
		assertEquals("7278969", cit.getId());
		assertEquals("Neutron diffraction reveals oxygen-histidine hydrogen bond in oxymyoglobin.",
				cit.getTitle());
		assertEquals("Nature", cit.getJournalInfo().getJournal().getTitle());
		assertEquals("292", cit.getJournalInfo().getVolume());
	}
	
	@Test
	public void testGetCitationFromCitexplore() throws Exception {
		// No assertions, just to measure time (testCall is slow because of DAS)
		Result citation = dasCaller.getCitationFromCitexplore("7278969");
	}

	@Test
	public void testGetPubmedId() {
		String url = "http://www.ebi.ac.uk/pdbe-srv/view/search/index/?Citation_pubmed_id=7278969";
		assertEquals("7278969", dasCaller.getPubMedId(url));
	}

	@Test
	public void testParseJournal() {
		String journal = "NATURE vol:292 page:81-82 (1981)";
		Result citation = new Result();
		JournalInfo jIssue = new JournalInfo();
		jIssue.setJournal(new Journal());
		citation.setJournalInfo(jIssue);
		dasCaller.parseJournal(journal, citation);
		assertEquals("NATURE", citation.getJournalInfo().getJournal().getTitle());
		assertEquals("292", citation.getJournalInfo().getVolume());
		assertEquals("81-82", citation.getPageInfo());
		assertEquals("1981", citation.getJournalInfo().getYearOfPublication().toString());
	}

}
