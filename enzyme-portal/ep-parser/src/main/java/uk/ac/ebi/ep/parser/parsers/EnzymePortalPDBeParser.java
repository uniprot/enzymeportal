/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.UniprotXref;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import static uk.ac.ebi.ep.parser.inbatch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.pdbeadapter.PdbService;
import uk.ac.ebi.ep.pdbeadapter.summary.PDBe;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalPDBeParser {

    private static final Logger LOGGER = Logger.getLogger(EnzymePortalPDBeParser.class);
    @Autowired
    private EnzymePortalParserService parserService;

    @Autowired
    private PdbService pdbService;

    public void updatePDBeData() {

        Set<UniprotXref> entries = new HashSet<>();

        List<UniprotXref> pdbIds = parserService.findPDBcodes();
        LOGGER.info("Number of PDB entries to process : "+ pdbIds.size());
        
        //int est = pdbIds.size()/500;
       

        Stream<UniprotXref> existingStream = pdbIds.stream();
        Stream<List<UniprotXref>> partitioned = partition(existingStream, 500, 1);
        AtomicInteger count = new AtomicInteger(1);
        partitioned.parallel().forEach((chunk) -> {

            chunk.stream().forEach((pdb) -> {
                //obtain a concrete pdb entry
                PdbSearchResult results = pdbService.getPdbSearchResults(pdb.getSourceId().toLowerCase());

                if (results != null) {
                    List<PDBe> result = results.get(pdb.getSourceId().toLowerCase());
                    String title = result.stream().findAny().get().getTitle();
                    pdb.setSourceName(title);
                    entries.add(pdb);
                }
            });
        });
        


//        AtomicInteger count = new AtomicInteger(1);
//        pdbIds.stream().forEach((pdb) -> {
//            //obtain a concrete pdb entry
//            PdbSearchResult results = pdbService.getPdbSearchResults(pdb.getSourceId().toLowerCase());
//           // System.out.println("count "+count.getAndIncrement() +" PDB id "+ pdb.getSourceId());
//            if (results != null) {
//                List<PDBe> result = results.get(pdb.getSourceId().toLowerCase());
//                String title = result.stream().findAny().get().getTitle();
//                pdb.setSourceName(title);
//                entries.add(pdb);
//            }
//        });
        
        
        //update entry
        List<UniprotXref> pdbEntries = entries.stream().collect(Collectors.toList());
        
         List<UniprotXref> updatedEntries = parserService.updatePDB(pdbEntries);
        LOGGER.info("Number of PDB entries updated are : " + updatedEntries.size());
        updatedEntries.clear();
    }
}
