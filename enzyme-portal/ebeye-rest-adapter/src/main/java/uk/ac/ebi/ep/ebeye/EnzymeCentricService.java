package uk.ac.ebi.ep.ebeye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * REST client that communicates with the EBeye search web-service retrieving
 * only enzyme centric data.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int FACET_COUNT_LIMIT = 1_000;

    private final RestTemplate restTemplate;
    private final EbeyeIndexProps enzymeCentricProps;

    public EnzymeCentricService(RestTemplate restTemplate, EbeyeIndexProps enzymeCentricPropsFile) {
        this.restTemplate = restTemplate;
        this.enzymeCentricProps = enzymeCentricPropsFile;
    }

    private String buildQueryUrl(String endpoint, String query, int facetCount, String facets, int startPage, int pageSize) {

        //String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY,OMIM,compound_type&compound_name&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";

        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            //ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";
            return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);
        }
        return String.format(ebeyeQueryUrl, endpoint, query, facetCount, startPage, pageSize);
    }

    /**
     *
     * @param query searchTerm
     * @param startPage start page
     * @param pageSize page size
     * @param facets comma separated facets
     * @param facetCount number of facets to be returned
     * @return
     */
    public EBISearchResult getSearchResult(String query, int startPage, int pageSize, String facets, int facetCount) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "startPage can not be less than 0");

        if (facetCount > FACET_COUNT_LIMIT) {
            facetCount = FACET_COUNT_LIMIT;
        }

        return getEbiSearchResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetCount, facets, startPage, pageSize));
    }

    private EBISearchResult getEbiSearchResult(String url) {

        logger.info("URL sent to EBI Service " + url);

        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=OMIM:114500
    public EBISearchResult findEbiSearchResultsByOmimId(String omimId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(omimId != null, "'omimId' must not be null");

        String query = "OMIM:" + omimId;

        return getSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    public EBISearchResult findEbiSearchResultsByPathwayId(String pathwayId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(pathwayId != null, "'pathwayId' must not be null");

        String query = "REACT:" + pathwayId;

        return getSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=INTENZ:1.1.1.1
    public EBISearchResult findEbiSearchResultsByEC(String ec, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(ec != null, "'ec' must not be null");

        String query = "INTENZ:" + ec;

        return getSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=TAXONOMY:9606
    public EBISearchResult findEbiSearchResultsByTaxId(String taxId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(taxId != null, "'taxId' must not be null");

        String query = "TAXONOMY:" + taxId;

        return getSearchResult(query, startPage, pageSize, facets, facetCount);

    }
}
