package uk.ac.ebi.ep.reaction.mechanism.model;

import uk.ac.ebi.ep.reaction.mechanism.model.Result;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.reaction.mechanism.config.McsaConfig;

/**
 *
 * @author joseph
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {McsaConfig.class})
public class ResultTest {

    private Result getResult() {
        Result result = new Result();
        result.setUrl("https://www.ebi.ac.uk/thornton-srv/m-csa/entry/255/");
        result.setMcsaId(255);
        result.setReference(false);
        result.setEnzymeName("Alcohol dehydrogenase (SDR type)");
        result.setDescription("An NAD+-dependent enzyme that catalyzes the oxidation of alcohols to aldehydes/ketones");
        result.setAllEcs(Arrays.asList("1.1.1.1"));
        return result;
    }

    /**
     * Test of getMcsaId method, of class Result.
     */
    @Test
    public void testGetMcsaId() {
        Result result = getResult();
        assertNotNull(result.getMcsaId());

    }

    /**
     * Test of getEnzymeName method, of class Result.
     */
    @Test
    public void testGetEnzymeName() {
        Result result = getResult();
        assertNotNull(result.getEnzymeName());
    }

    /**
     * Test of getUrl method, of class Result.
     */
    @Test
    public void testGetUrl() {
        Result result = getResult();
        assertNotNull(result.getUrl());
    }

    /**
     * Test of getDescription method, of class Result.
     */
    @Test
    public void testGetDescription() {
        Result result = getResult();
        assertNotNull(result.getDescription());
    }

    /**
     * Test of getAllEcs method, of class Result.
     */
    @Test
    public void testGetAllEcs() {
        Result result = getResult();
        assertNotNull(result.getAllEcs());
        assertThat(result.getAllEcs(), hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of isReference method, of class Result.
     */
    @Test
    public void testIsReference() {
        Result result = getResult();
        assertFalse(result.isReference());
    }

}
