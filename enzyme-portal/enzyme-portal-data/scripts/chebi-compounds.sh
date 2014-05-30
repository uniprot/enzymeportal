MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"

#parse uniprot data for ChEBI Compounds (activators & inhibitors)
echo "Request to query & parse UniProt data for ChEBI compounds (Activators/Inhibitors)  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.ChEBIParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "Finished querying and parsing UniProt data and updating Enzyme Portal database with ChEBI Compounds  - $(date)"

