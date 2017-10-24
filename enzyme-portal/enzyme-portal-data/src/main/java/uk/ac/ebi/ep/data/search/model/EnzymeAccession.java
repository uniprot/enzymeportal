
package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author joseph
 */
public class EnzymeAccession implements Serializable {

    protected transient List<String> uniprotaccessions;

    protected Species species;
    protected transient List<String> pdbeaccession;
    protected transient List<Compound> compounds;
    protected transient List<Disease> diseases;

    protected Boolean scoring = Boolean.FALSE;
    protected Float identity = 0.0f;
    protected Integer score = 0;

    protected String uniprotid;

    protected Boolean expEvidence;
    protected String uniprotaccession;
    protected String enzymeFunction;

    protected String PDB_SOURCE = "PDB";
    protected int PDB_CODE_LIMIT = 500;
    protected int SORTED_SPECIES_LIMIT = 100;
    protected Long taxId;

    /**
     * Gets the value of the uniprotaccessions property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the uniprotaccessions property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUniprotaccessions().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return
     */
    public List<String> getUniprotaccessions() {
        if (uniprotaccessions == null) {
            uniprotaccessions = new ArrayList<>();
        }
        return this.uniprotaccessions;
    }

    /**
     * Gets the value of the species property.
     *
     * @return possible object is {@link Species }
     *
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Sets the value of the species property.
     *
     * @param value allowed object is {@link Species }
     *
     */
    public void setSpecies(Species value) {
        this.species = value;
    }

    /**
     * Gets the value of the pdbeaccession property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the pdbeaccession property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPdbeaccession().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return pdbeaccessions
     */
    public List<String> getPdbeaccession() {
        if (pdbeaccession == null) {
            pdbeaccession = new ArrayList<>();
        }
        return this.pdbeaccession;
    }

    /**
     * Gets the value of the compounds property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the compounds property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompounds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Compound }
     *
     *
     * @return
     */
    public List<Compound> getCompounds() {
        if (compounds == null) {
            compounds = new ArrayList<>();
        }
        return this.compounds;
    }

    /**
     * Gets the value of the diseases property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the diseases property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiseases().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymePortalDisease }
     *
     *
     * @return
     */
    public List<Disease> getDiseases() {
        if (diseases == null) {
            diseases = new ArrayList<>();
        }
        return this.diseases;
    }

    /**
     * Gets the value of the scoring property.
     *
     * @return possible object is {@link Object }
     *
     */
    public Boolean getScoring() {
        return scoring;
    }

    /**
     * Sets the value of the scoring property.
     *
     * @param value allowed object is {@link Object }
     *
     */
    public void setScoring(Boolean value) {
        this.scoring = value;
    }

    public Float getIdentity() {

        return identity;
    }

    public void setIdentity(Float identity) {
        this.identity = identity;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public EnzymeAccession withUniprotaccessions(String... values) {
        if (values != null) {
            getUniprotaccessions().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withUniprotaccessions(Collection<String> values) {
        if (values != null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    public EnzymeAccession withPdbeaccession(String... values) {
        if (values != null) {
            getPdbeaccession().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withPdbeaccession(Collection<String> values) {
        if (values != null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withCompounds(Compound... values) {
        if (values != null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withCompounds(Collection<Compound> values) {
        if (values != null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withDiseases(Disease... values) {
        if (values != null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withDiseases(Collection<Disease> values) {
        if (values != null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withScoring(Boolean value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the uniprotaccessions property.
     *
     * @param uniprotaccessions allowed object is {@link String }
     *
     */
    public void setUniprotaccessions(List<String> uniprotaccessions) {
        this.uniprotaccessions = uniprotaccessions;
    }

    /**
     * Sets the value of the pdbeaccession property.
     *
     * @param pdbeaccession allowed object is {@link String }
     *
     */
    public void setPdbeaccession(List<String> pdbeaccession) {
        this.pdbeaccession = pdbeaccession;
    }

    /**
     * Sets the value of the compounds property.
     *
     * @param compounds allowed object is {@link Compound }
     *
     */
    public void setCompounds(List<Compound> compounds) {
        this.compounds = compounds;
    }

    /**
     * Sets the value of the diseases property.
     *
     * @param diseases allowed object is {@link EnzymePortalDisease }
     *
     */
    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public String getUniprotid() {
        return uniprotid;
    }

    public void setUniprotid(String uniprotid) {
        this.uniprotid = uniprotid;
    }

    public Boolean getExpEvidence() {

        if (expEvidence == null) {
            expEvidence = Boolean.FALSE;
        }
        return expEvidence;
    }

    public void setExpEvidence(Boolean expEvidence) {
        this.expEvidence = expEvidence;
    }

    public String getUniprotaccession() {
        return uniprotaccession;
    }

    public void setUniprotaccession(String uniprotaccession) {
        this.uniprotaccession = uniprotaccession;
    }

    public String getEnzymeFunction() {
        return enzymeFunction;
    }

    public void setEnzymeFunction(String enzymeFunction) {
        this.enzymeFunction = enzymeFunction;
    }

    public Long getTaxId() {
        return taxId;
    }

    @Override
    public String toString() {
        return "EnzymeAccession{" + "species=" + species + ", expEvidence=" + expEvidence + ", uniprotaccession=" + uniprotaccession + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.species);
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
        final EnzymeAccession other = (EnzymeAccession) obj;
        return Objects.equals(this.species, other.species);
    }
    
    

}
