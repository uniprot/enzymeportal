MM_SCRIPTS=$(cd $(dirname $0) && pwd)


#UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html
UNIMED=http://research.isb-sib.ch/unimed/SP_MeSH.tab

#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"

#parse unimed_tsv file for diseases
echo "[INFO] Request to parse UNIMED File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $UNIMED
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.DiseaseFileParser" -Dexec.args="$DB_CONFIG $UNIMED"
cd $WD
echo "[INFO] Finished parsing unimed file and updating Enzyme Portal database with Diseases Data  - $(date)"
