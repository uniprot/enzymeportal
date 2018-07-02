package uk.ac.ebi.ep.download;

import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Singular;

/**
 *
 * @author Joseph
 */
//@Data
//@NoArgsConstructor
//@Builder(builderMethodName = "enzymeBuilder")
@XmlRootElement(name = "enzyme")
@XmlAccessorType(XmlAccessType.NONE)
public class EnzymeCentric {

    @Singular
    private Set<String> cofactors;
    @Singular
    private List<String> catalytic_activities;
    private String family;
    @Singular
    private Set<String> alternative_names;
    private String enzyme_name;
    private String ec;

    public EnzymeCentric() {
    }

    public EnzymeCentric(String ec) {
        this.ec = ec;
    }
    
    

    //@Singular
    //private final ProteinGroupSearchResult proteins;
    @XmlElement(name = "cofactors")
    public Set<String> getCofactors() {
        return cofactors;
    }

    public void setCofactors(Set<String> cofactors) {
        this.cofactors = cofactors;
    }

    @XmlElement(name = "catalytic_activities")
    public List<String> getCatalytic_activities() {
        return catalytic_activities;
    }

    public void setCatalytic_activities(List<String> catalytic_activities) {
        this.catalytic_activities = catalytic_activities;
    }

   @XmlElement(name = "family")
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @XmlElement(name = "alternative_names")
    public Set<String> getAlternative_names() {
        return alternative_names;
    }

    public void setAlternative_names(Set<String> alternative_names) {
        this.alternative_names = alternative_names;
    }

    @XmlElement(name = "enzyme_name")
    public String getEnzyme_name() {
        return enzyme_name;
    }

    public void setEnzyme_name(String enzyme_name) {
        this.enzyme_name = enzyme_name;
    }

   
    @XmlAttribute(name = "ec")
    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

}
