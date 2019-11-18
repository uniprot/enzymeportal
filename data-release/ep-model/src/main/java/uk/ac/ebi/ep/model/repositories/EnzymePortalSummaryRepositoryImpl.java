package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import uk.ac.ebi.ep.model.dao.Summary;

/**
 *
 * @author joseph
 */
public class EnzymePortalSummaryRepositoryImpl implements EnzymePortalSummaryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Summary> findSummariesByCommentType(String commentType) {

        String nativeQuery = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE";
        Query query = entityManager.createNativeQuery(nativeQuery, "findCommentTextAndAccession");
        List<Summary> result = query.setParameter("COMMENT_TYPE", commentType).getResultList();

        return result;
    }

    @Override
    public Stream<Summary> streamSummariesByCommentType(String commentType) {

        String nativeQuery = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE";
        Query query = entityManager.createNativeQuery(nativeQuery, "findCommentTextAndAccession");
        return query.setParameter("COMMENT_TYPE", commentType).getResultStream();

    }

}
