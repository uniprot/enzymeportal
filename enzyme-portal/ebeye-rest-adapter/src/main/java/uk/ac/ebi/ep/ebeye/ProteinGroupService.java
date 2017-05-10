package uk.ac.ebi.ep.ebeye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class ProteinGroupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    public static final String simpleUrl = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=sildenafil&start=0&size=100&fields=id,name";
//    public static final String pGroupByEC = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=INTENZ:2.1.1.1&start=0&size=100&fields=id,name";
//    String pGroupByECJson = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=INTENZ:2.1.1.1&start=0&size=100&fields=id,name&sort=_relevance&reverse=true&format=json";
//    public static final String restUrl = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=sildenafil&fields=id,name,common_name,scientific_name,compound_name,disease_name";
    public static final String QUERY_URL = "%s?query=%s&start=%d&size=%d&fields=id,name,UNIPROTKB,common_name,scientific_name&sort=_relevance&reverse=true&format=json";

    private final RestTemplate restTemplate;
    private final EbeyeIndexProps proteinGroupProps;

    public ProteinGroupService(RestTemplate restTemplate, EbeyeIndexProps proteinGroupPropertyFile) {
        this.restTemplate = restTemplate;
        this.proteinGroupProps = proteinGroupPropertyFile;
    }

    private ProteinGroupResult getProteinGroupResult(String url) {

        logger.info("URL sent to EBI Service " + url);

        ProteinGroupResult results = restTemplate.getForObject(url.trim(), ProteinGroupResult.class);
        return results;
    }

    private String buildQueryUrl(String endpoint, String query, int startPage, int pageSize) {

        //String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";
        return String.format(QUERY_URL, endpoint, query, startPage, pageSize);
    }

    /**
     *
     * @param query searchTerm
     * @param startPage start page
     * @param pageSize page size
     * @return
     */
    private ProteinGroupResult getSearchResult(String query, int startPage, int pageSize) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");

        return getProteinGroupResult(buildQueryUrl(proteinGroupProps.getProteinGroupSearchUrl(), query, startPage, pageSize));
    }

    public ProteinGroupResult findProteinGroupResultByEC(String ec, int startPage, int pageSize) {
        Preconditions.checkArgument(ec != null, "'ec' must not be null");
        if (pageSize > 100) {
            pageSize = 100;
        }

        String query = "INTENZ:" + ec;

        return getSearchResult(query, startPage, pageSize);

    }

    public ProteinGroupResult findProteinGroupResultBySearchTermAndEC(String ec, String searchTerm, int startPage, int pageSize) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
        String query = searchTerm + " AND INTENZ:" + ec;
        if (pageSize > 100) {
            pageSize = 100;
        }
        //query = UrlUtil.encode(query);
        return getSearchResult(query, startPage, pageSize);
    }

}
