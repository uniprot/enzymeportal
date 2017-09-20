package uk.ac.ebi.ep.config;

//
//package uk.ac.ebi.ep.data.dataconfig;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContextType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import uk.ac.ebi.ep.data.service.HibernateSearchService;
//
///**
// *
// * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
// */
//@Configuration
//public class HibernateSearchConfig {
//    private final Logger logger = LoggerFactory.getLogger(HibernateSearchConfig.class);
//
//	//@Autowired
//          //@PersistenceContext
//          @PersistenceContext(type = PersistenceContextType.EXTENDED)
//	private EntityManager entityManager;
//
//	@Bean
//	HibernateSearchService hibernateSearchService() {
//		HibernateSearchService hibernateSearchService = new HibernateSearchService(entityManager);
//		//hibernateSearchService.initializeHibernateSearch();
//		return hibernateSearchService;
//	}
//}
