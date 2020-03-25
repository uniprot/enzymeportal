package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;
import uk.ac.ebi.ep.dataservice.entities.QUniprotEntry;

/**
 *
 * @author joseph
 */
public class UniprotEntryRepositoryImpl implements UniprotEntryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QUniprotEntry $ = QUniprotEntry.uniprotEntry;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ProteinData findProteinByAccession(String accession) {

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query
                .select(Projections.constructor(ProteinData.class, $.accession, $.proteinName, $.scientificName, $.commonName, $.function, $.synonymNames, $.taxId, $.sequenceLength, $.entryType, $.functionLength, $.expEvidenceFlag, $.relatedProteinsId.relProtInternalId,$.proteinGroupId))
                .from($)
                .where($.accession.eq(accession))
                .fetchOne();

    }

}
