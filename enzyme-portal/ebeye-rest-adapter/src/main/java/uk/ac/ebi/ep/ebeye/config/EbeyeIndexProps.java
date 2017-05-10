package uk.ac.ebi.ep.ebeye.config;

/**
 *
 * @author joseph
 */
public class EbeyeIndexProps {

    private String proteinCentricSearchUrl;
    private String enzymeCentricSearchUrl;
    private String proteinGroupSearchUrl;

    private int maxEbiSearchLimit;

    private int chunkSize;

    public String getProteinGroupSearchUrl() {
        return proteinGroupSearchUrl;
    }

    public void setProteinGroupSearchUrl(String proteinGroupSearchUrl) {
        this.proteinGroupSearchUrl = proteinGroupSearchUrl;
    }

    public String getEnzymeCentricSearchUrl() {
        return enzymeCentricSearchUrl;
    }

    public void setEnzymeCentricSearchUrl(String enzymeCentricSearchUrl) {
        this.enzymeCentricSearchUrl = enzymeCentricSearchUrl;
    }

    public String getProteinCentricSearchUrl() {
        return proteinCentricSearchUrl;
    }

    public void setProteinCentricSearchUrl(String proteinCentricSearchUrl) {
        this.proteinCentricSearchUrl = proteinCentricSearchUrl;
    }

    public int getMaxEbiSearchLimit() {
        return maxEbiSearchLimit;
    }

    public void setMaxEbiSearchLimit(int maxEbiSearchLimit) {
        this.maxEbiSearchLimit = maxEbiSearchLimit;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

}
