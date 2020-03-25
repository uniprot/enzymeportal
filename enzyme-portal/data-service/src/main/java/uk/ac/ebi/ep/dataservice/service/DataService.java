package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;

/**
 *
 * @author joseph
 */
public interface DataService {

    ProteinData findProteinByAccession(String accession);

    ProteinView findProteinViewByAccession(String accession);

    List<ProteinView> findProteinViewByRelatedProteinId(Long relId);

    List<ProteinView> findProteinViewByRelatedProteinIdAndProteinGroupId(Long relId, String proteinGroupId);

    List<String> findEcNumbersByAccession(String accession);

    List<PdbView> findPdbViewsByAccession(String accession);

    List<String> findPathwayIdsByAccession(String accession);

    List<DiseaseView> findDiseaseViewByAccession(String accession);

    List<String> findCatalyticActivitiesByAccession(String accession);

    List<EnzymeReactionView> findReactionsByAccession(String accession);

    List<CompoundView> findCompoundsByAccession(String accession);

    List<String> findChemblTargetIdByAccession(String accession);

}
