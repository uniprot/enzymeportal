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
