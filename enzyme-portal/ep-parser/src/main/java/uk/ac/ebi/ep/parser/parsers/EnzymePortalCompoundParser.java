/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

/**
 *
 * @author joseph
 */
//@Transactional
@Service
public class EnzymePortalCompoundParser {

    @Autowired
    private EnzymePortalCompoundRepository compoundRepository;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;

    @Autowired
    private EnzymePortalSummaryRepository enzymeSummaryRepository;

    @Autowired
    private EnzymePortalReactionRepository reactionRepository;

    @Autowired
    private EnzymePortalEcNumbersRepository ecNumbersRepository;
    @Autowired
    private ChebiWebServiceClient chebiWebServiceClient;

    @Transactional
    public EnzymePortalCompound addCompound(EnzymePortalCompound c) {
        EnzymePortalCompound compound = compoundRepository.saveAndFlush(c);
        return compound;
    }

    @Transactional
    public List<EnzymePortalCompound> addCompound(List<EnzymePortalCompound> compounds) {
        List<EnzymePortalCompound> portalCompounds = compoundRepository.save(compounds);
        return portalCompounds;
    }

    @Transactional
    public void parseAndLoadChEMBLCompounds(String file) throws Exception {
        ChemblSaxParser parser = new ChemblSaxParser(compoundRepository, uniprotEntryRepository);
        parser.parse(file);

    }

    @Transactional
    public void parseIntenzAndLoadCompoundsAndReactions(String file) throws Exception {
        IntenzSaxParser parser = new IntenzSaxParser(compoundRepository, ecNumbersRepository, reactionRepository);
        parser.parse(file);

    }

    //@Transactional
    public void loadChEBICompounds() {

        ChEBICompounds chebi = new ChEBICompounds(enzymeSummaryRepository, compoundRepository);

        chebi.computeAndLoadChEBICompounds();

    }

    //@Transactional
    public void loadCofactors() {

        CompoundParser compoundParser = new Cofactors(chebiWebServiceClient, compoundRepository, enzymeSummaryRepository);
        compoundParser.loadCofactors();

    }

}
