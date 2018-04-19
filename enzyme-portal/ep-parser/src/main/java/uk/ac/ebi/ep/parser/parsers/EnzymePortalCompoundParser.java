package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.EnzymePortalCompound;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author joseph <joseph@ebi.ac.uk>
 */
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

    @Autowired
    private ChemblService chemblService;
    @Autowired
    private ChemblXmlParser chemblXmlParser;
    @Autowired
    private EnzymePortalParserService parserService;

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


    @Transactional
    public void loadCofactors() {

        CompoundParser compoundParser = new Cofactors(chebiWebServiceClient, compoundRepository, enzymeSummaryRepository, parserService);
        compoundParser.loadCofactors();

    }

    @Transactional
    public void loadChemblMolecules() {

        ChemblCompound chembl = new ChemblCompound(chemblService, chemblXmlParser, parserService);
        chembl.loadChEMBL();

    }

    @Transactional
    public void loadChemblFDA() {

        FDA fda = new FDA(chemblService, chemblXmlParser, parserService);
        fda.loadChEMBL();

    }

    @Transactional
    public void loadCofactorsFromFTPFiles() {

        ICompoundParser compoundParser = new CofactorsFtpFiles(parserService, enzymeSummaryRepository);
        compoundParser.loadCofactors();

    }

    @Transactional
    @Modifying
    public void loadChemblTargets() {
        ChemblCompounds chemblTargets = new ChemblCompounds(chemblService, chemblXmlParser, parserService);
        chemblTargets.loadChemblTargetsToDB();

    }

    @Transactional
    @Modifying
    public void processChemblTargetsAndUpdateDatabase() {
        ChemblCompounds chemblTargets = new ChemblCompounds(chemblService, chemblXmlParser, parserService);
        chemblTargets.loadChEMBL();

    }

}
