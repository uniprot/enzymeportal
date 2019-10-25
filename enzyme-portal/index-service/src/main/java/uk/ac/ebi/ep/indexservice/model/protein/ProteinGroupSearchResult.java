package uk.ac.ebi.ep.indexservice.model.protein;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hitCount",
    "entries",
    "facets"
})
public class ProteinGroupSearchResult {

    @JsonProperty("hitCount")
    private Integer hitCount;
    @JsonProperty("entries")
    private List<ProteinGroupEntry> entries;

    @JsonProperty("facets")
    private List<Facet> facets;

    @JsonProperty("hitCount")
    public Integer getHitCount() {
        if (hitCount == null) {
            hitCount = 0;
        }
        return hitCount;
    }

    @JsonProperty("hitCount")
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    @JsonProperty("entries")
    public List<ProteinGroupEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }

        return entries;
    }

    @JsonProperty("entries")
    public void setEntries(List<ProteinGroupEntry> entries) {
        this.entries = entries;
    }

    @JsonProperty("facets")
    public List<Facet> getFacets() {
        if (facets == null) {
            facets = new ArrayList<>();
        }
        return facets;
    }

    @JsonProperty("facets")
    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }
}
