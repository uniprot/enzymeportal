/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.EnzymeCentric;
import uk.ac.ebi.ep.xml.generator.ProteinCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource(value = "classpath:ep-xml-config.properties", ignoreResourceNotFound = true)
public class XmlConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private EnzymePortalService enzymePortalService;
    @Autowired
    private EnzymePortalXmlService xmlService;
    @Autowired
    private Environment env;

    @Bean(name = "enzymeCentric")
    public XmlGenerator enzymeCentric() {
        return new EnzymeCentric(enzymePortalService, xmlService);
    }

    @Bean(name = "proteinCentric")
    public XmlGenerator proteinCentric() {
        return new ProteinCentric(enzymePortalService, xmlService);
    }

    @Bean
    public String releaseNumber() {
        return env.getProperty("release.number");
    }

    @Bean
    public String enzymeCentricXmlDir() {
        return env.getProperty("ep.enzyme.centric.xml.dir");
    }

    @Bean
    public String proteinCentricXmlDir() {
        return env.getProperty("ep.protein.centric.xml.dir");
    }

    @Bean
    public String ebeyeXSDs() {
        return env.getProperty("ep.ebeye.xsd");

    }

    //spring batch
    @Bean
    public ItemReader<UniprotEntry> databaseItemReader() {
        JdbcCursorItemReader<UniprotEntry> databaseReader = new JdbcCursorItemReader<>();
        String query = "SELECT * FROM UNIPROT_ENTRY WHERE ROWNUM < 1000";
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(query);
        databaseReader.setFetchSize(100);
        databaseReader.setDriverSupportsAbsolute(true);
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(UniprotEntry.class));

        return databaseReader;
    }

    @Bean
    public ItemReader<UniprotEntry> databaseItemReaderPageable() {
        JdbcPagingItemReader<UniprotEntry> databaseReader = new JdbcPagingItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setPageSize(10);

        PagingQueryProvider queryProvider = createQueryProvider();
        databaseReader.setQueryProvider(queryProvider);

        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(UniprotEntry.class));

        return databaseReader;
    }

    private PagingQueryProvider createQueryProvider() {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();

        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM UNIPROT_ENTRY");
        queryProvider.setSortKeys(sortByEntryTypeAsc());

        return queryProvider;
    }

    private Map<String, Order> sortByEntryTypeAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("ENTRY_TYPE", Order.ASCENDING);
        return sortConfiguration;
    }
}
