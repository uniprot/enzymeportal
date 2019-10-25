package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import uk.ac.ebi.ep.data.domain.ReactionMechanism;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;

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
public class EnzymeModel extends UniprotEntry implements Serializable {

    protected Enzyme enzyme;
    transient List<ProteinStructure> proteinstructure;
    transient List<ReactionPathway> reactionpathway;
    transient List<ReactionMechanism> reactionMechanisms;
    transient List<EnzymeReaction> enzymeReactions;

    protected String requestedfield;

    protected ChemicalEntity molecule;
    transient List<Disease> disease;

    transient List<Object> literature;

    transient List<Pathway> pathways;
    transient List<EnzymeAccession> relatedspecies;
    transient List<String> synonyms;

    transient List<String> catalyticActivities;

    List<String> ec;
    protected transient int numReactions;

    public int getNumReactions() {
        return numReactions;
    }

    public void setNumReactions(int numReactions) {
        this.numReactions = numReactions;
    }

    public List<ReactionMechanism> getReactionMechanisms() {
        if (reactionMechanisms == null) {
            reactionMechanisms = new ArrayList<>();
        }

        return reactionMechanisms;
    }

    public void setReactionMechanisms(List<ReactionMechanism> reactionMechanisms) {
        this.reactionMechanisms = reactionMechanisms;
    }

    public List<EnzymeReaction> getEnzymeReactions() {
        if (enzymeReactions == null) {
            enzymeReactions = new ArrayList<>();
        }

        return enzymeReactions;
    }

    public void setEnzymeReactions(List<EnzymeReaction> enzymeReactions) {
        this.enzymeReactions = enzymeReactions;
    }

    public List<String> getCatalyticActivities() {
        if (catalyticActivities == null) {
            catalyticActivities = new LinkedList<>();
        }

        return catalyticActivities;
    }

    public void setCatalyticActivities(List<String> catalyticActivities) {
        this.catalyticActivities = catalyticActivities;
    }

