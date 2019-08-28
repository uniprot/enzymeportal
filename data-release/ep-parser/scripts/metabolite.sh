MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh

DB_CONFIG="$1"

echo "[INFO] Request to update metabolites in Reactant table  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.MetabolightParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished updating metabolites in Enzyme Portal database  - $(date)"

