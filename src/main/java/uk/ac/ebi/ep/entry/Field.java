package uk.ac.ebi.ep.entry;

/**
 * Field to show for an enzyme in the portal.
 * @author rafa
 *
 */
public enum Field {
	/**
	 * The minimal information to handle result lists and filters internally.
	 */
	minimal,
    /**
     * Just a summary to show in the results page.
     */
    brief,
    enzyme,
    proteinStructure,
    reactionsPathways,
    molecules,
    diseaseDrugs,
    literature,
    full
}