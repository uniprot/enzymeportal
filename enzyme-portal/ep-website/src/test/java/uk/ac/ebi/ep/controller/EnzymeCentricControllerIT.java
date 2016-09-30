/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import uk.ac.ebi.ep.common.Config;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(classes = {WebTestConfig.class, ApplicationContextMock.class})
@WebAppConfiguration
public class EnzymeCentricControllerIT {// extends BaseTest {

    @Autowired
    protected EnzymePortalService enzymePortalServiceMock;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private Config searchConfig;
    @Autowired
    private EbeyeRestService ebeyeRestService;
    @Autowired
    private EnzymeCentricService enzymeCentricService;

    private MockMvc mockMvc;

    private final SearchController searchController = new SearchController();//(searchConfig, enzymePortalServiceMock, ebeyeRestService);

    private final EnzymeCentricController enzymeCentricController = new EnzymeCentricController();//(ebeyeRestService, enzymeCentricService);

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setup() {

        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(enzymePortalServiceMock);

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(searchController, enzymeCentricController);
        // this.mockMvc = builder.build();
        //this.mockMvc = standaloneSetup(new SearchController(searchConfig, enzymePortalServiceMock, ebeyeRestService))
        // .build();
        this.mockMvc = standaloneMockMvcBuilder.build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMyMvcController() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher msg = MockMvcResultMatchers.model()
                .attribute("homeVideo", "homeVideo");

//        
//          public String postSearchResult(@RequestParam(required = false, value = "searchKey") String searchKey,
//            @RequestParam(required = false, value = "filterFacet") List<String> filters,
//            @RequestParam(required = false, value = "servicePage") Integer servicePage,
//            @RequestParam(required = false, value = "keywordType") String keywordType,
//            @RequestParam(required = false, value = "searchId") String searchId,
//    
//         ResultActions resultActions = mockMvc.perform(get("/").accept(MediaType.ALL));
//
//    resultActions.andExpect(status().is4xxClientError());
//         resultActions.andExpect(msg);
//
//    resultActions.andExpect(view().name("index"));
//
//
//         System.out.println("REQUESTBUILDER "+ resultActions);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.ALL);
        this.mockMvc.perform(builder)
                // mockMvc.perform(get("/").accept(MediaType.ALL))
                .andDo(print())
                .andExpect(view().name("index"))
                .andExpect(ok)
                .andExpect(msg);

    }

    /**
     * Test of getSearchResults method, of class EnzymeCentricController.
     */
    //@Test
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
//        SearchModel searchModel = null;
//        BindingResult result_2 = null;
//        Model model = null;
//        HttpSession session = null;
//        HttpServletRequest request = null;
//        HttpServletResponse response = null;
//        EnzymeCentricController instance = new EnzymeCentricController();
//        String expResult = "";
//        String result = instance.getSearchResults(searchKey, filters, servicePage, keywordType, searchId, searchModel, result_2, model, session, request, response);
//        
//        
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
//    /**
//     * Test of postSearchResult method, of class EnzymeCentricController.
//     */
//    @Test
//    public void testPostSearchResult() {
//        System.out.println("postSearchResult");
//        String searchKey = "";
//        List<String> filters = null;
//        Integer servicePage = null;
//        String keywordType = "";
//        String searchId = "";
//        SearchModel searchModel = null;
//        Model model = null;
//        HttpServletRequest request = null;
//        EnzymeCentricController instance = new EnzymeCentricController();
//        String expResult = "";
//        String result = instance.postSearchResult(searchKey, filters, servicePage, keywordType, searchId, searchModel, model, request);
//
//    }
}
