package uk.ac.ebi.ep.ebeye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;
import uk.ac.ebi.ep.ebeye.utils.UrlUtil;

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
        //String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";
        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:enzyme_family&start=%d&size=%d&fields=id,name,description,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";

        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            //ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            //ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";
            //return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);

            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";
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
    public EnzymeSearchResult getQuerySearchResult(String query, int startPage, int pageSize, String facets, int facetCount) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "facetCount can not be less than 0");
        int facetsCount = facetCount;
        if (facetCount > FACET_COUNT_LIMIT) {
            facetsCount = FACET_COUNT_LIMIT;
        }
        query = UrlUtil.encode(query);
        return getEbiSearchResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetsCount, facets, startPage, pageSize));
    }

    private EnzymeSearchResult getEnzymeSearchResult(String query, int startPage, int pageSize, String facets, int facetCount) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "facetCount can not be less than 0");
        int facetsCount = facetCount;
        if (facetCount > FACET_COUNT_LIMIT) {
            facetsCount = FACET_COUNT_LIMIT;
        }

        return getEbiSearchResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetsCount, facets, startPage, pageSize));
    }

    //TODO return minimal fields for faster loading of enzyme-centric view page
    private EnzymeSearchResult getEbiSearchResult(String url) {

        logger.info("URL sent to EBI Service " + url);
        EnzymeSearchResult results = restTemplate.getForObject(url.trim(), EnzymeSearchResult.class);
        return results;
    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=OMIM:114500
    public EnzymeSearchResult findEbiSearchResultsByOmimId(String omimId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(omimId != null, "'omimId' must not be null");

        String query = "OMIM:" + omimId;

        return getEnzymeSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    //http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?query=REACTOME:R-CFA-1489509%20OR%20REACTOME:R-HSA-1489509&size=100&start=0&fields=name,scientific_name,status&sort=status&reverse=true&format=xml
//http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=REACTOME:R-GGA-189451
    public EnzymeSearchResult findEbiSearchResultsByPathwayId(String pathwayId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(pathwayId != null, "'pathwayId' must not be null");

        String query = "REACTOME:" + pathwayId;

        return getEnzymeSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=INTENZ:1.1.1.1
    public EnzymeSearchResult findEbiSearchResultsByEC(String ec, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(ec != null, "'ec' must not be null");

        //String query = "INTENZ:" + ec;
        String query = "id:" + ec;

        return getEnzymeSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    public EnzymeSearchResult findEnzymeByEC(String ec) {
        Preconditions.checkArgument(ec != null, "'ec' must not be null");
        String facets = "";
        int facetCount = 0;
        int startPage = 0;
        int pageSize = 1;
        //String query = "INTENZ:" + ec;
        String query = "id:" + ec;

        return getEnzymeSearchResult(query, startPage, pageSize, facets, facetCount);

    }

    //http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=TAXONOMY:9606
    public EnzymeSearchResult findEbiSearchResultsByTaxId(String taxId, int startPage, int pageSize, String facets, int facetCount) {
        Preconditions.checkArgument(taxId != null, "'taxId' must not be null");

        String query = "TAXONOMY:" + taxId;

        return getEnzymeSearchResult(query, startPage, pageSize, facets, facetCount);

    }
// current model TODO

    private EnzymeSearchResult getEnzymeViewResult(String url) {

        logger.info("URL sent to EBI Service " + url);
        EnzymeSearchResult results = restTemplate.getForObject(url.trim(), EnzymeSearchResult.class);
        return results;
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
    public EnzymeSearchResult getEnzymeResult(String query, int startPage, int pageSize, String facets, int facetCount) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "facetCount can not be less than 0");
        int facetsCount = facetCount;
        if (facetCount > FACET_COUNT_LIMIT) {
            facetsCount = FACET_COUNT_LIMIT;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        return getEnzymeViewResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetsCount, facets, startPage, pageSize));
    }
}
