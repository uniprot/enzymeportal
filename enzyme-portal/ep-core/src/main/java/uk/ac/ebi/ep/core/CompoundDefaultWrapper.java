package uk.ac.ebi.ep.core;

import java.util.Comparator;

import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.search.model.Compound;

/**
 * Wrapper around a Compound object which provides the missing
 * <code>equals</code> and <code>hashCode</code> methods (the class
 * <code>Compound</code> is auto-generated from a XML schema and does not
 * provide them).
 * <br>
 * The implemented methods are based exclusively on the compound ID.
 * <br>
 * It also implements <code>Comparable</code> to provide a 'natural' order for
 * Compound objects, based on their names.
 * @author rafa
 *
 */
public class CompoundDefaultWrapper implements Comparable<CompoundDefaultWrapper> {

    private static final Comparator<String> NAME_COMPARATOR =
            new ChemicalNameComparator();

	private Compound compound;
	
	public Compound getCompound() {
		return compound;
	}

	public CompoundDefaultWrapper(Compound compound){
		if (compound == null) throw new NullPointerException();
		this.compound = compound;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + compound.getId().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CompoundDefaultWrapper other = (CompoundDefaultWrapper) obj;
		if (!compound.getId().equals(other.compound.getId()))
			return false;
		return true;
	}

	public int compareTo(CompoundDefaultWrapper other) {
	    return NAME_COMPARATOR.compare(this.compound.getName(),
	            other.compound.getName());
	}

}
