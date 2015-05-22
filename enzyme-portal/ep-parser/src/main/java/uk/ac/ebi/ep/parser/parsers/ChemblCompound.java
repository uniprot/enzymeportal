/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
        AtomicInteger count = new AtomicInteger(0);
        partitioned.parallel().forEach((chunk) -> {

            chunk.stream().forEach((targets) -> {

                Optional<UniprotEntry> entry = parserService.findByAccession(targets.getKey());
                if (entry.isPresent()) {

                    for (String targetId : targets.getValue()) {
                        chemblService.chemblSmallMolecules(targetId, entry.get(), parserService);
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
        List<EnzymePortalCompound> compounds = chemblService.getChemblCompounds();

//        List<EnzymePortalCompound> bioactive = new ArrayList<>();
//        List<EnzymePortalCompound> activator = new ArrayList<>();
//        List<EnzymePortalCompound> inhibitor = new ArrayList<>();

        //load into database
        if (compounds != null) {

            LOGGER.warn("Num compounds found " + compounds.size());
            System.out.println("Num compounds found " + compounds.size());

            for (EnzymePortalCompound compound : compounds) {
                String compoundId = compound.getCompoundId();
                String moleculeName = compound.getCompoundName();
                String compoundSource = compound.getCompoundSource();
                String relationship = compound.getRelationship();
                String compoundRole = compound.getCompoundRole();
                String url = compound.getUrl();
                String accession = compound.getUniprotAccession().getAccession();
                String note = compound.getNote();

                parserService.createCompound(compoundId, moleculeName, compoundSource, relationship, accession, url, compoundRole, note);
            }

//            compounds.stream().map((c) -> {
//                if (c.getCompoundRole().equalsIgnoreCase("BIOACTIVE")) {
//                    bioactive.add(c);
//                }
//                return c;
//            }).map((c) -> {
//                if (c.getCompoundRole().equalsIgnoreCase("INHIBITOR")) {
//                    inhibitor.add(c);
//                }
//                return c;
//            }).filter((c) -> (c.getCompoundRole().equalsIgnoreCase("ACTIVATOR"))).forEach((c) -> {
//                activator.add(c);
//            });
//
//            LOGGER.warn("BIOACTIVE " + bioactive.size() + " INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());
//
//            System.out.println("BIOACTIVE " + bioactive.size() + " INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

        }

    }
}
