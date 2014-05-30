MM_SCRIPTS=$(cd $(dirname $0) && pwd)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
INTENZ_XML=$EBINOCLE_DATA/intenz/latest/intenz.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target_component.xml
#UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html
UNIMED=http://research.isb-sib.ch/unimed/SP_MeSH.tab


EP_CONFIG_DIR=/ebi/uniprot/production/enzyme_portal/dbconfig


#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"


#parse unimed_tsv file for diseases
echo "Request to parse UNIMED File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "The file to be parsed =" $UNIMED
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.DiseaseFileParser" -Dexec.args="$DB_CONFIG $UNIMED"
cd $WD
echo "Finished parsing unimed file and updating Enzyme Portal database with Diseases Data  - $(date)"


#parse chembl-target_component.xml for ChEMBL compounds
echo "Request to parse chembl-target_component.xml File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "The file to be parsed =" $CHEMBL_TARGET
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.ChEMBLXmlParser" -Dexec.args="$DB_CONFIG $CHEMBL_TARGET"
cd $WD
echo "Finished parsing chembl-target_component.xml file and updating Enzyme Portal database with ChEMBL Compounds  - $(date)"






#parse uniprot data for ChEBI Compounds (activators & inhibitors)
echo "Request to query & parse UniProt data for ChEBI compounds (Activators/Inhibitors)  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.ChEBIParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "Finished querying and parsing UniProt data and updating Enzyme Portal database with ChEBI Compounds  - $(date)"




#Parse intenz.xml file

echo "Request to parse intenz.xml - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "The file to be parsed =" $INTENZ_XML
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.IntenzXmlParser" -Dexec.args="$DB_CONFIG $INTENZ_XML"
cd $WD
echo "Finished parsing intenz.xml and updating Enzyme Portal database with ChEBI & Reaction (Rhea) Data  - $(date)"