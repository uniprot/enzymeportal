package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author joseph
 */
public class Compound  implements  Serializable {
 private static final long serialVersionUID = 1L;
    private Compound.Role role;
    protected boolean selected;

    protected String id;

    protected String name;
    protected String definition;

    protected transient Object url;

    public Compound() {
    }

    public Compound( String id, String name, Object url,String role) {
        this.role = Role.valueOf(role);
        this.id = id;
        this.name = name;
        this.url = url;
    }
    
    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.name);
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
        final Compound other = (Compound) obj;
        return Objects.equals(this.name, other.name);
    }




    public enum Role {

        SUBSTRATE_OR_PRODUCT,
        COFACTOR,
        ACTIVATOR,
        INHIBITOR,
        DRUG,
        BIOACTIVE;

        public String value() {
            return name();
        }

        public static Compound.Role fromValue(String v) {
            return valueOf(v);
        }

    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the definition property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Sets the value of the definition property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDefinition(String value) {
        this.definition = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return possible object is {@link Object }
     *
     */
    public Object getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value allowed object is {@link Object }
     *
     */
    public void setUrl(Object value) {
        this.url = value;
    }

    /**
     * Gets the value of the role property.
     *
     * @return possible object is {@link Compound.Role }
     *
     */
    public Compound.Role getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is {@link Compound.Role }
     *
     */
    public void setRole(Compound.Role value) {
        this.role = value;
    }

    /**
     * Gets the value of the selected property.
     *
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the value of the selected property.
     *
     */
    public void setSelected(boolean value) {
        this.selected = value;
    }

    public Compound withId(String value) {
        setId(value);
        return this;
    }

    public Compound withName(String value) {
        setName(value);
        return this;
    }

    public Compound withDefinition(String value) {
        setDefinition(value);
        return this;
    }

    public Compound withUrl(Object value) {
        setUrl(value);
        return this;
    }

    public Compound withRole(Compound.Role value) {
        setRole(value);
        return this;
    }

    public Compound withSelected(boolean value) {
        setSelected(value);
        return this;
    }

}
