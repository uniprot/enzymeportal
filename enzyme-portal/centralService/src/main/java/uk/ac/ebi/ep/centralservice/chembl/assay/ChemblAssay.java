package uk.ac.ebi.ep.centralservice.chembl.assay;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.ep.centralservice.chembl.service.PageMeta;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"assays",
"page_meta"
})
/**
 *
 * @author joseph
 */
public class ChemblAssay {
    
    @JsonProperty("assays")
private List<Assay> assays = new ArrayList<>();
@JsonProperty("page_meta")
private PageMeta pageMeta;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The assays
*/
@JsonProperty("assays")
public List<Assay> getAssays() {
return assays;
}

/**
* 
* @param assays
* The assays
*/
@JsonProperty("assays")
public void setAssays(List<Assay> assays) {
this.assays = assays;
}

/**
* 
* @return
* The pageMeta
*/
@JsonProperty("page_meta")
public PageMeta getPageMeta() {
return pageMeta;
}

/**
* 
* @param pageMeta
* The page_meta
*/
@JsonProperty("page_meta")
public void setPageMeta(PageMeta pageMeta) {
this.pageMeta = pageMeta;
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
