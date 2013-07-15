package uk.ac.ebi.ep.adapter.chembl;

/**
 * @author rafa
 * @since 1.0.0
 */
public class ChemblConfig implements ChemblConfigMBean {

    private String wsBaseUrl = "https://www.ebi.ac.uk/chemblws";

    private int minAssays = 5;

    private double minConf4 = 0.75;

    private double minConf9 = 0.5;

    private double minFunc = 0.5;

    public String getWsBaseUrl() {
        return wsBaseUrl;
    }

    public void setWsBaseUrl(String wsBaseUrl) {
        this.wsBaseUrl = wsBaseUrl;
    }

    public int getMinAssays() {
        return minAssays;
    }

    public void setMinAssays(int minAssays) {
        this.minAssays = minAssays;
    }

    public double getMinConf4() {
        return minConf4;
    }

    public void setMinConf4(double minConf4) {
        this.minConf4 = minConf4;
    }

    public double getMinConf9() {
        return minConf9;
    }

    public void setMinConf9(double minConf9) {
        this.minConf9 = minConf9;
    }

    public double getMinFunc() {
        return minFunc;
    }

    public void setMinFunc(double minFunc) {
        this.minFunc = minFunc;
    }
}
