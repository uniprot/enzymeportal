package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 * <p>
 * Java class for EnzymeModel complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 *
 *
 */
public class EnzymeModel extends UniprotEntry
        //extends EnzymeSummary
        implements Serializable {

    protected Enzyme enzyme;
    protected List<ProteinStructure> proteinstructure;
    protected List<ReactionPathway> reactionpathway;

    protected String requestedfield;

    protected ChemicalEntity molecule;
    protected List<Disease> disease;

    protected List<Object> literature;

    protected List<Pathway> pathways = new ArrayList<>();
    protected List<EnzymeAccession> relatedspecies;
    
    public List<Pathway> getPathways() {
        if (pathways == null) {
            pathways = new ArrayList<>();
        }
        return pathways;
    }

    public void setPathways(List<Pathway> pathways) {
        this.pathways = pathways;
    }

    @Override
    public List<EnzymeAccession> getRelatedspecies() {
        if (relatedspecies == null) {
            relatedspecies = new ArrayList<>();
        }
        return this.relatedspecies;
    }

    /**
     * Sets the value of the relatedspecies property.
     *
     * @param relatedspecies allowed object is {@link EnzymeAccession }
     *
     */
    public void setRelatedspecies(List<EnzymeAccession> relatedspecies) {
        this.relatedspecies = relatedspecies;
    }

    @Override
    public List<String> getPdbeaccession() {
        if (pdbeaccession == null) {
            pdbeaccession = new ArrayList<>();
        }
        return this.pdbeaccession;
    }

    /**
     * Gets the value of the enzyme property.
     *
     * @return possible object is {@link Enzyme }
     *
     */
    public Enzyme getEnzyme() {
        return enzyme;
    }

    /**
     * Sets the value of the enzyme property.
     *
     * @param value allowed object is {@link Enzyme }
     *
     */
    public void setEnzyme(Enzyme value) {
        this.enzyme = value;
    }

    /**
     * Gets the value of the proteinstructure property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the proteinstructure property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProteinstructure().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProteinStructure }
     *
     *
     */
    public List<ProteinStructure> getProteinstructure() {
        if (proteinstructure == null) {
            proteinstructure = new ArrayList<ProteinStructure>();
        }
        return this.proteinstructure;
    }

    /**
     * Gets the value of the reactionpathway property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the reactionpathway property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReactionpathway().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReactionPathway }
     *
     *
     */
    public List<ReactionPathway> getReactionpathway() {
        if (reactionpathway == null) {
            reactionpathway = new ArrayList<ReactionPathway>();
        }
        return this.reactionpathway;
    }

    /**
     * Gets the value of the requestedfield property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRequestedfield() {
        return requestedfield;
    }

    /**
     * Sets the value of the requestedfield property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRequestedfield(String value) {
        this.requestedfield = value;
    }

    /**
     * Gets the value of the molecule property.
     *
     * @return possible object is {@link ChemicalEntity }
     *
     */
    public ChemicalEntity getMolecule() {
        return molecule;
    }

    /**
     * Sets the value of the molecule property.
     *
     * @param value allowed object is {@link ChemicalEntity }
     *
     */
    public void setMolecule(ChemicalEntity value) {
        this.molecule = value;
    }

    /**
     * Gets the value of the disease property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the disease property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisease().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link uk.ac.ebi.ep.enzyme.model.Disease }
     *
     *
     */
    public List<Disease> getDisease() {
        if (disease == null) {
            disease = new ArrayList<Disease>();
        }
        return this.disease;
    }

    /**
     * Gets the value of the literature property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the literature property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLiterature().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Object }
     *
     *
     */
    public List<Object> getLiterature() {
        if (literature == null) {
            literature = new ArrayList<Object>();
        }
        return this.literature;
    }

    public EnzymeModel withEnzyme(Enzyme value) {
        setEnzyme(value);
        return this;
    }

    public EnzymeModel withProteinstructure(ProteinStructure... values) {
        if (values != null) {
            for (ProteinStructure value : values) {
                getProteinstructure().add(value);
            }
        }
        return this;
    }

    public EnzymeModel withProteinstructure(Collection<ProteinStructure> values) {
        if (values != null) {
            getProteinstructure().addAll(values);
        }
        return this;
    }

    public EnzymeModel withReactionpathway(ReactionPathway... values) {
        if (values != null) {
            for (ReactionPathway value : values) {
                getReactionpathway().add(value);
            }
        }
        return this;
    }

    public EnzymeModel withReactionpathway(Collection<ReactionPathway> values) {
        if (values != null) {
            getReactionpathway().addAll(values);
        }
        return this;
    }

    public EnzymeModel withRequestedfield(String value) {
        setRequestedfield(value);
        return this;
    }

    public EnzymeModel withMolecule(ChemicalEntity value) {
        setMolecule(value);
        return this;
    }

    public EnzymeModel withDisease(Disease... values) {
        if (values != null) {
            for (Disease value : values) {
                getDisease().add(value);
            }
        }
        return this;
    }

    public EnzymeModel withDisease(Collection<Disease> values) {
        if (values != null) {
            getDisease().addAll(values);
        }
        return this;
    }

    public EnzymeModel withLiterature(Object... values) {
        if (values != null) {
            for (Object value : values) {
                getLiterature().add(value);
            }
        }
        return this;
    }

    public EnzymeModel withLiterature(Collection<Object> values) {
        if (values != null) {
            getLiterature().addAll(values);
        }
        return this;
    }

    //@Override
    public EnzymeModel withEc(String... values) {
        if (values != null) {
            for (String value : values) {
                getEc().add(value);
            }
        }
        return this;
    }

    //@Override
    public EnzymeModel withEc(Collection<String> values) {
        if (values != null) {
            getEc().addAll(values);
        }
        return this;
    }

    //@Override
    public EnzymeModel withName(String value) {
        setName(value);
        return this;
    }

    //@Override
    public EnzymeModel withFunction(String value) {
        setFunction(value);
        return this;
    }

    //@Override
    public EnzymeModel withSynonym(String... values) {
        if (values != null) {
            for (String value : values) {
                getSynonym().add(value);
            }
        }
        return this;
    }

    //@Override
    public EnzymeModel withSynonym(Collection<String> values) {
        if (values != null) {
            getSynonym().addAll(values);
        }
        return this;
    }

    //@Override
    public EnzymeModel withUniprotid(String value) {
        setUniprotid(value);
        return this;
    }

    // @Override
    public EnzymeModel withRelatedspecies(EnzymeAccession... values) {
        if (values != null) {
            for (EnzymeAccession value : values) {
                getRelatedspecies().add(value);
            }
        }
        return this;
    }

    //@Override
    public EnzymeModel withRelatedspecies(Collection<EnzymeAccession> values) {
        if (values != null) {
            getRelatedspecies().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeModel withUniprotaccessions(String... values) {
        if (values != null) {
            for (String value : values) {
                getUniprotaccessions().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeModel withUniprotaccessions(Collection<String> values) {
        if (values != null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

     @Override
    public EnzymeModel withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    @Override
    public EnzymeModel withPdbeaccession(String... values) {
        if (values != null) {
            for (String value : values) {
                getPdbeaccession().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeModel withPdbeaccession(Collection<String> values) {
        if (values != null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeModel withCompounds(Compound... values) {
        if (values != null) {
            for (Compound value : values) {
                getCompounds().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeModel withCompounds(Collection<Compound> values) {
        if (values != null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeModel withDiseases(Disease... values) {
        if (values != null) {
            for (Disease value : values) {
                getDiseases().add(value);
            }
        }
        return this;
    }

    @Override
    public EnzymeModel withDiseases(Collection<Disease> values) {
        if (values != null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeModel withScoring(Object value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the proteinstructure property.
     *
     * @param proteinstructure allowed object is {@link ProteinStructure }
     *
     */
    public void setProteinstructure(List<ProteinStructure> proteinstructure) {
        this.proteinstructure = proteinstructure;
    }

    /**
     * Sets the value of the reactionpathway property.
     *
     * @param reactionpathway allowed object is {@link ReactionPathway }
     *
     */
    public void setReactionpathway(List<ReactionPathway> reactionpathway) {
        this.reactionpathway = reactionpathway;
    }

    public void setDisease(List<Disease> disease) {
        this.disease = disease;
    }

    /**
     * Sets the value of the literature property.
     *
     * @param literature allowed object is {@link Object }
     *
     */
    public void setLiterature(List<Object> literature) {
        this.literature = literature;
    }

}
