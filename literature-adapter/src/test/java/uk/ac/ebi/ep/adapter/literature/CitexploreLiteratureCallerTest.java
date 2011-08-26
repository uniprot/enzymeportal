package uk.ac.ebi.ep.adapter.literature;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.cdb.webservice.Citation;
import uk.ac.ebi.cdb.webservice.QueryException_Exception;

public class CitexploreLiteratureCallerTest {

	@Test
	@Ignore("This one does not work")
	public void testCall() throws QueryException_Exception {
		CitexploreLiteratureCaller caller = new CitexploreLiteratureCaller("P12345");
		Collection<Citation> citations = caller.call();
		assertEquals(1, citations.size());
		Citation citation = citations.iterator().next();
		assertEquals("Aspartate aminotransferase isozymes from rabbit liver. Purification and properties.",
				citation.getTitle());
		assertEquals("1985", citation.getJournalIssue().getYearOfPublication().toString());
		assertEquals(5, citation.getAuthorCollection().size());
		assertEquals("MED", citation.getDataSource());
		assertEquals("4030726", citation.getExternalId());
	}

}
