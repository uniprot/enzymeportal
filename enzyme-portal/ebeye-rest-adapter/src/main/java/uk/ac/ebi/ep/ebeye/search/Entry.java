package uk.ac.ebi.ep.ebeye.search;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import uk.ac.ebi.ep.ebeye.model.Protein;

/**
 * Represents the result object from Enzyme Portal domain in Ebeye Search Index
 *
 * @author joseph
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Entry extends EnzymeEntry implements Serializable, Comparable<Entry> {

    private static final long serialVersionUID = 1L;
    @JsonProperty("acc")
    private String uniprotAccession;
    @JsonProperty("id")
    private String uniprotName;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;

    private String title;
    private List<String> scientificName;
    protected Protein protein;

    @JsonIgnore
    private final Map<String, Fields> fieldsMap = new HashMap<>();

    public Entry() {
    }

    public Entry(String ec, String source, String title) {
        super(ec);
        this.source = source;
        this.title = title;
    }

    public Entry(String uniprotAccession, String uniprotName) {
        this.uniprotAccession = uniprotAccession;
        this.uniprotName = uniprotName;
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

    public List<String> getScientificName() {
        scientificName = fields.getScientificName();
        return scientificName;
    }

    public Protein getProtein() {
        String name = fields.getName().stream().findFirst().orElse("");
        String scienceName = getScientificName().stream().findFirst().orElse("");
        String status = fields.getStatus().stream().findFirst().orElse("");
        return new Protein(getUniprotAccession(), name, scienceName,status);
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "Entry{" + "uniprotAccession=" + uniprotAccession
                + ", uniprotName=" + uniprotName + ", source=" + source + ", title=" + title + ", ec=" + getEc() + '}';
    }

    @Override
    public int compareTo(Entry obj) {
        int compare = obj.getUniprotName().compareToIgnoreCase(this.getUniprotName());

        return compare == 0 ? obj.getUniprotName().compareToIgnoreCase(this.getUniprotName()) : compare;
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

    public String getTitle() {
//        if (get("fields") != null) {
//            title = get("fields").getName().stream().findFirst().get();
//        }
        title = fields.getName().stream().findFirst().orElse("");
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
