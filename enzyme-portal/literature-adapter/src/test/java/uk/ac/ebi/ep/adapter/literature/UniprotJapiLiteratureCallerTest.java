package uk.ac.ebi.ep.adapter.literature;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import uk.ac.ebi.cdb.webservice.Citation;

public class UniprotJapiLiteratureCallerTest {

	@Test
	public void testCall() throws Exception {
		UniprotJapiLiteratureCaller caller =
				new UniprotJapiLiteratureCaller("P12345");
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
