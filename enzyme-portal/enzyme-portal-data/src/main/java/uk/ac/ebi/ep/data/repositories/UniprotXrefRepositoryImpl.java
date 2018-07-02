
package uk.ac.ebi.ep.data.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QUniprotXref;
import uk.ac.ebi.ep.data.domain.UniprotXref;

/**
 *
 * @author joseph
 */
public class UniprotXrefRepositoryImpl implements UniprotXrefRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QUniprotXref $ = QUniprotXref.uniprotXref;
    private static final String PDB = "PDB";

//    @Override
//    public List<UniprotXref> findPDBcodesByAccession(String accession) {
//        // JPAQuery query = new JPAQuery(entityManager);
//  JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        BooleanExpression isAccession = $.accession.accession.equalsIgnoreCase(accession);
//        return jpaQueryFactory
//                .selectDistinct($).from($)
//                .where($.source.equalsIgnoreCase(PDB).and($.sourceName.isNotNull().or($.sourceName.isNotEmpty())).and(isAccession))
//                .fetch();
//                //.list($);
//        //List<UniprotXref> pdb = query.from($).where($.source.equalsIgnoreCase(PDB).and(isAccession)).list($);
//
//        // return pdb.stream().distinct().collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findPdbCodesWithNoNames() {
        //JPAQuery query = new JPAQuery(entityManager);
          JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .selectDistinct($.sourceId)
                .from($)
                .where($.source.equalsIgnoreCase(PDB))
                .fetch();

    }

    @Transactional(readOnly = true)
    @Override
    public UniprotXref findPdbById(String pdbId) {
        //JPAQuery query = new JPAQuery(entityManager);
  JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .selectDistinct($)
                .from($)
                .where($.source.equalsIgnoreCase(PDB).and($.sourceId.equalsIgnoreCase(pdbId)))
                .fetchOne();

    }

    @Override
    public List<UniprotXref> findPDBcodes() {
        //JPAQuery query = new JPAQuery(entityManager);
          JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .selectDistinct($)
                .from($)
                .where($.source.equalsIgnoreCase(PDB))
                .fetch();

    }

}
