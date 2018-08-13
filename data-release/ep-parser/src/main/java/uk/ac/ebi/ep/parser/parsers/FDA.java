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
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author joseph
 */
//@Service
public class FDA {

    private final Logger LOGGER = Logger.getLogger(FDA.class);

    private final ChemblService chemblService;

    private final ChemblXmlParser chemblXmlParser;

    private final EnzymePortalParserService parserService;

    public FDA(ChemblService chemblService, ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
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
        LOGGER.warn("FDA-finished parsing chembl target file : " + chemblTargets.size());
////

        for (Map.Entry<String, List<String>> targets : chemblTargets.entrySet()) {
                String protein = targets.getKey();
                targets.getValue()
                        .stream()
                        .parallel()
                        .forEach((targetId) ->  chemblService.getMoleculesByCuratedMechanism(targetId, protein));

        }

        List<TempCompoundCompare> compounds = chemblService.getFdaChemblCompounds().stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());



        //load into database
        if (compounds != null) {

            LOGGER.warn("Num FDA compounds found " + compounds.size());
            System.out.println("Num FDA-compounds found " + compounds.size());

            LOGGER.warn("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB

            compounds.stream()
                    .filter((compound) -> (compound != null))
                    .forEach((compound) -> {
                 parserService.addTempCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());

            });

            LOGGER.warn("Finished loading temporal compound table ::::::  ");
            LOGGER.warn("*******************Updating compound table ignoring duplicates ************");

            parserService.insertCompoundsFromTempTable();
            LOGGER.warn("**********DONE*************** ");

            //delete LATER
//                    List<TempCompoundCompare> activator = new ArrayList<>();
//        List<TempCompoundCompare> inhibitor = new ArrayList<>();
//            compounds.stream().map((c) -> {
//                if (c.getCompoundRole().equalsIgnoreCase("INHIBITOR")) {
//                    inhibitor.add(c);
//                }
//                return c;
//            }).filter((c) -> (c.getCompoundRole().equalsIgnoreCase("ACTIVATOR"))).forEach((c) -> {
//                activator.add(c);
//            });
//
//            LOGGER.warn(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());
//
//            System.out.println(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());

        }

    }
}
