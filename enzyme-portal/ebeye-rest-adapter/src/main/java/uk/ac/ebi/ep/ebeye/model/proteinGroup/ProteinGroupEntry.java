package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public class ProteinGroupEntry implements ProteinView {

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

    @Override
    public String getProteinName() {
        return getFields().getName().stream().findAny().orElse("");
    }

    @Override
    public String getPrimaryOrganism() {
        return fields.getCommonName().stream().findFirst().orElse(fields.getScientificName().stream().findFirst().orElse("Please report this error"));

    }

    @Override
    public String getPrimaryAccession() {
        //temp implementation till we have primary accession in the index
        return fields.getUNIPROTKB().stream().findAny().orElse("This should not happen .. report");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final ProteinGroupEntry other = (ProteinGroupEntry) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Entry{" + "proteinGroupId=" + getProteinGroupId() + ", proteinName=" + getProteinName() + '}';
    }

       // temp implementation till we have primary specie in the index
//    public List<String> getSpecies() {
//        Map<Integer, String> priorityMapper = new TreeMap<>();
//        AtomicInteger key = new AtomicInteger(50);
//        AtomicInteger customKey = new AtomicInteger(6);
//        LinkedList<String> sortedSpecies = new LinkedList<>();
//
//        List<String> species = fields.getCommonName();
//        if (species.isEmpty()) {
//            species = fields.getScientificName();
//        }
//
//        species.stream().forEach((name) -> {
//            sortSpecies(name, priorityMapper, customKey, key);
//        });
//
//        priorityMapper.entrySet().stream().forEach(map -> {
//            sortedSpecies.add(map.getValue());
//        });
//        return sortedSpecies.stream().distinct().limit(2).collect(Collectors.toList());
//
//    }
//    
//        private void sortSpecies(String specieName, Map<Integer, String> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
//
//        if (specieName.equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {
//
//            priorityMapper.put(1, specieName);
//        } else if (specieName.equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {
//
//            priorityMapper.put(2, specieName);
//        } else if (specieName.equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getCommonName())) {
//            priorityMapper.put(3, specieName);
//
//        } else if (specieName.equalsIgnoreCase(ModelOrganisms.RAT.getCommonName())) {
//            priorityMapper.put(customKey.getAndIncrement(), specieName);
//
//        } else {
//            priorityMapper.put(key.getAndIncrement(), specieName);
//        }
//    }

    @Override
    @Deprecated
    public String getPrimarySpecie() {
       return fields.getCommonName().stream().findFirst().orElse(fields.getScientificName().stream().findFirst().orElse("Please report this error")); 
    }
}