    public List<String> getSynonyms() {
        if (synonyms == null) {
            synonyms = new ArrayList<>();
        }

        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<Pathway> getPathways() {
        if (pathways == null) {
            pathways = new ArrayList<>();
        }
        return pathways;//.stream().distinct().collect(Collectors.toList());
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

    // @Override
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
     * @return protein structure
     */
    public List<ProteinStructure> getProteinstructure() {
        if (proteinstructure == null) {
            proteinstructure = new ArrayList<>();
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
     * @return reaction pathway
     */
    public List<ReactionPathway> getReactionpathway() {
        if (reactionpathway == null) {
            reactionpathway = new ArrayList<>();
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
     * {@link uk.ac.ebi.ep.data.enzyme.model.Disease }
     *
     *
     * @return
     */
    public List<Disease> getDisease() {
        if (disease == null) {
            disease = new ArrayList<>();
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
     * @return
     */
    public List<Object> getLiterature() {
        if (literature == null) {
            literature = new ArrayList<>();
        }
        return this.literature;
    }

//    public EnzymeModel withEnzyme(Enzyme value) {
//        setEnzyme(value);
//        return this;
//    }
//
//    public EnzymeModel withProteinstructure(ProteinStructure... values) {
//        if (values != null) {
//            for (ProteinStructure value : values) {
//                getProteinstructure().add(value);
//            }
//        }
//        return this;
//    }
//
//    public EnzymeModel withProteinstructure(Collection<ProteinStructure> values) {
//        if (values != null) {
//            getProteinstructure().addAll(values);
//        }
//        return this;
//    }
//
//    public EnzymeModel withReactionpathway(ReactionPathway... values) {
//        if (values != null) {
//            for (ReactionPathway value : values) {
//                getReactionpathway().add(value);
//            }
//        }
//        return this;
//    }
//
//    public EnzymeModel withReactionpathway(Collection<ReactionPathway> values) {
//        if (values != null) {
//            getReactionpathway().addAll(values);
//        }
//        return this;
//    }
//
//    public EnzymeModel withRequestedfield(String value) {
//        setRequestedfield(value);
//        return this;
//    }
//
//    public EnzymeModel withMolecule(ChemicalEntity value) {
//        setMolecule(value);
//        return this;
//    }
//
//    public EnzymeModel withDisease(Disease... values) {
//        if (values != null) {
//            getDisease().addAll(Arrays.asList(values));
//        }
//        return this;
//    }
//
//    public EnzymeModel withDisease(Collection<Disease> values) {
//        if (values != null) {
//            getDisease().addAll(values);
//        }
//        return this;
//    }
//
//    public EnzymeModel withLiterature(Object... values) {
//        if (values != null) {
//            getLiterature().addAll(Arrays.asList(values));
//        }
//        return this;
//    }
//
//    public EnzymeModel withLiterature(Collection<Object> values) {
//        if (values != null) {
//            getLiterature().addAll(values);
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withEc(String... values) {
//        if (values != null) {
//            //getEc().addAll(Arrays.asList(values));
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withEc(Collection<String> values) {
//        if (values != null) {
//           // getEc().addAll(values);
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withName(String value) {
//        //setName(value);
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withFunction(String value) {
//        //setFunction(value);
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withSynonym(String... values) {
//        if (values != null) {
//           // getSynonym().addAll(Arrays.asList(values));
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withSynonym(Collection<String> values) {
//        if (values != null) {
//            //getSynonym().addAll(values);
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withUniprotid(String value) {
//        //setUniprotid(value);
//        return this;
//    }
//
//    // @Override
//    public EnzymeModel withRelatedspecies(EnzymeAccession... values) {
//        if (values != null) {
//            for (EnzymeAccession value : values) {
//                getRelatedspecies().add(value);
//            }
//        }
//        return this;
//    }
//
//    //@Override
//    public EnzymeModel withRelatedspecies(Collection<EnzymeAccession> values) {
//        if (values != null) {
//            getRelatedspecies().addAll(values);
//        }
//        return this;
//    }
//    @Override
//    public EnzymeModel withUniprotaccessions(String... values) {
//        if (values != null) {
//            for (String value : values) {
//                getUniprotaccessions().add(value);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withUniprotaccessions(Collection<String> values) {
//        if (values != null) {
//            getUniprotaccessions().addAll(values);
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withSpecies(Species value) {
//        setSpecies(value);
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withPdbeaccession(String... values) {
//        if (values != null) {
//            for (String value : values) {
//                getPdbeaccession().add(value);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withPdbeaccession(Collection<String> values) {
//        if (values != null) {
//            getPdbeaccession().addAll(values);
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withCompounds(Compound... values) {
//        if (values != null) {
//            for (Compound value : values) {
//                getCompounds().add(value);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withCompounds(Collection<Compound> values) {
//        if (values != null) {
//            getCompounds().addAll(values);
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withDiseases(Disease... values) {
//        if (values != null) {
//            for (Disease value : values) {
//                getDiseases().add(value);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withDiseases(Collection<Disease> values) {
//        if (values != null) {
//            getDiseases().addAll(values);
//        }
//        return this;
//    }
//
//    @Override
//    public EnzymeModel withScoring(Boolean value) {
//        setScoring(value);
//        return this;
//    }
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

    /////refactoring starts here
//    public List<String> getUniprotaccessions() {
//        if (uniprotaccessions == null) {
//            uniprotaccessions = new ArrayList<>();
//        }
//
//        return uniprotaccessions;
//    }
//
//    public void setUniprotaccessions(List<String> uniprotaccessions) {
//        this.uniprotaccessions = uniprotaccessions;
//    }
//
//    public Species getSpecies() {
//        return species;
//    }
//
//    public void setSpecies(Species species) {
//        this.species = species;
//    }
//
//    public BigInteger getExpEvidenceFlag() {
//        return expEvidenceFlag;
//    }
//
//    public void setExpEvidenceFlag(BigInteger expEvidenceFlag) {
//        this.expEvidenceFlag = expEvidenceFlag;
//    }
//
//    public String getFunction() {
//        return function;
//    }
//
//    public void setFunction(String function) {
//        this.function = function;
//    }
//
//    public String getSynonymNames() {
//        return synonymNames;
//    }
//
//    public void setSynonymNames(String synonymNames) {
//        this.synonymNames = synonymNames;
//    }
//
//    public String getAccession() {
//        return accession;
//    }
//
//    public void setAccession(String accession) {
//        this.accession = accession;
//    }
//
//    public Long getTaxId() {
//        return taxId;
//    }
//
//    public void setTaxId(Long taxId) {
//        this.taxId = taxId;
//    }
//
//    public String getProteinName() {
//        return proteinName;
//    }
//
//    public void setProteinName(String proteinName) {
//        this.proteinName = proteinName;
//    }
//
//    public String getScientificName() {
//        return scientificName;
//    }
//
//    public void setScientificName(String scientificName) {
//        this.scientificName = scientificName;
//    }
//
//    public String getCommonName() {
//        return commonName;
//    }
//
//    public void setCommonName(String commonName) {
//        this.commonName = commonName;
//    }
//
//    public String getUniprotid() {
//
//        return uniprotid;
//    }
//
//    public void setUniprotid(String uniprotid) {
//        this.uniprotid = uniprotid;
//    }
//
//    public List<String> getEc() {
//        if (ec == null) {
//            ec = new ArrayList<>();
//        }
//
//        return ec;
//    }
//
//    public void setEc(List<String> ec) {
//        this.ec = ec;
//    }
//
//    public BigInteger getFunctionLength() {
//        return functionLength;
//    }
//
//    public void setFunctionLength(BigInteger functionLength) {
//        this.functionLength = functionLength;
//    }
//
//    public String getEnzymeFunction() {
//        return enzymeFunction;
//    }
//
//    public void setEnzymeFunction(String enzymeFunction) {
//        this.enzymeFunction = enzymeFunction;
//    }
//
//    public Short getEntryType() {
//        return entryType;
//    }
//
//    public void setEntryType(Short entryType) {
//        this.entryType = entryType;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    
}
