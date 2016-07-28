package uk.ac.ebi.ep.ebeye.enzyme.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import uk.ac.ebi.ep.ebeye.model.Fields;
import uk.ac.ebi.ep.ebeye.protein.model.ModelOrganisms;

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
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public Entry() {

    }

    /**
     *
     * @param id ec number
     * @param fields
     */
    public Entry(String id, Fields fields) {
        this.id = id;
        this.fields = fields;
    }

    /**
     *
     * @param id ec number
     * @param source
     * @param fields
     */
    public Entry(String id, String source, Fields fields) {
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
    public List<String> getSpecies() {
        Map<Integer, String> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        LinkedList<String> sortedSpecies = new LinkedList<>();

        List<String> species = fields.getCommonName();
        if (species.isEmpty()) {
            species = fields.getScientificName();
        }

        species.stream().forEach((name) -> {
            sortSpecies(name, priorityMapper, customKey, key);
        });

        priorityMapper.entrySet().stream().forEach(map -> {
            sortedSpecies.add(map.getValue());
        });
        return sortedSpecies.stream().distinct().collect(Collectors.toList());

    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    private void sortSpecies(String specieName, Map<Integer, String> priorityMapper, AtomicInteger customKey, AtomicInteger key) {

        if (specieName.equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {

            priorityMapper.put(1, specieName);
        } else if (specieName.equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {

            priorityMapper.put(2, specieName);
        } else if (specieName.equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getCommonName())) {
            priorityMapper.put(3, specieName);

        } else if (specieName.equalsIgnoreCase(ModelOrganisms.RAT.getCommonName())) {
            priorityMapper.put(customKey.getAndIncrement(), specieName);

        } else {
            priorityMapper.put(key.getAndIncrement(), specieName);
        }
    }

}
