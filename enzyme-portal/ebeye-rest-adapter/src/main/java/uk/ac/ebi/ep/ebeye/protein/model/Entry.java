package uk.ac.ebi.ep.ebeye.protein.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import uk.ac.ebi.ep.ebeye.model.Fields;

/**
 * Represents the result object from Enzyme Portal domain in Ebeye Search Index
 *
 * @author joseph
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "acc",
    "id",
    "source",
    "fields"
})
//public final class Entry extends EnzymeEntry implements Serializable, Comparable<Entry> {
public final class Entry extends ProteinView implements Serializable, Comparable<Entry> {

    private static final long serialVersionUID = 1L;
    @JsonProperty("acc")
    private String uniprotAccession;
    @JsonProperty("id")
    private String uniprotName;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;

    @JsonIgnore
    transient final Map<String, Fields> fieldsMap = new HashMap<>();

    public Entry() {
    }

    public Entry(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    public Entry(String uniprotAccession, String uniprotName, String source) {
        this.uniprotAccession = uniprotAccession;
        this.uniprotName = uniprotName;
        this.source = source;
    }

    public Entry(String uniprotAccession, String uniprotName, Fields fields) {
        this.uniprotAccession = uniprotAccession;
        this.uniprotName = uniprotName;
        this.fields = fields;
    }

    public Entry(String uniprotAccession, String uniprotName, String source, Fields fields) {
        this.uniprotAccession = uniprotAccession;
        this.uniprotName = uniprotName;
        this.source = source;
        this.fields = fields;
    }

    /**
     * @return The uniprot name
     */
    public String getUniprotName() {
        return uniprotName.substring(0, uniprotName.indexOf("_"));
    }

    /**
     * @return uniprot accession
     */
    public String getUniprotAccession() {
        return uniprotAccession;
    }

    /**
     * @return source data index
     */
    public String getSource() {
        return source;
    }

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
    public List<String> getScientificName() {

        return fields.getScientificName();
    }

    @Override
    public Protein getProtein() {
        String name = fields.getName().stream().findFirst().orElse("");
        String scienceName = getScientificName().stream().findFirst().orElse("");
        String status = fields.getStatus().stream().findFirst().orElse("");

        return new Protein(getUniprotAccession(), name, scienceName, status);
    }

    @Override
    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "Entry{" + "uniprotAccession=" + uniprotAccession + ", uniprotName=" + uniprotName + ", Title=" + getTitle() + ", source=" + source + '}';
    }

    @Override
    public int compareTo(Entry obj) {
//        int compare = obj.getUniprotName().compareToIgnoreCase(this.getUniprotName());
//
//        return compare == 0 ? obj.getUniprotName().compareToIgnoreCase(this.getUniprotName()) : compare;
//        

        int compare = obj.getFields().getStatus().stream().findFirst().orElse("").compareToIgnoreCase(this.getFields().getStatus().stream().findFirst().orElse(""));

        return compare == 0 ? obj.getFields().getStatus().stream().findFirst().orElse("").compareToIgnoreCase(this.getFields().getStatus().stream().findFirst().orElse("")) : compare;

    }

    public Fields get(String name) {
        return fieldsMap.get(name);
    }

    @JsonAnyGetter
    public Map<String, Fields> any() {
        return fieldsMap;
    }

    @JsonAnySetter
    public void set(String name, Fields value) {
        fieldsMap.put(name, value);
    }

    @Override
    public String getTitle() {
//        if (get("fields") != null) {
//            title = get("fields").getName().stream().findFirst().get();
//        }

        return fields.getName().stream().findFirst().orElse("");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.getTitle());

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
        final Entry other = (Entry) obj;

        return Objects.equals(this.getTitle(), other.getTitle());
    }
}
