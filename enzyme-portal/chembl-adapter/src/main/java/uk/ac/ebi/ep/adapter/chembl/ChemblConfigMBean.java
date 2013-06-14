package uk.ac.ebi.ep.adapter.chembl;

/**
 * @author rafa
 * @since 2013-06-13
 */
public interface ChemblConfigMBean {

    String getWsBaseUrl();

    /**
     * Sets the base URL for the ChEMBL web service.
     * @param wsBaseUrl the base URL.
     */
    void setWsBaseUrl(String wsBaseUrl);

    int getMinAssays();

    /**
     * Sets the minimum number of assays required to consider a bioactivity.
     * @see {@link ChemblBioactivities#filter(int, double, double, double)}
     * @param minAssays the minimum number of assays reported for the pair
     *      target-compound.
     */
    void setMinAssays(int minAssays);

    double getMinConf4();

    /**
     * Sets the minimum fraction of assays which must have a confidence level of
     * 4 or more in order to consider a bioactivity.
     * @param minConf4 the minimum fraction of assays with confidence level 4 or
     *      higher.
     */
    void setMinConf4(double minConf4);

    double getMinConf9();

    /**
     * Sets the minimum fraction of assays which must have a confidence level of
     * 9 in order to consider a bioactivity.
     * @param minConf9 the minimum fraction of assays with confidence level 9.
     */
    void setMinConf9(double minConf9);

    double getMinFunc();

    /**
     * Sets the minimum fraction of assays which must be functional ones in
     * order to consider a bioactivity.
     * @param minFunc the minimum fraction of functional assays.
     */
    void setMinFunc(double minFunc);
}
