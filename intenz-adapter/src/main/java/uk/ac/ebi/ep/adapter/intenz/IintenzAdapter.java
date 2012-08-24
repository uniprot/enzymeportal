package uk.ac.ebi.ep.adapter.intenz;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IintenzAdapter {
    public static final String FAILED_MSG = "Unable to get synonyms from Intenz! ";

    public Map<String, Set<String>> getSynonyms(Set<String> ecNumbers)
            throws MultiThreadingException;

    /**
     * Get synonyms and EC classification from Intenz
     * @param enzymeModel
     * @return
     * @throws MultiThreadingException
     */
	public EnzymeModel getEnzymeDetails(EnzymeModel enzymeModel)
	throws MultiThreadingException;

	public void setConfig(IntenzConfig config);

	public IntenzConfig getConfig();

    /**
     * Retrieves cofactors for one EC number.
     * @param ec an EC number.
     * @return a collection of cofactors.
     */
	public Collection<Molecule> getCofactors(String ec);
	
	/**
	 * Retrieves cofactors for several EC numbers.
	 * @param ecs EC numbers.
	 * @return
	 */
	public Collection<Molecule> getCofactors(Collection<String> ecs);

}
