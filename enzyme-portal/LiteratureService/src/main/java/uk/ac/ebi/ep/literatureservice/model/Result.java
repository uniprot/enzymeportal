/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.literatureservice.model;

import uk.ac.ebi.ep.literatureservice.model.DbCrossReferenceList;
import uk.ac.ebi.ep.literatureservice.model.GrantsList;
import uk.ac.ebi.ep.literatureservice.model.ChemicalList;
import uk.ac.ebi.ep.literatureservice.model.FullTextUrlList;
import uk.ac.ebi.ep.literatureservice.model.AuthorList;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "pmid",
    "title",
    "authorString",
    "authorList",
    "journalInfo",
    "pageInfo",
    "abstractText",
    "affiliation",
    "language",
    "pubModel",
    "pubTypeList",
    "grantsList",
    "meshHeadingList",
    "keywordList",
    "chemicalList",
    "subsetList",
    "fullTextUrlList",
    "inEPMC",
    "inPMC",
    "citedByCount",
    "hasReferences",
    "hasTextMinedTerms",
    "hasDbCrossReferences",
    "dbCrossReferenceList",
    "hasLabsLinks",
    "hasTMAccessionNumbers",
    "dateOfCompletion",
    "dateOfCreation",
    "electronicPublicationDate",
    "firstPublicationDate",
    "luceneScore",
    "hasBook",
    "doi"
})

/**
 *
 * @author joseph
 */
public class Result {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("pmid")
    private String pmid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("authorString")
    private String authorString;
    @JsonProperty("authorList")
    private AuthorList authorList;
    @JsonProperty("journalInfo")
    private JournalInfo journalInfo;
    @JsonProperty("pageInfo")
    private String pageInfo;
    @JsonProperty("abstractText")
    private String abstractText;
    @JsonProperty("affiliation")
    private String affiliation;
    @JsonProperty("language")
    private String language;
    @JsonProperty("pubModel")
    private String pubModel;
    @JsonProperty("pubTypeList")
    private PubTypeList pubTypeList;
    @JsonProperty("grantsList")
    private GrantsList grantsList;
    @JsonProperty("meshHeadingList")
    private MeshHeadingList meshHeadingList;
    @JsonProperty("keywordList")
    private KeywordList keywordList;
    @JsonProperty("chemicalList")
    private ChemicalList chemicalList;
    @JsonProperty("subsetList")
    private SubsetList subsetList;
    @JsonProperty("fullTextUrlList")
    private FullTextUrlList fullTextUrlList;
    @JsonProperty("inEPMC")
    private String inEPMC;
    @JsonProperty("inPMC")
    private String inPMC;
    @JsonProperty("citedByCount")
    private Integer citedByCount;
    @JsonProperty("hasReferences")
    private String hasReferences;
    @JsonProperty("hasTextMinedTerms")
    private String hasTextMinedTerms;
    @JsonProperty("hasDbCrossReferences")
    private String hasDbCrossReferences;
    @JsonProperty("dbCrossReferenceList")
    private DbCrossReferenceList dbCrossReferenceList;
    @JsonProperty("hasLabsLinks")
    private String hasLabsLinks;
    @JsonProperty("hasTMAccessionNumbers")
    private String hasTMAccessionNumbers;
    @JsonProperty("dateOfCompletion")
    private String dateOfCompletion;
    @JsonProperty("dateOfCreation")
    private String dateOfCreation;
    @JsonProperty("electronicPublicationDate")
    private String electronicPublicationDate;
    @JsonProperty("firstPublicationDate")
    private String firstPublicationDate;
    @JsonProperty("luceneScore")
    private String luceneScore;
    @JsonProperty("hasBook")
    private String hasBook;
    @JsonProperty("doi")
    private String doi;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return The pmid
     */
    @JsonProperty("pmid")
    public String getPmid() {
        return pmid;
    }

    /**
     *
     * @param pmid The pmid
     */
    @JsonProperty("pmid")
    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    /**
     *
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return The authorString
     */
    @JsonProperty("authorString")
    public String getAuthorString() {
        return authorString;
    }

    /**
     *
     * @param authorString The authorString
     */
    @JsonProperty("authorString")
    public void setAuthorString(String authorString) {
        this.authorString = authorString;
    }

    /**
     *
     * @return The authorList
     */
    @JsonProperty("authorList")
    public AuthorList getAuthorList() {
        return authorList;
    }

    /**
     *
     * @param authorList The authorList
     */
    @JsonProperty("authorList")
    public void setAuthorList(AuthorList authorList) {
        this.authorList = authorList;
    }

    /**
     *
     * @return The journalInfo
     */
    @JsonProperty("journalInfo")
    public JournalInfo getJournalInfo() {
        return journalInfo;
    }

    /**
     *
     * @param journalInfo The journalInfo
     */
    @JsonProperty("journalInfo")
    public void setJournalInfo(JournalInfo journalInfo) {
        this.journalInfo = journalInfo;
    }

    /**
     *
     * @return The pageInfo
     */
    @JsonProperty("pageInfo")
    public String getPageInfo() {
        return pageInfo;
    }

