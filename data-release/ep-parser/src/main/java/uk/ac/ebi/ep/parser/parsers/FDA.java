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

    private final EnzymePortalParserService parserService;

    public FDA(EnzymePortalParserService parserService, ChemblService chemblService) {
        super(parserService);
        this.chemblService = chemblService;
        this.parserService = parserService;
    }

    @Override
    public void loadChEMBL() {

        List<String> uniqueTargetedproteins = findUniqueTargetedproteins();

        log.info(" Number of unique targeted proteins found for FDA : " + uniqueTargetedproteins.size());

       
        uniqueTargetedproteins
                .forEach(protein -> chemblService.getMoleculesByCuratedMechanism(findProteinTargetets(protein), protein));

        List<TempCompoundCompare> compounds = chemblService.getFdaChemblCompounds()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        
        loadToDatabase(compounds);

    }

    private void loadToDatabase(List<TempCompoundCompare> compounds) {

        //load into database
        if (compounds != null) {

            log.info("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB

            compounds.stream()
                    .filter(Objects::nonNull)
                    .forEach(compound -> parserService.addTempCompound(compound.getPrimaryTargetId(), compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote()));

            log.info("Finished loading temporal compound table ::::::  ");
            log.info("*******************Updating compound table ignoring duplicates ************");

            parserService.insertCompoundsFromTempTable();
            log.info("**********DONE*************** ");

        }
    }

}
