package uk.ac.ebi.ep.core;

import uk.ac.ebi.ep.search.model.Disease;

/**
 * Wrapper around a Disease object which provides the missing
 * <code>equals</code> and <code>hashCode</code> methods (the class
 * <code>Disease</code> is auto-generated from a XML schema and does not
 * provide them).
 * <br>
 * The implemented methods are based exclusively on the disease ID.
 * <br>
 * It also implements <code>Comparable</code> to provide a 'natural' order for
 * Disease objects, based on their names.
 * @author rafa
 *
 */
public class DiseaseDefaultWrapper implements Comparable<DiseaseDefaultWrapper> {

	private Disease disease;
	
	public Disease getDisease(){
		return disease;
	}
	
	public DiseaseDefaultWrapper(Disease disease) {
		if (disease == null) throw new NullPointerException();
		this.disease = disease;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + disease.getId().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DiseaseDefaultWrapper other = (DiseaseDefaultWrapper) obj;
		if (!disease.getId().equals(other.disease.getId()))
			return false;
		return true;
	}

	public int compareTo(DiseaseDefaultWrapper o) {
		return disease.getName().compareTo(o.disease.getName());
	}

}
