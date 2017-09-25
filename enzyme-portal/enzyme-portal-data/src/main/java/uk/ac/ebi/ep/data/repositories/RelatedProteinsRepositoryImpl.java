/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QRelatedProteins;
import uk.ac.ebi.ep.data.domain.RelatedProteins;

/**
 *
 * @author joseph
 */
public class RelatedProteinsRepositoryImpl implements RelatedProteinsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QRelatedProteins $ = QRelatedProteins.relatedProteins;

    @Transactional(readOnly = true)
    @Override
    public List<RelatedProteins> findRelatedProteinsByNamePrefixes(List<String> nameprefixes) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

       // JPAQuery query = new JPAQuery(entityManager);
        BooleanBuilder builder = new BooleanBuilder();

        nameprefixes.stream().forEach(prefix -> {

            builder.or($.namePrefix.equalsIgnoreCase(prefix));

        });
        return jpaQueryFactory.selectFrom($)
                .where(builder).fetch();

    }

}
