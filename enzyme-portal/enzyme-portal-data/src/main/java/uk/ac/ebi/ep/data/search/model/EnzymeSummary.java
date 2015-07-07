package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class EnzymeSummary extends EnzymeAccession  implements Comparable<EnzymeSummary>, Serializable {

    protected List<String> ec;

    private String accession;

    protected String name;
    protected String function;
    protected List<String> synonym;

    protected String uniprotid;
    protected List<EnzymeAccession> relatedspecies;

 
    private UniprotEntry uniprotAccession;
    private String commentText;
    

    public EnzymeSummary() {
    }

    public EnzymeSummary(UniprotEntry uniprotEntry) {
        this.uniprotAccession = uniprotEntry;
    }

    public EnzymeSummary(String commentText) {
        this.commentText = commentText;
    }
    
    

    public EnzymeSummary(String commentText,UniprotEntry uniprotEntry) {
        this.uniprotAccession = uniprotEntry;
        this.commentText = commentText;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }
    
    public String getCommentText() {
        return commentText;
    }


    /**
     * Gets the value of the ec property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the ec property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return
     */
    public List<String> getEc() {
        if (ec == null) {
            ec = new ArrayList<>();
        }
        return this.ec;
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

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
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
     * Gets the value of the function property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFunction(String value) {
        this.function = value;
    }

    /**
     * Gets the value of the synonym property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the synonym property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSynonym().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return
     */
    public List<String> getSynonym() {
        if (synonym == null) {
            synonym = new ArrayList<>();
        }
        return this.synonym;
       
            }
    /**
     * Sets the value of the synonym property.
     *
     * @param synonym allowed object is {@link String }
     *
     */
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    
   
    /**
     * Gets the value of the uniprotid property.
     *
     * @return possible object is {@link String }
     *
     */
    @Override
    public String getUniprotid() {
        return uniprotid;
    }

    /**
     * Sets the value of the uniprotid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    @Override
    public void setUniprotid(String value) {
        this.uniprotid = value;
    }

    /**
     * Gets the value of the relatedspecies property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the relatedspecies property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedspecies().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymeAccession }
     *
     *
     * @return
     */
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

    public EnzymeSummary withEc(String... values) {
        if (values != null) {
            getEc().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummary withEc(Collection<String> values) {
        if (values != null) {
            getEc().addAll(values);
        }
        return this;
    }

    public EnzymeSummary withName(String value) {
        setName(value);
        return this;
    }

    public EnzymeSummary withFunction(String value) {
        setFunction(value);
        return this;
    }

    public EnzymeSummary withSynonym(String... values) {
        if (values != null) {
            for (String value : values) {
                getSynonym().add(value);
            }
        }
        return this;
    }

    public EnzymeSummary withSynonym(Collection<String> values) {
        if (values != null) {
            getSynonym().addAll(values);
        }
        return this;
    }

    public EnzymeSummary withUniprotid(String value) {
        setUniprotid(value);
        return this;
    }

    public EnzymeSummary withRelatedspecies(EnzymeAccession... values) {
        if (values != null) {
            getRelatedspecies().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummary withRelatedspecies(Collection<EnzymeAccession> values) {
        if (values != null) {
            getRelatedspecies().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withUniprotaccessions(String... values) {
        if (values != null) {
            getUniprotaccessions().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withUniprotaccessions(Collection<String> values) {
        if (values != null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    @Override
    public EnzymeSummary withPdbeaccession(String... values) {
        if (values != null) {
            getPdbeaccession().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withPdbeaccession(Collection<String> values) {
        if (values != null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withCompounds(Compound... values) {
        if (values != null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withCompounds(Collection<Compound> values) {
        if (values != null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withDiseases(Disease... values) {
        if (values != null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withDiseases(Collection<Disease> values) {
        if (values != null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withScoring(Boolean value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the ec property.
     *
     * @param ec allowed object is {@link String }
     *
     */
    public void setEc(List<String> ec) {
        this.ec = ec;
    }



    @Override
    public String toString() {
        return "EnzymeSummary{" + "name=" + name + ", function=" + function + ", synonym=" + synonym + ", uniprotid=" + uniprotid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final EnzymeSummary other = (EnzymeSummary) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EnzymeSummary o) {
        if (super.scoring != null) {
            return super.scoring.toString().compareTo(o.scoring.toString());
        }
        return this.name.compareToIgnoreCase(o.getName());
    }


}
