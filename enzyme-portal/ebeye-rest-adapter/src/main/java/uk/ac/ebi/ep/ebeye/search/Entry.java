package uk.ac.ebi.ep.ebeye.search;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the result object from Enzyme Portal domain in Ebeye Search Index
 *
 * @author joseph
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Entry implements Comparable<Entry> {
    @JsonProperty("acc")
    private String uniprotAccession;
    @JsonProperty("id")
    private String uniprotName;
    @JsonProperty("source")
    private String source;

    private String title;

    @JsonIgnore
    private final Map<String, Fields> fields = new HashMap<>();

    public Entry() {}

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

    @Override
    public String toString() {
        return "Entry{" + "uniprot_accession=" + uniprotAccession + ", uniport_name=" + uniprotName + ", source=" +
                source + '}';
    }

    @Override
    public int compareTo(Entry obj) {
        int compare = obj.getUniprotName().compareToIgnoreCase(this.getUniprotName());

        return compare == 0 ? obj.getUniprotName().compareToIgnoreCase(this.getUniprotName()) : compare;
    }

    public Fields get(String name) {
        return fields.get(name);
    }

    @JsonAnyGetter
    public Map<String, Fields> any() {
        return fields;
    }

    @JsonAnySetter
    public void set(String name, Fields value) {
        fields.put(name, value);
    }

    public String getTitle() {
        if (get("fields") != null) {
            title = get("fields").getName().stream().findFirst().get();
        }
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