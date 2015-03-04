/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public abstract class AbstractEbeyeTest extends TestCase {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractEbeyeTest.class);
    @Autowired
    protected EbeyeIndexUrl ebeyeIndexUrl;
    protected MockRestServiceServer mockRestServer;
    protected RestTemplate restTemplate;
    protected AsyncRestTemplate asyncRestTemplate;

    @Before
    @Override
    public void setUp() {
        restTemplate = new RestTemplate(clientHttpRequestFactory());
        asyncRestTemplate = new AsyncRestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServer = mockServer;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }


        protected String getJsonFile(String filename) throws IOException {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream(filename);

        return IOUtils.toString(in);

       
    }

    protected String getValueFromJsonData(String jsonData, String nodeName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(jsonData);
        return nodes.findValue(nodeName).textValue();


    }

}
