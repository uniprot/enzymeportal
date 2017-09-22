/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import static uk.ac.ebi.ep.parser.inbatch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author joseph
 */
public class ChemblCompound {

    private final Logger LOGGER = Logger.getLogger(ChemblCompound.class);

    private final ChemblService chemblService;

    private final ChemblXmlParser chemblXmlParser;

    private final EnzymePortalParserService parserService;

    public ChemblCompound(ChemblService chemblService, ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
        this.chemblService = chemblService;
        this.chemblXmlParser = chemblXmlParser;
        this.parserService = parserService;
    }

    public void loadChEMBL() {

        Map<String, List<String>> chemblTargets = new HashMap<>();
        try {
            chemblTargets = chemblXmlParser.parseChemblTarget();
        } catch (FileNotFoundException ex) {
            LOGGER.error("File cannot be found", ex);
        }

        LOGGER.warn("finished parsing chembl target file : " + chemblTargets.size());

        Stream<Entry<String, List<String>>> existingStream = chemblTargets.entrySet().stream();

        Stream<List<Entry<String, List<String>>>> partitioned = partition(existingStream, 1024, 1);
        AtomicInteger count = new AtomicInteger(1);
        partitioned.parallel().forEach((chunk) -> {

            chunk.stream().forEach((targets) -> {

                String protein = targets.getKey();

                for (String targetId : targets.getValue()) {
                    LOGGER.info("counter : " + count.getAndIncrement() + " protein : " + protein + " target id : " + targetId);
                    chemblService.chemblSmallMolecules(targetId, protein);

                }

            });
        });

        //sequencial processing .....
//        AtomicInteger count = new AtomicInteger(0);
//
//        for (Map.Entry<String, List<String>> targets : chemblTargets.entrySet()) {
//
//            //System.out.println(map.getKey() + " --> "+ map.getValue());
//            //Optional<UniprotEntry> entry = parserService.findByAccession(targets.getKey());
//            UniprotEntry entry = new UniprotEntry(targets.getKey());
//            //if (entry.isPresent()) {
//
//            for (String targetId : targets.getValue()) {
//                chemblService.chemblSmallMolecules(targetId, entry);
//                LOGGER.warn("counter : " + count.getAndIncrement() + " protein : " + targets.getKey() + " target id : " + targetId);
//            }
//            //}
//
//        }
        List<TempCompoundCompare> compounds = chemblService.getChemblCompounds().stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());
    
        //load into database
        if (compounds != null) {

            System.out.println("Num compounds found " + compounds.size());

            LOGGER.warn("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB
            compounds.stream().filter((compound) -> (compound != null)).forEach((compound) -> {
                 parserService.addTempCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
            });


            LOGGER.warn("Finished loading temporal compound table ::::::  ");
            LOGGER.warn("*******************Updating compound table ignoring duplicates ************");
            parserService.insertCompoundsFromTempTable();
            LOGGER.warn("**********DONE*************** ");

            //DELETE LATER AND all system.out.println()
            List<TempCompoundCompare> activator = new ArrayList<>();
            List<TempCompoundCompare> inhibitor = new ArrayList<>();

            compounds.stream().map((c) -> {
                if (c.getCompoundRole().equalsIgnoreCase("INHIBITOR")) {
                    inhibitor.add(c);
                }
                return c;
            }).filter((c) -> (c.getCompoundRole().equalsIgnoreCase("ACTIVATOR"))).forEach((c) -> {
                activator.add(c);
            });

            LOGGER.warn(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

            System.out.println(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

        }

    }
}
