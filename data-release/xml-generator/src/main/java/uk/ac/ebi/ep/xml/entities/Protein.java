package uk.ac.ebi.ep.xml.entities;

/**
 *
 * @author joseph
 */
public interface Protein {

    String getProteinGroupId();

    String getProteinName();

    String getAccession();

    long getTaxId();

    String getScientificName();

    String getCommonName();

    String getSynonymNames();

    int getExpEvidenceFlag();

    int getRelatedProteinsId();

    Short getEntryType();

    String getGeneName();

    String getPrimaryAccession();

    int getPrimaryRelatedProteinsId();

    String getOmimNumber();

    String getDiseaseName();

    String getCompoundId();

    String getCompoundName();

    String getCompoundRole();

    String getCompoundSource();

    String getReactantId();

    String getReactantName();

    String getReactantSource();

    String getEcNumber();

    Short getEcFamily();

    String getCatalyticActivity();

    String getPathwayId();

    String getPathwayName();

    String getReactionId();

    String getReactionSource();

    String getFamilyGroupId();

    String getFamilyName();

    String getChebiCompoundId();

    String getChebiCompoundName();

    String getChebiCompoundRole();

    String getChebiSynonyms();

}
