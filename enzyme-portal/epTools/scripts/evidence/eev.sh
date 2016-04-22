#This computes the experimental evidence and populates the database with the result.
EP_SCRIPTS=$(cd $(dirname $0) && pwd)


#ensure that db config is passed as param
. $EP_SCRIPTS/checkParams.sh


DB_CONFIG="$1"

#compute experimental evidence in swissprot Accessions in enzyme portal
echo "[INFO] Request to start processing EEV - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/../..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.analysis.main.Analysis" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished computing experimental evidences and loaded results to enzyme portal database  - $(date)"

