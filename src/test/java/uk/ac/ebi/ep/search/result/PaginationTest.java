package uk.ac.ebi.ep.search.result;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PaginationTest {

	@Test
	public void testOnePage(){
		Pagination pagination = new Pagination(3, 10);
		assertEquals(0, pagination.getFirstResult());
		assertEquals(2, pagination.getLastResult());
		assertEquals(1, pagination.getCurrentPage());
		assertEquals(1, pagination.getLastPage());
	}
	
	@Test
	public void testSeveralPages() {
		Pagination pagination = new Pagination(23, 10);
		assertEquals(0, pagination.getFirstResult());
		assertEquals(9, pagination.getLastResult());
		assertEquals(1, pagination.getCurrentPage());
		assertEquals(3, pagination.getLastPage());
		
		pagination.setFirstResult(10);
		assertEquals(19, pagination.getLastResult());
		assertEquals(2, pagination.getCurrentPage());
		assertEquals(3, pagination.getLastPage());
		
		pagination.setFirstResult(20);
		assertEquals(22, pagination.getLastResult());
		assertEquals(3, pagination.getCurrentPage());
		assertEquals(3, pagination.getLastPage());
	}

	@Test
	public void testEmptyResults(){
		Pagination pagination = new Pagination(0, 10);
		assertEquals(-1, pagination.getFirstResult());
		assertEquals(-1, pagination.getLastResult());
		assertEquals(0, pagination.getCurrentPage());
		assertEquals(0, pagination.getLastPage());
	}
}
