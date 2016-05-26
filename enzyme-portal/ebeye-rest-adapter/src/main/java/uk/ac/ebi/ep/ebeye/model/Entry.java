/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.PowerService;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;

/**
 * Represents the result object from Enzyme Portal domain in EBI Search service
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "fields"
})
public class Entry extends EnzymeView {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private Set<Protein> proteins = null;

    private PowerService powerService;

    public Entry() {

        proteins = new HashSet<>();
        EbeyeIndexUrl ebeyeIndexUrl = new EbeyeIndexUrl();
        ebeyeIndexUrl.setChunkSize(10);
        ebeyeIndexUrl.setMaxEbiSearchLimit(100);
        ebeyeIndexUrl.setDefaultSearchIndexUrl("http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal");
        // proteinService = new EbeyeProteinService(ebeyeIndexUrl, new RestTemplate());
        powerService = new PowerService(ebeyeIndexUrl, new AsyncRestTemplate(), new RestTemplate());
      
    }

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return The fields
     */
    @JsonProperty("fields")
    public Fields getFields() {
        return fields;
    }

    /**
     *
     * @param fields The fields
     */
    @JsonProperty("fields")
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public String getEc() {
        return id;
    }

    @Override
    public String getEnzymeName() {
        return fields.getName().stream().findFirst().orElse("");

    }

    @Override
    public String getEnzymeFamily() {
        return fields.getEnzymeFamily().stream().findFirst().orElse("");

    }

    @Override
    public List<String> getCatalyticActivities() {

        return fields.getDescription().stream().distinct().collect(Collectors.toList());

    }

    @Override
    public int getNumEnzymeHits() {
        return fields.getProteinName().size();

    }

    @Override
    public List<String> getSpecies() {
        return fields.getCommonName();

    }

    @Override
    public List<String> getProteins() {
        //System.out.println("calling protins name "+ id);
        return fields.getProteinName().stream().collect(Collectors.toList());

    }

    @Override
    public List<Protein> getProtein() {
        System.out.println("calling protein .."+ id);
        List<Protein>  p = this.powerService.queryForUniqueProteins(id, 5);
     
        return p;

    }

    private List<Protein> buildProtein() {
        List<String> accessions = fields.getUNIPROTKB().stream().collect(Collectors.toList());
   

        do {
            if (proteins.size() == 5) {
                break;
            }
            //for (String accession : accessions) {
            String accession = "";
            List<uk.ac.ebi.ep.ebeye.search.Entry> entries =null;// ps.getProteinView(id, accession).stream().distinct().collect(Collectors.toList());
     
            for (uk.ac.ebi.ep.ebeye.search.Entry entry : entries) {
               String sc = entry.getScientificName().stream().findFirst().orElse("");
                Protein protein = new Protein(entry.getUniprotAccession(), entry.getTitle(), sc);
                proteins.add(protein);
            }
            //}
        } while (proteins.size() <= 5);

        return proteins.stream().collect(Collectors.toList());
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
