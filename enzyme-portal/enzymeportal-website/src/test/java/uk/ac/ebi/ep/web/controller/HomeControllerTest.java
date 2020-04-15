
package uk.ac.ebi.ep.web.controller;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author joseph
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {
    

    	@Autowired
	private MockMvc mvc;

	@Test
	public void testHomePage() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.ALL))
				.andExpect(status().isOk());
				
	}
    	@Test
	public void testStatusPage() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/status/json").accept(MediaType.ALL))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("UP")));
	}
}
