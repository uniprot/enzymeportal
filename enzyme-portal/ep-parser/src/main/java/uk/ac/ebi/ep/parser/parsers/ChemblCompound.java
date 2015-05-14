/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;
import static uk.ac.ebi.ep.parser.inbatch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author joseph
 */
//@Service
public class ChemblCompound {
 private  final Logger LOGGER = Logger.getLogger(ChemblCompound.class);
    //@Autowired
    private final ChemblService chemblService;
    //@Autowired
    private final ChemblXmlParser chemblXmlParser;
   // @Autowired
    private final EnzymePortalParserService parserService;
//       @Autowired
//    private ChemblServiceUrl chemblServiceUrl;
    
    //FDA  count : 3611
//Num compounds found 442
//BIOACTIVE 0 INHIBITORS 430 ACTIVATORS 12
    //Total time: 3:11.480s

    public ChemblCompound(ChemblService chemblService, ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
        this.chemblService = chemblService;
        this.chemblXmlParser = chemblXmlParser;
        this.parserService = parserService;
    }
    
    
    

    public void loadChEMBL(String targetXml) {

        Map<String, List<String>> chemblTargets = chemblXmlParser.parseChemblTarget(targetXml);
        
        LOGGER.warn("finished parsing chembl target file : " + chemblTargets.size());

        System.out.println("finished parsing file " + chemblTargets.size());
     
        Stream<Entry<String, List<String>>> existingStream = chemblTargets.entrySet().stream();

        Stream<List<Entry<String, List<String>>>> partitioned = partition(existingStream, 1000, 1);
        AtomicInteger count = new AtomicInteger(0);
        partitioned.parallel().forEach((chunk) -> {

            chunk.stream().forEach((targets) -> {

                Optional<UniprotEntry> entry = parserService.findByAccession(targets.getKey());
                if (entry.isPresent()) {
     
                    for (String targetId : targets.getValue()) {
                        chemblService.chemblSmallMolecules(targetId, entry.get());
                        //chemblService.getMoleculesByCuratedMechanism(targetId, entry.get());
                        //System.out.println("count : " + count.getAndIncrement());
                         LOGGER.warn("counter : " + count.getAndIncrement());
                    }
                }
            });
        });
        
        //sequencial processing .....

//        AtomicInteger count = new AtomicInteger(0);
//
//        for (Map.Entry<String, List<String>> targets : chemblTargets.entrySet()) {
//
//            //System.out.println(map.getKey() + " --> "+ map.getValue());
//            Optional<UniprotEntry> entry = parserService.findByAccession(targets.getKey());
//            if (entry.isPresent()) {
//                System.out.println("protein " + entry.get().getAccession() + " targets " + targets.getValue().size());
//                for (String targetId : targets.getValue()) {
//                    chemblService.chemblSmallMolecules(targetId, entry.get());
//                    System.out.println("count : " + count.getAndIncrement());
//                }
//            }
//
//        }
   
        Set<EnzymePortalCompound> compounds = chemblService.getChemblCompounds();

        List<EnzymePortalCompound> bioactive = new ArrayList<>();
        List<EnzymePortalCompound> activator = new ArrayList<>();
        List<EnzymePortalCompound> inhibitor = new ArrayList<>();

        //load into database
        if (compounds != null) {
            
            LOGGER.warn("Num compounds found " + compounds.size());
            System.out.println("Num compounds found " + compounds.size());
    
            compounds.stream().map((c) -> {
                if (c.getCompoundRole().equalsIgnoreCase("BIOACTIVE")) {
                    bioactive.add(c);
                }
                return c;
            }).map((c) -> {
                if (c.getCompoundRole().equalsIgnoreCase("INHIBITOR")) {
                    inhibitor.add(c);
                }
                return c;
            }).filter((c) -> (c.getCompoundRole().equalsIgnoreCase("ACTIVATOR"))).forEach((c) -> {
                activator.add(c);
            });
            
             LOGGER.warn("BIOACTIVE " + bioactive.size() + " INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

            System.out.println("BIOACTIVE " + bioactive.size() + " INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

            //System.out.println("activators found " + activator);
        }

    }
}
