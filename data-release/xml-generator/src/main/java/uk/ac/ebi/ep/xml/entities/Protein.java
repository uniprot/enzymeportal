package uk.ac.ebi.ep.xml.entities;

/**
 *
 * @author joseph
 */
public interface Protein {

    String getAccession();

    String getCommonName();

    String getScientificName();

    String getProteinGroupId();

    String getProteinName();

    public long getTaxId();

    public String getSynonymNames();

    public int getExpEvidenceFlag();

    public int getRelatedProteinsId();

    public Short getEntryType();

    public String getGeneName();

    public String getPrimaryAccession();
    // public char getPdbFlag();

//    public int getPrimaryTaxId();
//
//    public String getPrimaryCommonName();
//
//    public String getPrimaryScientificName();
//
//    public String getPrimaryPdbId();
//
//    public String getPrimaryFunction();
//
//    public String getPrimaryPdbSpecies();
//
//    public String getPrimaryPdbLinkedAcc();
//
//    public int getPrimaryEntryType();

    public int getPrimaryRelatedProteinsId();

    public String getOmimNumber();

    public String getDiseaseName();

    public String getCompoundId();

    public String getCompoundName();

    public String getCompoundRole();

    public String getCompoundSource();

    public String getReactantId();

    public String getReactantName();

    public String getReactantSource();

    public String getEcNumber();

    public Short getEcFamily();

    public String getCatalyticActivity();

    public String getPathwayId();

    public String getPathwayName();

    public String getReactionId();

    public String getReactionSource();

    public String getFamilyGroupId();

    public String getFamilyName();

}
