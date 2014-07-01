package uk.ac.ebi.ep.adapter.literature;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.ac.ebi.cdb.webservice.Result;

public class UniprotJapiLiteratureCallerTest {

	@Test
	public void testCall() throws Exception {
		UniprotJapiLiteratureCaller caller =
				new UniprotJapiLiteratureCaller("Q9NP56");
		Collection<Result> citations = caller.call();
		assertTrue(citations.size() >= 1);
		Result citation = citations.iterator().next();
              
              //This data can change anytime as they are being updated from time to time
		assertEquals("The DNA sequence and analysis of human chromosome 6.",
				citation.getTitle());
		assertEquals("2003", citation.getJournalInfo().getYearOfPublication().toString());
//		assertEquals(5, citation.getAuthorList().getAuthor().size());
//		assertEquals("MED", citation.getSource());
//		assertEquals("4030726", citation.getId());
	}

}
