package uk.ac.ebi.ep.adapter.literature;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import uk.ac.ebi.cdb.webservice.Result;

public class UniprotJapiLiteratureCallerTest {

	@Test
	public void testCall() throws Exception {
		UniprotJapiLiteratureCaller caller =
				new UniprotJapiLiteratureCaller("P12345");
		Collection<Result> citations = caller.call();
		assertTrue(citations.size() >= 1);
		Result citation = citations.iterator().next();
		assertEquals("Aspartate aminotransferase isozymes from rabbit liver. Purification and properties.",
				citation.getTitle());
		assertEquals("1985", citation.getJournalInfo().getYearOfPublication().toString());
		assertEquals(5, citation.getAuthorList().getAuthor().size());
		assertEquals("MED", citation.getSource());
		assertEquals("4030726", citation.getId());
	}

}
