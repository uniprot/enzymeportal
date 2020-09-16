//package uk.ac.ebi.ep.config;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// *
// * @author Joseph <joseph@ebi.ac.uk>
// */
//@Configuration
//public class QueryDSLConfig {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }
//}
