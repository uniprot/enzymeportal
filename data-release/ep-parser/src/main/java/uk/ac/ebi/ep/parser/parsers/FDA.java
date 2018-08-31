/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Slf4j
public class FDA extends SmallMolecules {

    private final ChemblService chemblService;

    // private final ChemblXmlParser chemblXmlParser;
    private final EnzymePortalParserService parserService;

    public FDA(EnzymePortalParserService parserService, ChemblService chemblService) {
        super(parserService);
        this.chemblService = chemblService;
        this.parserService = parserService;
    }

//    public FDA(ChemblService chemblService, ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
//        this.chemblService = chemblService;
//        this.chemblXmlParser = chemblXmlParser;
//        this.parserService = parserService;
//    }
//    private List<String> findUniqueTargetedproteins() {
//
//        return parserService.findUniqueTargetedproteins();
//    }
//
//    private List<String> findProteinTargetets(String accession) {
//
//        return parserService.findTargetetsByProtein(accession);
//    }
    @Override
    public void loadChEMBL() {

//        Map<String, List<String>> chemblTargets = new HashMap<>();
//        try {
//            chemblTargets = chemblXmlParser.parseChemblTarget();
//        } catch (FileNotFoundException ex) {
//            log.error("File cannot be found", ex);
//        }
//        log.warn("FDA-finished parsing chembl target file : " + chemblTargets.size());
        List<String> uniqueTargetedproteins = findUniqueTargetedproteins();
        //.stream().limit(2).collect(Collectors.toList());

        log.warn(" Number of unique targeted proteins found for FDA : " + uniqueTargetedproteins.size());

        uniqueTargetedproteins
                .forEach(protein -> chemblService.getMoleculesByCuratedMechanism(findProteinTargetets(protein), protein));

        loadToDatabase();

    }

    private void loadToDatabase() {
        List<TempCompoundCompare> compounds = chemblService.getFdaChemblCompounds().stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());

        //load into database
        if (compounds != null) {

            log.warn("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB

            compounds.stream()
                    .filter(compound -> compound != null)
                    .forEach(compound -> parserService.addTempCompound(compound.getPrimaryTargetId(), compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote()));

            log.warn("Finished loading temporal compound table ::::::  ");
            log.warn("*******************Updating compound table ignoring duplicates ************");

            parserService.insertCompoundsFromTempTable();
            log.warn("**********DONE*************** ");

        }
    }

//    public void loadChEMBL() {
//
//        Map<String, List<String>> chemblTargets = new HashMap<>();
//        try {
//            chemblTargets = chemblXmlParser.parseChemblTarget();
//        } catch (FileNotFoundException ex) {
//            log.error("File cannot be found", ex);
//        }
//        log.warn("FDA-finished parsing chembl target file : " + chemblTargets.size());
//////
//
//        for (Map.Entry<String, List<String>> targets : chemblTargets.entrySet()) {
//            String protein = targets.getKey();
//            targets.getValue()
//                    .stream()
//                    .parallel()
//                    .forEach((targetId) -> chemblService.getMoleculesByCuratedMechanism(targetId, protein));
//
//        }
//
//        List<TempCompoundCompare> compounds = chemblService.getFdaChemblCompounds().stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
//
//        //load into database
//        if (compounds != null) {
//
//            log.warn("Num FDA compounds found " + compounds.size());
//            System.out.println("Num FDA-compounds found " + compounds.size());
//
//            log.warn("About to load the temporal compounds found ::::::  " + compounds.size());
//            //UPDATE DB
//
//            compounds.stream()
//                    .filter((compound) -> (compound != null))
//                    .forEach((compound) -> {
//                        parserService.addTempCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
//
//                    });
//
//            log.warn("Finished loading temporal compound table ::::::  ");
//            log.warn("*******************Updating compound table ignoring duplicates ************");
//
//            parserService.insertCompoundsFromTempTable();
//            log.warn("**********DONE*************** ");
//
//            //delete LATER
////                    List<TempCompoundCompare> activator = new ArrayList<>();
////        List<TempCompoundCompare> inhibitor = new ArrayList<>();
////            compounds.stream().map((c) -> {
////                if (c.getCompoundRole().equalsIgnoreCase("INHIBITOR")) {
////                    inhibitor.add(c);
////                }
////                return c;
////            }).filter((c) -> (c.getCompoundRole().equalsIgnoreCase("ACTIVATOR"))).forEach((c) -> {
////                activator.add(c);
////            });
////
////            log.warn(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());
////
////            System.out.println(" INHIBITORS " + inhibitor.size() + " ACTIVATORS " + activator.size());
//        }
//
//    }
}