    /**
     *
     * @param pageInfo The pageInfo
     */
    @JsonProperty("pageInfo")
    public void setPageInfo(String pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     *
     * @return The abstractText
     */
    @JsonProperty("abstractText")
    public String getAbstractText() {
        return abstractText;
    }

    /**
     *
     * @param abstractText The abstractText
     */
    @JsonProperty("abstractText")
    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    /**
     *
     * @return The affiliation
     */
    @JsonProperty("affiliation")
    public String getAffiliation() {
        return affiliation;
    }

    /**
     *
     * @param affiliation The affiliation
     */
    @JsonProperty("affiliation")
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    /**
     *
     * @return The language
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language The language
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return The pubModel
     */
    @JsonProperty("pubModel")
    public String getPubModel() {
        return pubModel;
    }

    /**
     *
     * @param pubModel The pubModel
     */
    @JsonProperty("pubModel")
    public void setPubModel(String pubModel) {
        this.pubModel = pubModel;
    }

    /**
     *
     * @return The pubTypeList
     */
    @JsonProperty("pubTypeList")
    public PubTypeList getPubTypeList() {
        return pubTypeList;
    }

    /**
     *
     * @param pubTypeList The pubTypeList
     */
    @JsonProperty("pubTypeList")
    public void setPubTypeList(PubTypeList pubTypeList) {
        this.pubTypeList = pubTypeList;
    }

    /**
     *
     * @return The grantsList
     */
    @JsonProperty("grantsList")
    public GrantsList getGrantsList() {
        return grantsList;
    }

    /**
     *
     * @param grantsList The grantsList
     */
    @JsonProperty("grantsList")
    public void setGrantsList(GrantsList grantsList) {
        this.grantsList = grantsList;
    }

    /**
     *
     * @return The meshHeadingList
     */
    @JsonProperty("meshHeadingList")
    public MeshHeadingList getMeshHeadingList() {
        return meshHeadingList;
    }

    /**
     *
     * @param meshHeadingList The meshHeadingList
     */
    @JsonProperty("meshHeadingList")
    public void setMeshHeadingList(MeshHeadingList meshHeadingList) {
        this.meshHeadingList = meshHeadingList;
    }

    /**
     *
     * @return The keywordList
     */
    @JsonProperty("keywordList")
    public KeywordList getKeywordList() {
        return keywordList;
    }

    /**
     *
     * @param keywordList The keywordList
     */
    @JsonProperty("keywordList")
    public void setKeywordList(KeywordList keywordList) {
        this.keywordList = keywordList;
    }

    /**
     *
     * @return The chemicalList
     */
    @JsonProperty("chemicalList")
    public ChemicalList getChemicalList() {
        return chemicalList;
    }

    /**
     *
     * @param chemicalList The chemicalList
     */
    @JsonProperty("chemicalList")
    public void setChemicalList(ChemicalList chemicalList) {
        this.chemicalList = chemicalList;
    }

    /**
     *
     * @return The subsetList
     */
    @JsonProperty("subsetList")
    public SubsetList getSubsetList() {
        return subsetList;
    }

    /**
     *
     * @param subsetList The subsetList
     */
    @JsonProperty("subsetList")
    public void setSubsetList(SubsetList subsetList) {
        this.subsetList = subsetList;
    }

    /**
     *
     * @return The fullTextUrlList
     */
    @JsonProperty("fullTextUrlList")
    public FullTextUrlList getFullTextUrlList() {
        return fullTextUrlList;
    }

    /**
     *
     * @param fullTextUrlList The fullTextUrlList
     */
    @JsonProperty("fullTextUrlList")
    public void setFullTextUrlList(FullTextUrlList fullTextUrlList) {
        this.fullTextUrlList = fullTextUrlList;
    }

    /**
     *
     * @return The inEPMC
     */
    @JsonProperty("inEPMC")
    public String getInEPMC() {
        return inEPMC;
    }

    /**
     *
     * @param inEPMC The inEPMC
     */
    @JsonProperty("inEPMC")
    public void setInEPMC(String inEPMC) {
        this.inEPMC = inEPMC;
    }

    /**
     *
     * @return The inPMC
     */
    @JsonProperty("inPMC")
    public String getInPMC() {
        return inPMC;
    }

    /**
     *
     * @param inPMC The inPMC
     */
    @JsonProperty("inPMC")
    public void setInPMC(String inPMC) {
        this.inPMC = inPMC;
    }

    /**
     *
     * @return The citedByCount
     */
    @JsonProperty("citedByCount")
    public Integer getCitedByCount() {
        return citedByCount;
    }

    /**
     *
     * @param citedByCount The citedByCount
     */
    @JsonProperty("citedByCount")
    public void setCitedByCount(Integer citedByCount) {
        this.citedByCount = citedByCount;
    }

    /**
     *
     * @return The hasReferences
     */
    @JsonProperty("hasReferences")
    public String getHasReferences() {
        return hasReferences;
    }

    /**
     *
     * @param hasReferences The hasReferences
     */
    @JsonProperty("hasReferences")
    public void setHasReferences(String hasReferences) {
        this.hasReferences = hasReferences;
    }

    /**
     *
     * @return The hasTextMinedTerms
     */
    @JsonProperty("hasTextMinedTerms")
    public String getHasTextMinedTerms() {
        return hasTextMinedTerms;
    }

    /**
     *
     * @param hasTextMinedTerms The hasTextMinedTerms
     */
    @JsonProperty("hasTextMinedTerms")
    public void setHasTextMinedTerms(String hasTextMinedTerms) {
        this.hasTextMinedTerms = hasTextMinedTerms;
    }

    /**
     *
     * @return The hasDbCrossReferences
     */
    @JsonProperty("hasDbCrossReferences")
    public String getHasDbCrossReferences() {
        return hasDbCrossReferences;
    }

    /**
     *
     * @param hasDbCrossReferences The hasDbCrossReferences
     */
    @JsonProperty("hasDbCrossReferences")
    public void setHasDbCrossReferences(String hasDbCrossReferences) {
        this.hasDbCrossReferences = hasDbCrossReferences;
    }

    /**
     *
     * @return The dbCrossReferenceList
     */
    @JsonProperty("dbCrossReferenceList")
    public DbCrossReferenceList getDbCrossReferenceList() {
        return dbCrossReferenceList;
    }

    /**
     *
     * @param dbCrossReferenceList The dbCrossReferenceList
     */
    @JsonProperty("dbCrossReferenceList")
    public void setDbCrossReferenceList(DbCrossReferenceList dbCrossReferenceList) {
        this.dbCrossReferenceList = dbCrossReferenceList;
    }

    /**
     *
     * @return The hasLabsLinks
     */
    @JsonProperty("hasLabsLinks")
    public String getHasLabsLinks() {
        return hasLabsLinks;
    }

    /**
     *
     * @param hasLabsLinks The hasLabsLinks
     */
    @JsonProperty("hasLabsLinks")
    public void setHasLabsLinks(String hasLabsLinks) {
        this.hasLabsLinks = hasLabsLinks;
    }

    /**
     *
     * @return The hasTMAccessionNumbers
     */
    @JsonProperty("hasTMAccessionNumbers")
    public String getHasTMAccessionNumbers() {
        return hasTMAccessionNumbers;
    }

    /**
     *
     * @param hasTMAccessionNumbers The hasTMAccessionNumbers
     */
    @JsonProperty("hasTMAccessionNumbers")
    public void setHasTMAccessionNumbers(String hasTMAccessionNumbers) {
        this.hasTMAccessionNumbers = hasTMAccessionNumbers;
    }

    /**
     *
     * @return The dateOfCompletion
     */
    @JsonProperty("dateOfCompletion")
    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    /**
     *
     * @param dateOfCompletion The dateOfCompletion
     */
    @JsonProperty("dateOfCompletion")
    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    /**
     *
     * @return The dateOfCreation
     */
    @JsonProperty("dateOfCreation")
    public String getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     *
     * @param dateOfCreation The dateOfCreation
     */
    @JsonProperty("dateOfCreation")
    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     *
     * @return The electronicPublicationDate
     */
    @JsonProperty("electronicPublicationDate")
    public String getElectronicPublicationDate() {
        return electronicPublicationDate;
    }

    /**
     *
     * @param electronicPublicationDate The electronicPublicationDate
     */
    @JsonProperty("electronicPublicationDate")
    public void setElectronicPublicationDate(String electronicPublicationDate) {
        this.electronicPublicationDate = electronicPublicationDate;
    }

    /**
     *
     * @return The firstPublicationDate
     */
    @JsonProperty("firstPublicationDate")
    public String getFirstPublicationDate() {
        return firstPublicationDate;
    }

    /**
     *
     * @param firstPublicationDate The firstPublicationDate
     */
    @JsonProperty("firstPublicationDate")
    public void setFirstPublicationDate(String firstPublicationDate) {
        this.firstPublicationDate = firstPublicationDate;
    }

    /**
     *
     * @return The luceneScore
     */
    @JsonProperty("luceneScore")
    public String getLuceneScore() {
        return luceneScore;
    }

    /**
     *
     * @param luceneScore The luceneScore
     */
    @JsonProperty("luceneScore")
    public void setLuceneScore(String luceneScore) {
        this.luceneScore = luceneScore;
    }

    /**
     *
     * @return The hasBook
     */
    @JsonProperty("hasBook")
    public String getHasBook() {
        return hasBook;
    }

    /**
     *
     * @param hasBook The hasBook
     */
    @JsonProperty("hasBook")
    public void setHasBook(String hasBook) {
        this.hasBook = hasBook;
    }

    /**
     *
     * @return The doi
     */
    @JsonProperty("doi")
    public String getDoi() {
        return doi;
    }

    /**
     *
     * @param doi The doi
     */
    @JsonProperty("doi")
    public void setDoi(String doi) {
        this.doi = doi;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
