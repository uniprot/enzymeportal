package uk.ac.ebi.ep.adapter.ebeye.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfGetResults;

public class TransformerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransformToListOfStringEnumArray() {
		FieldsOfGetResults[] items = new FieldsOfGetResults[2];
		items[0] = FieldsOfGetResults.id;
		items[1] = FieldsOfGetResults.acc;
		List<String> expected = new ArrayList<String>();
		expected.add("id");
		expected.add("acc");
		final List<String> actual = Transformer.transformToListOfString(items);
		assertEquals(expected, actual);
	}

}
