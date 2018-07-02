package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Java class for ReactionPathway complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 *
 *
 */
public class ReactionPathway
        implements Serializable {

    protected transient EnzymeReaction reaction;
    protected transient List<Pathway> pathways;

    protected transient List<Object> mechanism;

    protected transient List<String> provenance;

    protected transient List<EnzymeReaction> reactions;
    

    public List<EnzymeReaction> getReactions() {
        if (reactions == null) {
            reactions = new ArrayList<>();
        }

        return reactions;
    }

    public void setReactions(List<EnzymeReaction> reactions) {
        this.reactions = reactions;
    }

    /**
     * Gets the value of the reaction property.
     *
     * @return possible object is {@link EnzymeReaction }
     *
     */
    public EnzymeReaction getReaction() {
        return reaction;
    }

    /**
     * Sets the value of the reaction property.
     *
     * @param value allowed object is {@link EnzymeReaction }
     *
     */
    public void setReaction(EnzymeReaction value) {
        this.reaction = value;
    }

    /**
     * Gets the value of the pathways property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the pathways property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPathways().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Pathway }
     *
     *
     * @return pathways
     */
    public List<Pathway> getPathways() {
        if (pathways == null) {
            pathways = new ArrayList<>();
        }
        return this.pathways;
    }

    /**
     * Gets the value of the mechanism property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the mechanism property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMechanism().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Object }
     *
     *
     */
    public List<Object> getMechanism() {
        if (mechanism == null) {
            mechanism = new ArrayList<Object>();
        }
        return this.mechanism;
    }

    /**
     * Gets the value of the provenance property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the provenance property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProvenance().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getProvenance() {
        if (provenance == null) {
            provenance = new ArrayList<String>();
        }
        return this.provenance;
    }

    public ReactionPathway withReaction(EnzymeReaction value) {
        setReaction(value);
        return this;
    }

    public ReactionPathway withPathways(Pathway... values) {
        if (values != null) {
            for (Pathway value : values) {
                getPathways().add(value);
            }
        }
        return this;
    }

    public ReactionPathway withPathways(Collection<Pathway> values) {
        if (values != null) {
            getPathways().addAll(values);
        }
        return this;
    }

    public ReactionPathway withMechanism(Object... values) {
        if (values != null) {
            for (Object value : values) {
                getMechanism().add(value);
            }
        }
        return this;
    }

    public ReactionPathway withMechanism(Collection<Object> values) {
        if (values != null) {
            getMechanism().addAll(values);
        }
        return this;
    }

    public ReactionPathway withProvenance(String... values) {
        if (values != null) {
            for (String value : values) {
                getProvenance().add(value);
            }
        }
        return this;
    }

    public ReactionPathway withProvenance(Collection<String> values) {
        if (values != null) {
            getProvenance().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the pathways property.
     *
     * @param pathways allowed object is {@link Pathway }
     *
     */
    public void setPathways(List<Pathway> pathways) {
        this.pathways = pathways;
    }

    /**
     * Sets the value of the mechanism property.
     *
     * @param mechanism allowed object is {@link Object }
     *
     */
    public void setMechanism(List<Object> mechanism) {
        this.mechanism = mechanism;
    }

    /**
     * Sets the value of the provenance property.
     *
     * @param provenance allowed object is {@link String }
     *
     */
    public void setProvenance(List<String> provenance) {
        this.provenance = provenance;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.reaction);
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
        final ReactionPathway other = (ReactionPathway) obj;
        if (!Objects.equals(this.reaction, other.reaction)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReactionPathway{" + "reaction=" + reaction + ", pathways=" + pathways + ", reactions=" + reactions + '}';
    }

}
