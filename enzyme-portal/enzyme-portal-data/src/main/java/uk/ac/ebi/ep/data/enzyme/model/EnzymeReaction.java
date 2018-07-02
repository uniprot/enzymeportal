package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * <p>
 * Java class for EnzymeReaction complex type.
 *
 *
 *
 *
 */
public class EnzymeReaction
        extends Entity
        implements Serializable {

    protected Equation equation;
    private String rheadId;
    private String keggId;

    public EnzymeReaction() {
    }

    public EnzymeReaction(String id) {
        this.id = id;
        this.rheadId = id;

    }

    public EnzymeReaction(String rheadId, String keggId) {
        this.rheadId = rheadId;
        this.keggId = keggId;
        this.id = rheadId;
    }

    public String getRheadId() {
        return rheadId;
    }

    public void setRheadId(String rheadId) {
        this.rheadId = rheadId;
    }

    public String getKeggId() {
        return keggId;
    }

    public void setKeggId(String keggId) {
        this.keggId = keggId;
    }

    /**
     * Gets the value of the equation property.
     *
     * @return possible object is {@link Equation }
     *
     */
    public Equation getEquation() {
        return equation;
    }

    /**
     * Sets the value of the equation property.
     *
     * @param value allowed object is {@link Equation }
     *
     */
    public void setEquation(Equation value) {
        this.equation = value;
    }

    public EnzymeReaction withEquation(Equation value) {
        setEquation(value);
        return this;
    }

    @Override
    public EnzymeReaction withId(String value) {
        setId(value);
        return this;
    }

    @Override
    public EnzymeReaction withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public EnzymeReaction withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public EnzymeReaction withUrl(Object value) {
        setUrl(value);
        return this;
    }

    @Override
    public EnzymeReaction withXrefs(Object... values) {
        if (values != null) {
            for (Object value : values) {
                getXrefs().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeReaction withXrefs(Collection<Object> values) {
        if (values != null) {
            getXrefs().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeReaction withEvidence(String... values) {
        if (values != null) {
            for (String value : values) {
                getEvidence().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeReaction withEvidence(Collection<String> values) {
        if (values != null) {
            getEvidence().addAll(values);
        }
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.rheadId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymeReaction other = (EnzymeReaction) obj;
        if (!Objects.equals(this.rheadId, other.rheadId)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "EnzymeReaction{" + "keggId=" + keggId + ", rheadId=" + rheadId + '}';
    }

}
