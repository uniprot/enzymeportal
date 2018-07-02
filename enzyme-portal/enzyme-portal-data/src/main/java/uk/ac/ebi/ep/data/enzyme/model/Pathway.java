package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * <p>
 * Java class for Pathway complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 *
 *
 *
 */
public class Pathway
        extends Entity
        implements Serializable {

    protected String image;

    private String pathwayId;
    private String pathwayName;
    private String pathwayGroupId;

    public Pathway() {
    }

    public Pathway(String pathwayId, String pathwayName) {
        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;
        this.id = pathwayId;
        this.name = pathwayName;
    }  
    
    public Pathway(String pathwayGroupId,String pathwayId, String pathwayName) {
        this.id = pathwayId;
        this.name = pathwayName;
        this.groupId = pathwayGroupId;

        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;
        this.pathwayGroupId = pathwayGroupId;
    }

    public Pathway(String pathwayGroupId, String pathwayId, String pathwayName, String url) {
        this.id = pathwayId;
        this.name = pathwayName;
        this.url = url;
        this.groupId = pathwayGroupId;

        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getUrl() {
        return url;
    }

    public String getPathwayGroupId() {
        return pathwayGroupId;
    }

    public void setPathwayGroupId(String pathwayGroupId) {
        this.pathwayGroupId = pathwayGroupId;
    }
    
    

    public String getPathwayId() {
        return pathwayId;
    }

    public void setPathwayId(String pathwayId) {
        this.pathwayId = pathwayId;
    }

    public String getPathwayName() {
        return pathwayName;
    }

    public void setPathwayName(String pathwayName) {
        this.pathwayName = pathwayName;
    }

    /**
     * Gets the value of the image property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setImage(String value) {
        this.image = value;
    }

    public Pathway withImage(String value) {
        setImage(value);
        return this;
    }

    @Override
    public Pathway withId(String value) {
        setId(value);
        return this;
    }

    @Override
    public Pathway withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public Pathway withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public Pathway withUrl(Object value) {
        setUrl(value);
        return this;
    }

    @Override
    public Pathway withXrefs(Object... values) {
        if (values != null) {
            getXrefs().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public Pathway withXrefs(Collection<Object> values) {
        if (values != null) {
            getXrefs().addAll(values);
        }
        return this;
    }

    @Override
    public Pathway withEvidence(String... values) {
        if (values != null) {
            getEvidence().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public Pathway withEvidence(Collection<String> values) {
        if (values != null) {
            getEvidence().addAll(values);
        }
        return this;
    }

//    @Override
//    public String toString() {
//        return pathwayName;
//    }

    @Override
    public String toString() {
        return "Pathway{" + "pathwayId=" + pathwayId + ", pathwayName=" + pathwayName + ", pathwayGroupId=" + pathwayGroupId + '}';
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.pathwayName);
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
        final Pathway other = (Pathway) obj;
        return Objects.equals(this.pathwayName, other.pathwayName);
    }

}
