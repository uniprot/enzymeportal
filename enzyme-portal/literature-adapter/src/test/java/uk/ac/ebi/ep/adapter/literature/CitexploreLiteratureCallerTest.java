package uk.ac.ebi.ep.adapter.literature;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.cdb.webservice.Result;
//@Ignore
public class CitexploreLiteratureCallerTest {

	@Test
	@Ignore("This one does not work")
	public void testCall() throws Exception {
		CitexploreLiteratureCaller caller = new CitexploreLiteratureCaller("P12345");
		Collection<Result> citations = caller.call();
		assertEquals(1, citations.size());
		Result citation = citations.iterator().next();
		assertEquals("Aspartate aminotransferase isozymes from rabbit liver. Purification and properties.",
				citation.getTitle());
		assertEquals("1985", citation.getJournalInfo().getYearOfPublication().toString());
		assertEquals(5, citation.getAuthorList().getAuthor().size());
		assertEquals("MED", citation.getSource());
		assertEquals("4030726", citation.getId());
	}

}
