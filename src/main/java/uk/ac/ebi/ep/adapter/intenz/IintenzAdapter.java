package uk.ac.ebi.ep.adapter.intenz;

import java.util.Map;
import java.util.Set;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
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

}
