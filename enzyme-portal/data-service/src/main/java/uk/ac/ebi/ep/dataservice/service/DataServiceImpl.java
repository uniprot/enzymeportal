package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.repositories.DiseaseRepository;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.dataservice.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.dataservice.repositories.UniprotXrefRepository;

/**
 *
 * @author joseph
 */
@Service
class DataServiceImpl implements DataService {

    private final UniprotEntryRepository uniprotEntryRepository;
    private final EnzymePortalEcNumbersRepository ecNumbersRepository;
    private final UniprotXrefRepository xrefRepository;
    private final EnzymePortalPathwaysRepository pathwaysRepository;
    private final DiseaseRepository diseaseRepository;
    private final EnzymePortalReactionRepository reactionRepository;
    private final EnzymePortalCompoundRepository compoundRepository;

    @Autowired
    public DataServiceImpl(UniprotEntryRepository uniprotEntryRepository, EnzymePortalEcNumbersRepository ecNumbersRepository, UniprotXrefRepository xrefRepository, EnzymePortalPathwaysRepository pathwaysRepository, DiseaseRepository diseaseRepository, EnzymePortalReactionRepository reactionRepository, EnzymePortalCompoundRepository compoundRepository) {
        this.uniprotEntryRepository = uniprotEntryRepository;
        this.ecNumbersRepository = ecNumbersRepository;
        this.xrefRepository = xrefRepository;
        this.pathwaysRepository = pathwaysRepository;
        this.diseaseRepository = diseaseRepository;
        this.reactionRepository = reactionRepository;
        this.compoundRepository = compoundRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ProteinView findProteinViewByAccession(String accession) {
        return uniprotEntryRepository.findProteinViewByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProteinView> findProteinViewByRelatedProteinId(Long relId) {
        return uniprotEntryRepository.findProteinViewByRelatedProteinsId(relId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findEcNumbersByAccession(String accession) {
        return ecNumbersRepository.findEcNumbersByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PdbView> findPdbViewsByAccession(String accession) {
        return xrefRepository.findPdbViewsByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findPathwayIdsByAccession(String accession) {
        return pathwaysRepository.findPathwayGroupIdsByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DiseaseView> findDiseaseViewByAccession(String accession) {
        return diseaseRepository.findDiseasesViewByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findCatalyticActivitiesByAccession(String accession) {
        return uniprotEntryRepository.findCatalyticActivitiesByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnzymeReactionView> findReactionsByAccession(String accession) {
        return reactionRepository.findReactionsByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompoundView> findCompoundsByAccession(String accession) {
        return compoundRepository.findCompoundsByAccession(accession);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findChemblTargetIdByAccession(String accession) {

        return compoundRepository.findChemblTargetIdByAccession(accession);

    }

}
