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

    public EnzymeReaction() {
    }
    
   

    public EnzymeReaction(String id, String name) {
        this.id = id;
        this.name = name;
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
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final EnzymeReaction other = (EnzymeReaction) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    

}
