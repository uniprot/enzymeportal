MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh


DB_CONFIG="$1"

#computes PDBe codes and obtain relevant title and update PDBe entries
echo "[INFO] Request to query & compute PDBe code to obtain title using PDBe web services - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.PDBeParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished querying and computing PDBe data and updating Enzyme Portal database  - $(date)"

