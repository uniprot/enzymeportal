package uk.ac.ebi.ep.centralservice.chembl.mechanism;

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
    "mechanisms",
    "page_meta"
})
/**
 *
 * @author joseph
 */
public class FdaApproved {

    @JsonProperty("mechanisms")
    private List<Mechanism> mechanisms = new ArrayList<>();
    @JsonProperty("page_meta")
    private PageMeta pageMeta;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The mechanisms
     */
    @JsonProperty("mechanisms")
    public List<Mechanism> getMechanisms() {
        return mechanisms;
    }

    /**
     *
     * @param mechanisms The mechanisms
     */
    @JsonProperty("mechanisms")
    public void setMechanisms(List<Mechanism> mechanisms) {
        this.mechanisms = mechanisms;
    }

    /**
     *
     * @return The pageMeta
     */
    @JsonProperty("page_meta")
    public PageMeta getPageMeta() {
        return pageMeta;
    }

    /**
     *
     * @param pageMeta The page_meta
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
