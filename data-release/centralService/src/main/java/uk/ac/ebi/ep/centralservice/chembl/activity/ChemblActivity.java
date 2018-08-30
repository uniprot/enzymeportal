package uk.ac.ebi.ep.centralservice.chembl.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.centralservice.chembl.service.PageMeta;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "activities",
    "page_meta"
})
/**
 *
 * @author joseph
 */
public class ChemblActivity {

    @JsonProperty("activities")
    private List<Activity> activities = new ArrayList<>();
    @JsonProperty("page_meta")
    private PageMeta pageMeta;

    /**
     *
     * @return The activities
     */
    @JsonProperty("activities")
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     *
     * @param activities The activities
     */
    @JsonProperty("activities")
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    /**
     *
     * @return The pageMeta
     */
    @JsonProperty("page_meta")
    public PageMeta getPageMeta() {
        if(pageMeta == null){
            pageMeta = new PageMeta();
        }
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


}
