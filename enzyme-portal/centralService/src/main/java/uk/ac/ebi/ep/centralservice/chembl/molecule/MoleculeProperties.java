/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.molecule;


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
"acd_logd",
"acd_logp",
"acd_most_apka",
"acd_most_bpka",
"alogp",
"aromatic_rings",
"full_molformula",
"full_mwt",
"hba",
"hbd",
"heavy_atoms",
"med_chem_friendly",
"molecular_species",
"mw_freebase",
"mw_monoisotopic",
"num_alerts",
"num_ro5_violations",
"psa",
"qed_weighted",
"ro3_pass",
"rtb"
})
/**
 *
 * @author joseph
 */
public class MoleculeProperties {

@JsonProperty("acd_logd")
private String acdLogd;
@JsonProperty("acd_logp")
private String acdLogp;
@JsonProperty("acd_most_apka")
private String acdMostApka;
@JsonProperty("acd_most_bpka")
private String acdMostBpka;
@JsonProperty("alogp")
private String alogp;
@JsonProperty("aromatic_rings")
private Integer aromaticRings;
@JsonProperty("full_molformula")
private String fullMolformula;
@JsonProperty("full_mwt")
private String fullMwt;
@JsonProperty("hba")
private Integer hba;
@JsonProperty("hbd")
private Integer hbd;
@JsonProperty("heavy_atoms")
private Integer heavyAtoms;
@JsonProperty("med_chem_friendly")
private String medChemFriendly;
@JsonProperty("molecular_species")
private String molecularSpecies;
@JsonProperty("mw_freebase")
private String mwFreebase;
@JsonProperty("mw_monoisotopic")
private String mwMonoisotopic;
@JsonProperty("num_alerts")
private Integer numAlerts;
@JsonProperty("num_ro5_violations")
private Integer numRo5Violations;
@JsonProperty("psa")
private String psa;
@JsonProperty("qed_weighted")
private String qedWeighted;
@JsonProperty("ro3_pass")
private String ro3Pass;
@JsonProperty("rtb")
private Integer rtb;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The acdLogd
*/
@JsonProperty("acd_logd")
public String getAcdLogd() {
return acdLogd;
}

/**
* 
* @param acdLogd
* The acd_logd
*/
@JsonProperty("acd_logd")
public void setAcdLogd(String acdLogd) {
this.acdLogd = acdLogd;
}

/**
* 
* @return
* The acdLogp
*/
@JsonProperty("acd_logp")
public String getAcdLogp() {
return acdLogp;
}

/**
* 
* @param acdLogp
* The acd_logp
*/
@JsonProperty("acd_logp")
public void setAcdLogp(String acdLogp) {
this.acdLogp = acdLogp;
}

/**
* 
* @return
* The acdMostApka
*/
@JsonProperty("acd_most_apka")
public String getAcdMostApka() {
return acdMostApka;
}

/**
* 
* @param acdMostApka
* The acd_most_apka
*/
@JsonProperty("acd_most_apka")
public void setAcdMostApka(String acdMostApka) {
this.acdMostApka = acdMostApka;
}

/**
* 
* @return
* The acdMostBpka
*/
@JsonProperty("acd_most_bpka")
public String getAcdMostBpka() {
return acdMostBpka;
}

/**
* 
* @param acdMostBpka
* The acd_most_bpka
*/
@JsonProperty("acd_most_bpka")
public void setAcdMostBpka(String acdMostBpka) {
this.acdMostBpka = acdMostBpka;
}

/**
* 
* @return
* The alogp
*/
@JsonProperty("alogp")
public String getAlogp() {
return alogp;
}

/**
* 
* @param alogp
* The alogp
*/
@JsonProperty("alogp")
public void setAlogp(String alogp) {
this.alogp = alogp;
}

/**
* 
* @return
* The aromaticRings
*/
@JsonProperty("aromatic_rings")
public Integer getAromaticRings() {
return aromaticRings;
}

/**
* 
* @param aromaticRings
* The aromatic_rings
*/
@JsonProperty("aromatic_rings")
public void setAromaticRings(Integer aromaticRings) {
this.aromaticRings = aromaticRings;
}

/**
* 
* @return
* The fullMolformula
*/
@JsonProperty("full_molformula")
public String getFullMolformula() {
return fullMolformula;
}

/**
* 
* @param fullMolformula
* The full_molformula
*/
@JsonProperty("full_molformula")
public void setFullMolformula(String fullMolformula) {
this.fullMolformula = fullMolformula;
}

/**
* 
* @return
* The fullMwt
*/
@JsonProperty("full_mwt")
public String getFullMwt() {
return fullMwt;
}

/**
* 
* @param fullMwt
* The full_mwt
*/
@JsonProperty("full_mwt")
public void setFullMwt(String fullMwt) {
this.fullMwt = fullMwt;
}

/**
* 
* @return
* The hba
*/
@JsonProperty("hba")
public Integer getHba() {
return hba;
}

/**
* 
* @param hba
* The hba
*/
@JsonProperty("hba")
public void setHba(Integer hba) {
this.hba = hba;
}

/**
* 
* @return
* The hbd
*/
@JsonProperty("hbd")
public Integer getHbd() {
return hbd;
}

/**
* 
* @param hbd
* The hbd
*/
@JsonProperty("hbd")
public void setHbd(Integer hbd) {
this.hbd = hbd;
}

/**
* 
* @return
* The heavyAtoms
*/
@JsonProperty("heavy_atoms")
public Integer getHeavyAtoms() {
return heavyAtoms;
}

/**
* 
* @param heavyAtoms
* The heavy_atoms
*/
@JsonProperty("heavy_atoms")
public void setHeavyAtoms(Integer heavyAtoms) {
this.heavyAtoms = heavyAtoms;
}

/**
* 
* @return
* The medChemFriendly
*/
@JsonProperty("med_chem_friendly")
public String getMedChemFriendly() {
return medChemFriendly;
}

/**
* 
* @param medChemFriendly
* The med_chem_friendly
*/
@JsonProperty("med_chem_friendly")
public void setMedChemFriendly(String medChemFriendly) {
this.medChemFriendly = medChemFriendly;
}

/**
* 
* @return
* The molecularSpecies
*/
@JsonProperty("molecular_species")
public String getMolecularSpecies() {
return molecularSpecies;
}

/**
* 
* @param molecularSpecies
* The molecular_species
*/
@JsonProperty("molecular_species")
public void setMolecularSpecies(String molecularSpecies) {
this.molecularSpecies = molecularSpecies;
}

/**
* 
* @return
* The mwFreebase
*/
@JsonProperty("mw_freebase")
public String getMwFreebase() {
return mwFreebase;
}

/**
* 
* @param mwFreebase
* The mw_freebase
*/
@JsonProperty("mw_freebase")
public void setMwFreebase(String mwFreebase) {
this.mwFreebase = mwFreebase;
}

/**
* 
* @return
* The mwMonoisotopic
*/
@JsonProperty("mw_monoisotopic")
public String getMwMonoisotopic() {
return mwMonoisotopic;
}

/**
* 
* @param mwMonoisotopic
* The mw_monoisotopic
*/
@JsonProperty("mw_monoisotopic")
public void setMwMonoisotopic(String mwMonoisotopic) {
this.mwMonoisotopic = mwMonoisotopic;
}

/**
* 
* @return
* The numAlerts
*/
@JsonProperty("num_alerts")
public Integer getNumAlerts() {
return numAlerts;
}

/**
* 
* @param numAlerts
* The num_alerts
*/
@JsonProperty("num_alerts")
public void setNumAlerts(Integer numAlerts) {
this.numAlerts = numAlerts;
}

/**
* 
* @return
* The numRo5Violations
*/
@JsonProperty("num_ro5_violations")
public Integer getNumRo5Violations() {
return numRo5Violations;
}

/**
* 
* @param numRo5Violations
* The num_ro5_violations
*/
@JsonProperty("num_ro5_violations")
public void setNumRo5Violations(Integer numRo5Violations) {
this.numRo5Violations = numRo5Violations;
}

/**
* 
* @return
* The psa
*/
@JsonProperty("psa")
public String getPsa() {
return psa;
}

/**
* 
* @param psa
* The psa
*/
@JsonProperty("psa")
public void setPsa(String psa) {
this.psa = psa;
}

/**
* 
* @return
* The qedWeighted
*/
@JsonProperty("qed_weighted")
public String getQedWeighted() {
return qedWeighted;
}

/**
* 
* @param qedWeighted
* The qed_weighted
*/
@JsonProperty("qed_weighted")
public void setQedWeighted(String qedWeighted) {
this.qedWeighted = qedWeighted;
}

/**
* 
* @return
* The ro3Pass
*/
@JsonProperty("ro3_pass")
public String getRo3Pass() {
return ro3Pass;
}

/**
* 
* @param ro3Pass
* The ro3_pass
*/
@JsonProperty("ro3_pass")
public void setRo3Pass(String ro3Pass) {
this.ro3Pass = ro3Pass;
}

/**
* 
* @return
* The rtb
*/
@JsonProperty("rtb")
public Integer getRtb() {
return rtb;
}

/**
* 
* @param rtb
* The rtb
*/
@JsonProperty("rtb")
public void setRtb(Integer rtb) {
this.rtb = rtb;
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
