package uk.ac.ebi.ep.ebeye.config;

/**
 *
 * @author joseph
 */
public class EbeyeIndexProps {
    private String ebeyeSearchUrl;

    private int maxEbiSearchLimit;

    private int chunkSize;

    public String getEbeyeSearchUrl() {
        return ebeyeSearchUrl;
    }

    public void setEbeyeSearchUrl(String ebeyeSearchUrl) {
        this.ebeyeSearchUrl = ebeyeSearchUrl;
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
