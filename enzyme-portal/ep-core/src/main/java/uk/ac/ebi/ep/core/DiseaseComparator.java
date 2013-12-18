/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.core;

import java.util.Comparator;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.enzyme.model.Disease;

/**
 *
 * @author joseph
 */
public class DiseaseComparator implements Comparable<DiseaseComparator> {
    
    private uk.ac.ebi.ep.enzyme.model.Disease disease;
    
       private static final Comparator<String> NAME_COMPARATOR =
            new ChemicalNameComparator();

    public DiseaseComparator(Disease disease) {
        this.disease = disease;
    }

    public Disease getDisease() {
        return disease;
    }
    


//    public int compareTo(DiseaseComparator o) {
//        int result = disease.getName().compareToIgnoreCase(o.disease.getName());
//        return result;
//    }
    
     	public int compareTo(DiseaseComparator o) {
	    return NAME_COMPARATOR.compare(this.disease.getName(),
	            o.disease.getName());
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.disease.getName() != null ? this.disease.getName().hashCode() : 0);
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
        final DiseaseComparator other = (DiseaseComparator) obj;
        if (this.disease != other.disease && (this.disease == null || !this.disease.equals(other.disease))) {
            return false;
        }
          if ((this.disease.getName() == null ? other.disease.getName() != null : !this.disease.getName().equals(other.disease.getName())) && (this.disease.getName() == null || !this.disease.getName().equalsIgnoreCase(other.disease.getName()))) {
            return false;
        }
        return true;
    }

 
    
    
}
