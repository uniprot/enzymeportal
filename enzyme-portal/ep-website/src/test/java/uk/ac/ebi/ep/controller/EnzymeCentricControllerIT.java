/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.web.utils.SearchUtil;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(classes = {WebTestConfig.class, ApplicationContextMock.class})
@WebAppConfiguration
public class EnzymeCentricControllerIT {

    @Autowired
    protected EnzymePortalService enzymePortalServiceMock;
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    protected SearchUtil searchUtilMock;

    private MockMvc mockMvc;

    private final SearchController searchController = new SearchController();

    private final EnzymeCentricController enzymeCentricController = new EnzymeCentricController();


    @Before
    public void setup() {

        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(enzymePortalServiceMock);

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(searchController, enzymeCentricController);

        this.mockMvc = standaloneMockMvcBuilder.build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHomeController() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher msg = MockMvcResultMatchers.model()
                .attribute("homeVideo", "homeVideo");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.ALL);
        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(view().name("index"))
                .andExpect(ok)
                .andExpect(msg);

    }

    /**
     * Test of getSearchResults method, of class EnzymeCentricController.
     */
    @Test
    @Ignore
    public void testGetSearchResults() throws Exception {
        System.out.println("getSearchResults");

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher msg = MockMvcResultMatchers.model()
                .attribute("enzymeView", "homeVideo");

        ResultMatcher searchModel = MockMvcResultMatchers.model()
                .attribute("enzymeView", "homeVideo");

        String searchKey = "sildenafil";
        List<String> filters = new ArrayList<>();
        Integer servicePage = null;
        String keywordType = "KEYWORD";
        String searchId = "sildenafil";
     
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/enzymes")
                //.get("/enzymes")
                .accept(MediaType.ALL)
                .param("searchKey", searchKey)
                .param("keywordType", keywordType)
                .param("servicePage", Integer.toString(1)).param("filterFacet", filters.toString())
                .param("searchId", searchId);
        //.sessionAttr("searchModel", new SearchModel());
        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(view().name("enzymes"))
                //.andExpect(ok)
                .andExpect(msg);

    }
//

}
