package uk.ac.ebi.ep.ebeye.model.enzyme;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "fields"
})
public class EnzymeEntry extends AbstractPageView implements EnzymeView {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private EnzymeFields fields;

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public EnzymeEntry() {

    }

//    public EnzymeEntry(String id) {
//        this.id = id;
//    }

    /**
     *
     * @param id ec number
     * @param fields
     */
    public EnzymeEntry(String id, EnzymeFields fields) {
        this.id = id;
        this.fields = fields;
    }

    /**
     *
     * @param id ec number
     * @param source
     * @param fields
     */
    public EnzymeEntry(String id, String source, EnzymeFields fields) {
        this.id = id;
        this.source = source;
        this.fields = fields;
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
    public EnzymeFields getFields() {
        return fields;
    }

    /**
     *
     * @param fields The fields
     */
    @JsonProperty("fields")
    public void setFields(EnzymeFields fields) {
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
    public Set<String> getIntenzCofactors() {
        return fields.getIntenzCofactors();
    }

    @Override
    public Set<String> getAltNames() {
        return fields.getAltNames();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymeEntry other = (EnzymeEntry) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
         return "EnzymeEntry{" + "id=" + id + ", fields=" + fields + '}';
       // return id;
    }

}
