package uk.ac.ebi.ep.adapter.chembl;

import java.text.MessageFormat;

/**
 * Bioactivity of a compound against a target, as measured by the number of
 * assays and how many of them are functional and the degree of confidence.<br/>
 * See CONFIDENCE_SCORE_LOOKUP and ASSAY_TYPE tables in ChEMBL
 * <a href="ftp://ftp.ebi.ac.uk/pub/databases/chembl/ChEMBLdb/releases/chembl_16/chembl_16_schema_documentation.html">database</a>.
 * @author rafa
 * @since 1.0.0
 */
public class ChemblBioactivity {

    /**
     * The number of assays relating the same compound and same target.
     */
    private int assays;

    /**
     * The number of assays for which conficence is at least 4 ("Multiple
     * homologous protein targets may be assigned").
     */
    private int conf4;

    /**
     * The number of assays for which conficence is at least 4 ("Direct single
     * protein target assigned").
     */
    private int conf9;

    /**
     * The number of assays for which the assay type is functional.
     */
    private int funct;

    public void addAssay(){
        assays++;
    }

    public void addConf4(){
        conf4++;
    }

    public void addConf9(){
        conf9++;
    }

    public void addFunct(){
        funct++;
    }

    public int getAssays() {
        return assays;
    }

    public int getConf4() {
        return conf4;
    }

    public int getConf9() {
        return conf9;
    }

    public int getFunct() {
        return funct;
    }

    public double getFractionConf4() {
        return (double) conf4 / assays;
    }

    public double getFractionConf9() {
        return (double) conf9 / assays;
    }

    public double getFractionFunc() {
        return (double) funct / assays;
    }

    @Override
    public String toString() {
        final String PERC = "{0,number,#.##%}";
        StringBuilder sb = new StringBuilder()
                .append(assays).append(" assays: ")
                .append(MessageFormat.format(PERC, getFractionConf4()))
                .append(" >= conf4; ")
                .append(MessageFormat.format(PERC, getFractionConf9()))
                .append(" = conf9; ")
                .append(MessageFormat.format(PERC, getFractionFunc()))
                .append(" F");
        return sb.toString();
    }
}
