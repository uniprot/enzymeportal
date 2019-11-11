MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh

DB_CONFIG="$1"

#parse uniprot data for ChEBI Compounds (cofactors)
echo "[INFO] Request to query & parse UniProt data for ChEBI compounds (Cofactors)  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.CofactorParserFTP" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished querying and parsing UniProt data and updating Enzyme Portal database with ChEBI Compounds  - $(date)"
