/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeConfig;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PDBeConfig.class})
//@PropertySource({"classpath:pdb.properties"})
public abstract class BaseTest extends TestCase {

    @Autowired
    protected PDBeUrl pDBeUrl;
    protected MockRestServiceServer mockRestServer;
    protected RestTemplate restTemplate;

    @Before
    @Override
    public void setUp() {
        restTemplate = new RestTemplate(clientHttpRequestFactory());
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServer = mockServer;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

}
