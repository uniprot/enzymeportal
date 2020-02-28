package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalChebiCompound;

/**
 *
 * @author joseph
 */
public interface EnzymePortalChebiCompoundRepository extends JpaRepository<EnzymePortalChebiCompound, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert INTO UNIQUE_CHEBI_COMPOUND "
            + "(COMPOUND_ID,COMPOUND_NAME,SYNONYMS,RELATIONSHIP,URL,COMPOUND_ROLE,NOTE) VALUES (?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    void createUniqueChebiCompound(String compoundId, String compoundName, String synonyms, String relationship, String url, String compoundRole, String note);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_CHEBI_COMPOUND,EZP_CHEBI_COMPOUND_IDX) */ INTO ENZYME_PORTAL_CHEBI_COMPOUND "
            + "(COMPOUND_ID,COMPOUND_NAME,SYNONYMS,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) select uc.compound_id, uc.compound_name, uc.synonyms, uc.relationship, ri.uniprot_accession, uc.url, uc.compound_role, uc.note\n"
            + "from enzyme_reaction_info ri, unique_chebi_compound uc where ri.xref=uc.compound_id and ri.xref_type='CHEBI'", nativeQuery = true)
    void createChebiCompoundIgnoreDup();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_CHEBI_COMPOUND,EZP_CHEBI_COMPOUND_IDX) */ INTO ENZYME_PORTAL_CHEBI_COMPOUND "
            + "(COMPOUND_INTERNAL_ID,COMPOUND_ID,COMPOUND_NAME,SYNONYMS,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) VALUES (SEQ_EZP_CHEBI_COMP.NEXTVAL,?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void createChebiCompoundIgnoreDup(String compoundId, String compoundName, String synonyms, String relationship, String accession, String url, String compoundRole, String note);

    @Query(value = "SELECT count( compound_id) from unique_chebi_compound", nativeQuery = true)
    long countUniqueChebiIds();

}
