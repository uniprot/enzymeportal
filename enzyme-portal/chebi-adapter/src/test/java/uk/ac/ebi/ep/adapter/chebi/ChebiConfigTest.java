package uk.ac.ebi.ep.adapter.chebi;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ChebiConfigTest {

    @Test
    public void testReadFromFile() throws IOException {
        ChebiConfig config = ChebiConfig.readFromFile();
        assertEquals(8, config.getMaxThreads());
        assertEquals(5, config.getMaxRetrievedMolecules());
        assertEquals("THREE_ONLY", config.getSearchStars());
        assertEquals(15000, config.getTimeout());
        assertEquals("http://www.ebi.ac.uk/chebi/foo/bar/baz?",
                config.getCompoundBaseUrl());
        assertEquals("http://www.ebi.ac.uk/chebi/img/",
                config.getCompoundImgBaseUrl());
    }

    @Test
    public void testReadFromFileString() throws IOException {
        ChebiConfig config;
        
        config = ChebiConfig.readFromFile("chebiConfigTest-01.properties");
        assertEquals(10, config.getMaxThreads());
        assertEquals(3, config.getMaxRetrievedMolecules());
        assertEquals("ALL", config.getSearchStars());
        assertEquals(30000, config.getTimeout());
        assertEquals("http://www.ebi.ac.uk/chebi/searchId.do?chebiId=",
                config.getCompoundBaseUrl());
        assertEquals("http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=",
                config.getCompoundImgBaseUrl());
        
        config = ChebiConfig.readFromFile("chebiConfigTest-02.properties");
        assertEquals(5, config.getMaxThreads());
        assertEquals(7, config.getMaxRetrievedMolecules());
        assertEquals("TWO_ONLY", config.getSearchStars());
        assertEquals(60000, config.getTimeout());
        assertEquals("http://a.b.c/", config.getCompoundBaseUrl());
        assertEquals("http://d.e.f/g/", config.getCompoundImgBaseUrl());
    }

}
