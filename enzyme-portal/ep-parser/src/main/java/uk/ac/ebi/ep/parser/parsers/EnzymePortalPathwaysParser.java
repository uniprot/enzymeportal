/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.main.PathwaysParser;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalPathwaysParser {

    //@Autowired
   // private EnzymePortalPathwaysRepository pathwaysRepository;
    @Autowired
    private EnzymePortalParserService parserService;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;
    private static final String REACTOME_FILE = "http://www.reactome.org/download/current/UniProt2Reactome.txt";
    private static final Logger LOGGER
            = Logger.getLogger(PathwaysParser.class);

    /**
     * parse the Reactome file. (note: if file is missing,the default Reactome
     * url will be used)
     *
     * @param file the reactome file
     */
    public void parseReactomeFile(String file) {

        try {
            File downloadedFile = new File(file);

            if (downloadedFile.exists()) {
                parseDownloadedFile(file);
            } else {
                parseFromWeblink(REACTOME_FILE);
            }
        } catch (IOException ex) {
            LOGGER.fatal("IOException while parsing reactome file : ", ex);
        }
    }

    private void parseDownloadedFile(String file) throws FileNotFoundException, IOException {

        try ( /**
                 * Creating a buffered reader to read the file
                 */
                BufferedReader bReader = new BufferedReader(new FileReader(file))) {
            String line;

            /**
             * Looping the read block until all lines in the file are read.
             */
            while ((line = bReader.readLine()) != null) {

                /**
                 * Splitting the content of tabbed separated line
                 */
                String fields[] = line.split("\t");
                loadToDB(fields);

            }

        }

    }

    private void loadToDB(String[] fields) {
        if (fields.length >= 4) {
            String accession = fields[0];
            String pathwayId = fields[1];
            String pathwayUrl = fields[2];
            String pathwayName = fields[3];
            String status = fields[4];
            String species = fields[5];

            //check if accession is an enzyme
            UniprotEntry enzyme = uniprotEntryRepository.findByAccession(accession);

            if (enzyme != null) {

//                EnzymePortalPathways pathways = new EnzymePortalPathways();
//                pathways.setUniprotAccession(enzyme);
//                pathways.setPathwayId(pathwayId);
//                pathways.setPathwayName(pathwayName);
//                pathways.setPathwayUrl(pathwayUrl);
//                pathways.setSpecies(species);
//                pathways.setStatus(status);
//                pathwaysRepository.saveAndFlush(pathways);
                
                parserService.createPathway(accession, pathwayId, pathwayUrl, pathwayName, status, species);
                
               
                
                
                
                

                /**
                 * Printing the value read from file to the console
                 */
//        System.out.println(accession + "\t" + pathwayId + "\t" + pathwayUrl + "\t"
//                + pathwayName + "\t" + status + "\t" + species);
            }
        }

    }

    private void parseFromWeblink(String file) throws IOException {
        // Check the extension of the file:

        BufferedReader br = null;
        InputStreamReader isr = null;
        InputStream is = null;
        try {

            is = file.startsWith("http://")
                    ? new URL(file).openStream()
                    : new FileInputStream(file);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            LOGGER.info("Parsing start");

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields == null) {
                    continue; // header lines
                }
                loadToDB(fields);

            }
            LOGGER.info("Parsing end");

        } catch (IOException e) {
            LOGGER.error("During parsing", e);

            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

}
