package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import uk.ac.ebi.ep.ebeye.protein.model.ModelOrganisms;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//    "id",
//    "source",
//    "fields"
//})
public class ProteinGroupEntry {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();
    
  
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("fields")
    public Fields getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getProteinGroupId() {
        return getId();
    }

    public String getProteinName() {
        return getFields().getName().stream().findAny().orElse("");
    }

    @Override
    public String toString() {
        return "Entry{" + "proteinGroupId=" + getProteinGroupId() + ", proteinName=" + getProteinName() + '}';
    }

    public String getPrimarySpecie() {
       //temp implementation till we have primary specie in the index
        return getSpecies().stream().findFirst().orElse("Please report this error");
    }

    public String getPrimaryAccession() {
       //temp implementation till we have primary accession in the index
        return fields.getUNIPROTKB().stream().findAny().orElse("This should not happen .. report");
    }
    
    
    
    
    
    
    
       // temp implementation till we have primary specie in the index
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
        return sortedSpecies.stream().distinct().limit(2).collect(Collectors.toList());

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
