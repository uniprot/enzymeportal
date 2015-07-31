/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.eupmc;



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
"pmcid",
"title",
"authorString",
"journalTitle",
"issue",
"journalVolume",
"pubYear",
"journalIssn",
"pageInfo",
"pubType",
"isOpenAccess",
"inEPMC",
"inPMC",
"citedByCount",
"hasReferences",
"hasTextMinedTerms",
"hasDbCrossReferences",
"hasLabsLinks",
"hasTMAccessionNumbers",
"luceneScore",
"doi",
"dbCrossReferenceList",
"tmAccessionTypeList"
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
@JsonProperty("pmcid")
private String pmcid;
@JsonProperty("title")
private String title;
@JsonProperty("authorString")
private String authorString;
@JsonProperty("journalTitle")
private String journalTitle;
@JsonProperty("issue")
private String issue;
@JsonProperty("journalVolume")
private String journalVolume;
@JsonProperty("pubYear")
private String pubYear;
@JsonProperty("journalIssn")
private String journalIssn;
@JsonProperty("pageInfo")
private String pageInfo;
@JsonProperty("pubType")
private String pubType;
@JsonProperty("isOpenAccess")
private String isOpenAccess;
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
@JsonProperty("hasLabsLinks")
private String hasLabsLinks;
@JsonProperty("hasTMAccessionNumbers")
private String hasTMAccessionNumbers;
@JsonProperty("luceneScore")
private String luceneScore;
@JsonProperty("doi")
private String doi;
@JsonProperty("dbCrossReferenceList")
private DbCrossReferenceList dbCrossReferenceList;
@JsonProperty("tmAccessionTypeList")
private TmAccessionTypeList tmAccessionTypeList;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The id
*/
@JsonProperty("id")
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The source
*/
@JsonProperty("source")
public String getSource() {
return source;
}

/**
* 
* @param source
* The source
*/
@JsonProperty("source")
public void setSource(String source) {
this.source = source;
}

/**
* 
* @return
* The pmid
*/
@JsonProperty("pmid")
public String getPmid() {
return pmid;
}

/**
* 
* @param pmid
* The pmid
*/
@JsonProperty("pmid")
public void setPmid(String pmid) {
this.pmid = pmid;
}

/**
* 
* @return
* The pmcid
*/
@JsonProperty("pmcid")
public String getPmcid() {
return pmcid;
}

/**
* 
* @param pmcid
* The pmcid
*/
@JsonProperty("pmcid")
public void setPmcid(String pmcid) {
this.pmcid = pmcid;
}

/**
* 
* @return
* The title
*/
@JsonProperty("title")
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
@JsonProperty("title")
public void setTitle(String title) {
this.title = title;
}

/**
* 
* @return
* The authorString
*/
@JsonProperty("authorString")
public String getAuthorString() {
return authorString;
}

/**
* 
* @param authorString
* The authorString
*/
@JsonProperty("authorString")
public void setAuthorString(String authorString) {
this.authorString = authorString;
}

/**
* 
* @return
* The journalTitle
*/
@JsonProperty("journalTitle")
public String getJournalTitle() {
return journalTitle;
}

/**
* 
* @param journalTitle
* The journalTitle
*/
@JsonProperty("journalTitle")
public void setJournalTitle(String journalTitle) {
this.journalTitle = journalTitle;
}

/**
* 
* @return
* The issue
*/
@JsonProperty("issue")
public String getIssue() {
return issue;
}

/**
* 
* @param issue
* The issue
*/
@JsonProperty("issue")
public void setIssue(String issue) {
this.issue = issue;
}

/**
* 
* @return
* The journalVolume
*/
@JsonProperty("journalVolume")
public String getJournalVolume() {
return journalVolume;
}

/**
* 
* @param journalVolume
* The journalVolume
*/
@JsonProperty("journalVolume")
public void setJournalVolume(String journalVolume) {
this.journalVolume = journalVolume;
}

/**
* 
* @return
* The pubYear
*/
@JsonProperty("pubYear")
public String getPubYear() {
return pubYear;
}

/**
* 
* @param pubYear
* The pubYear
*/
@JsonProperty("pubYear")
public void setPubYear(String pubYear) {
this.pubYear = pubYear;
}

/**
* 
* @return
* The journalIssn
*/
@JsonProperty("journalIssn")
public String getJournalIssn() {
return journalIssn;
}

/**
* 
* @param journalIssn
* The journalIssn
*/
@JsonProperty("journalIssn")
public void setJournalIssn(String journalIssn) {
this.journalIssn = journalIssn;
}

/**
* 
* @return
* The pageInfo
*/
@JsonProperty("pageInfo")
public String getPageInfo() {
return pageInfo;
}

/**
* 
* @param pageInfo
* The pageInfo
*/
@JsonProperty("pageInfo")
public void setPageInfo(String pageInfo) {
this.pageInfo = pageInfo;
}

/**
* 
* @return
* The pubType
*/
@JsonProperty("pubType")
public String getPubType() {
return pubType;
}

/**
* 
* @param pubType
* The pubType
*/
@JsonProperty("pubType")
public void setPubType(String pubType) {
this.pubType = pubType;
}

