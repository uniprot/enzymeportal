MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh

DB_CONFIG="$1"

#parse RHEA data for RHEA REACTIONS
echo "[INFO] Request to Parse Rhea ftp files and load RHEA reactions to database  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.RheaParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished Parsing Rhea ftp files and loading RHEA reactions to Enzyme Portal database  - $(date)"

