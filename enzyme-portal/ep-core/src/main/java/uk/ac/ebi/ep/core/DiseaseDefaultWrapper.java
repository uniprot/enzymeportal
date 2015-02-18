package uk.ac.ebi.ep.core;

import java.util.Comparator;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.search.model.Disease;
//import  uk.ac.ebi.ep.enzyme.model.Disease;

/**
 * Wrapper around a Disease object which provides the missing
 * <code>equals</code> and
 * <code>hashCode</code> methods (the class
 * <code>Disease</code> is auto-generated from a XML schema and does not provide
 * them). <br> The implemented methods are based exclusively on the disease Name.
 * <br> It also implements
 * <code>Comparable</code> to provide a 'natural' order for Disease objects,
 * based on their names.
 *
 * @author rafa
 *
 */
public class DiseaseDefaultWrapper implements Comparable<DiseaseDefaultWrapper> {

    private Disease disease;
    
     private static final Comparator<String> NAME_COMPARATOR =
            new ChemicalNameComparator();

    public Disease getDisease() {
        return disease;
    }

    public DiseaseDefaultWrapper(Disease disease) {
        if (disease == null) {
            throw new NullPointerException();
        }
        this.disease = disease;
    }

//    public int compareTo(DiseaseDefaultWrapper o) {
//        int result = disease.getName().compareToIgnoreCase(o.disease.getName());
//
//        return result;
//    }
    
    	public int compareTo(DiseaseDefaultWrapper o) {
	    return NAME_COMPARATOR.compare(this.disease.getName(),
	            o.disease.getName());
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.disease.getName() != null ? this.disease.getName().hashCode() : 0);
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
        final DiseaseDefaultWrapper other = (DiseaseDefaultWrapper) obj;
        if (this.disease != other.disease && (this.disease == null || !this.disease.equals(other.disease))) {
            return false;
        }
        if ((this.disease.getName() == null ? other.disease.getName() != null : !this.disease.getName().equals(other.disease.getName())) && (this.disease.getName() == null || !this.disease.getName().equalsIgnoreCase(other.disease.getName()))) {
            return false;
        }
        return true;
    }
}
