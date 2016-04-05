/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.config;

/**
 *
 * @author joseph
 */
public class EbeyeIndexUrl {

    private String defaultSearchIndexUrl;
    private String enzymeCentricSearchUrl;

    private int maxEbiSearchLimit;

    private int chunkSize;

    public String getEnzymeCentricSearchUrl() {
        return enzymeCentricSearchUrl;
    }

    public void setEnzymeCentricSearchUrl(String enzymeCentricSearchUrl) {
        this.enzymeCentricSearchUrl = enzymeCentricSearchUrl;
    }

    public String getDefaultSearchIndexUrl() {
        return defaultSearchIndexUrl;
    }

    public void setDefaultSearchIndexUrl(String defaultSearchIndexUrl) {
        this.defaultSearchIndexUrl = defaultSearchIndexUrl;
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