/**
* 
* @return
* The isOpenAccess
*/
@JsonProperty("isOpenAccess")
public String getIsOpenAccess() {
return isOpenAccess;
}

/**
* 
* @param isOpenAccess
* The isOpenAccess
*/
@JsonProperty("isOpenAccess")
public void setIsOpenAccess(String isOpenAccess) {
this.isOpenAccess = isOpenAccess;
}

/**
* 
* @return
* The inEPMC
*/
@JsonProperty("inEPMC")
public String getInEPMC() {
return inEPMC;
}

/**
* 
* @param inEPMC
* The inEPMC
*/
@JsonProperty("inEPMC")
public void setInEPMC(String inEPMC) {
this.inEPMC = inEPMC;
}

/**
* 
* @return
* The inPMC
*/
@JsonProperty("inPMC")
public String getInPMC() {
return inPMC;
}

/**
* 
* @param inPMC
* The inPMC
*/
@JsonProperty("inPMC")
public void setInPMC(String inPMC) {
this.inPMC = inPMC;
}

/**
* 
* @return
* The citedByCount
*/
@JsonProperty("citedByCount")
public Integer getCitedByCount() {
return citedByCount;
}

/**
* 
* @param citedByCount
* The citedByCount
*/
@JsonProperty("citedByCount")
public void setCitedByCount(Integer citedByCount) {
this.citedByCount = citedByCount;
}

/**
* 
* @return
* The hasReferences
*/
@JsonProperty("hasReferences")
public String getHasReferences() {
return hasReferences;
}

/**
* 
* @param hasReferences
* The hasReferences
*/
@JsonProperty("hasReferences")
public void setHasReferences(String hasReferences) {
this.hasReferences = hasReferences;
}

/**
* 
* @return
* The hasTextMinedTerms
*/
@JsonProperty("hasTextMinedTerms")
public String getHasTextMinedTerms() {
return hasTextMinedTerms;
}

/**
* 
* @param hasTextMinedTerms
* The hasTextMinedTerms
*/
@JsonProperty("hasTextMinedTerms")
public void setHasTextMinedTerms(String hasTextMinedTerms) {
this.hasTextMinedTerms = hasTextMinedTerms;
}

/**
* 
* @return
* The hasDbCrossReferences
*/
@JsonProperty("hasDbCrossReferences")
public String getHasDbCrossReferences() {
return hasDbCrossReferences;
}

/**
* 
* @param hasDbCrossReferences
* The hasDbCrossReferences
*/
@JsonProperty("hasDbCrossReferences")
public void setHasDbCrossReferences(String hasDbCrossReferences) {
this.hasDbCrossReferences = hasDbCrossReferences;
}

/**
* 
* @return
* The hasLabsLinks
*/
@JsonProperty("hasLabsLinks")
public String getHasLabsLinks() {
return hasLabsLinks;
}

/**
* 
* @param hasLabsLinks
* The hasLabsLinks
*/
@JsonProperty("hasLabsLinks")
public void setHasLabsLinks(String hasLabsLinks) {
this.hasLabsLinks = hasLabsLinks;
}

/**
* 
* @return
* The hasTMAccessionNumbers
*/
@JsonProperty("hasTMAccessionNumbers")
public String getHasTMAccessionNumbers() {
return hasTMAccessionNumbers;
}

/**
* 
* @param hasTMAccessionNumbers
* The hasTMAccessionNumbers
*/
@JsonProperty("hasTMAccessionNumbers")
public void setHasTMAccessionNumbers(String hasTMAccessionNumbers) {
this.hasTMAccessionNumbers = hasTMAccessionNumbers;
}

/**
* 
* @return
* The luceneScore
*/
@JsonProperty("luceneScore")
public String getLuceneScore() {
return luceneScore;
}

/**
* 
* @param luceneScore
* The luceneScore
*/
@JsonProperty("luceneScore")
public void setLuceneScore(String luceneScore) {
this.luceneScore = luceneScore;
}

/**
* 
* @return
* The doi
*/
@JsonProperty("doi")
public String getDoi() {
return doi;
}

/**
* 
* @param doi
* The doi
*/
@JsonProperty("doi")
public void setDoi(String doi) {
this.doi = doi;
}

/**
* 
* @return
* The dbCrossReferenceList
*/
@JsonProperty("dbCrossReferenceList")
public DbCrossReferenceList getDbCrossReferenceList() {
return dbCrossReferenceList;
}

/**
* 
* @param dbCrossReferenceList
* The dbCrossReferenceList
*/
@JsonProperty("dbCrossReferenceList")
public void setDbCrossReferenceList(DbCrossReferenceList dbCrossReferenceList) {
this.dbCrossReferenceList = dbCrossReferenceList;
}

/**
* 
* @return
* The tmAccessionTypeList
*/
@JsonProperty("tmAccessionTypeList")
public TmAccessionTypeList getTmAccessionTypeList() {
return tmAccessionTypeList;
}

/**
* 
* @param tmAccessionTypeList
* The tmAccessionTypeList
*/
@JsonProperty("tmAccessionTypeList")
public void setTmAccessionTypeList(TmAccessionTypeList tmAccessionTypeList) {
this.tmAccessionTypeList = tmAccessionTypeList;
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

