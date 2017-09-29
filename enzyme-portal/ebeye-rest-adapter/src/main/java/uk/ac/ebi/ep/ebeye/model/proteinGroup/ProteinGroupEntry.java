package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
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
        return fields.getPrimaryOrganism().stream().findFirst().orElse("");

    }

    @Override
    public String getPrimaryAccession() {

        return fields.getPrimaryAccession().stream().findAny().orElse("");
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

//    @Override
//    public String toString() {
//        return "Entry{" + "proteinGroupId=" + getProteinGroupId() + ", proteinName=" + getProteinName() + '}';
//    }
    @Override
    public String getEntryType() {
        return fields.getEntryType().stream().findAny().orElse("");
    }

    @Override
    public List<String> getGeneName() {
        return fields.getGeneName();
    }

    @Override
    public PdbImage getPrimaryImage() {
        return fields.getPrimaryImage()
                .stream()
                .map(m -> m.split("\\|"))
                .map(p -> new PdbImage(p[0], p[1]))
                .findAny()
                .orElse(new PdbImage());

    }

    @Override
    public String getFunction() {
        return fields.getFunction().stream().findAny().orElse("");
    }

    @Override
    public List<RelSpecies> getRelatedSpecies() {

        return buildRelSpecies();
    }

    private List<RelSpecies> buildRelSpecies() {
        Set<RelSpecies> specieList = new LinkedHashSet<>();
       // List<String> ph = Stream.of("O76074;Human;Homo sapiens | Q28156;Bovine;Bos taurus | Q8CG03;Mouse;Mus musculus | O54735;Rat;Rattus norvegicus | O77746;Dog;Canis lupus familiaris").collect(Collectors.toList());
        List<String[]> items
                = fields.getRelatedSpecies()
                .stream()
                .map(String::trim)
                .map(rel -> rel.replace("\\|", " \\|"))
                .map(s -> s.split("\\|"))
                .collect(toList());

        List<String> foo = new LinkedList<>();
        for (String[] item : items) {
            foo = Arrays.asList(item);
        }

        specieList = foo.stream()
                .map(comma -> comma.split("\\;"))
                .map(specie -> new RelSpecies(specie[0], specie[1], specie[2]))
                .collect(Collectors.toSet());

       // return specieList.stream().collect(Collectors.toList());
        return sortByModelOrganism(specieList);
    }

    @Override
    public List<String> getSynonym() {
       return fields.getSynonym().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getdiseases() {
        return fields.getDiseaseName();
    }
    
   
    private List<RelSpecies> sortByModelOrganism(Set<RelSpecies> species){
            Map<Integer, RelSpecies> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        LinkedList<RelSpecies> sortedSpecies = new LinkedList<>();


        species.stream().forEach((name) -> {
            sortSpecies(name, priorityMapper, customKey, key);
        });

        priorityMapper.entrySet().stream().forEach(map -> {
            sortedSpecies.add(map.getValue());
        });
        return sortedSpecies.stream().distinct().limit(50).collect(Collectors.toList());
    }
    
    private void sortSpecies(RelSpecies specie, Map<Integer, RelSpecies> priorityMapper, AtomicInteger customKey, AtomicInteger key) {

        if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {

            priorityMapper.put(1, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {

            priorityMapper.put(2, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getCommonName())) {
            priorityMapper.put(3, specie);

        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.RAT.getCommonName())) {
            priorityMapper.put(customKey.getAndIncrement(), specie);

        } else {
            priorityMapper.put(key.getAndIncrement(), specie);
        }
    }



}
