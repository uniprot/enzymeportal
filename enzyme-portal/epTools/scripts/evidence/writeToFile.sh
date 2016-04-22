#This computes the experimental evidence and write to a file
#This is how to run this script
# ./writeToFile.sh db /dir filename.extension true or false
# e.g ./writeToFile.sh uzpdev /evidence evidence.txt false


EP_SCRIPTS=$(cd $(dirname $0) && pwd)

#ensure that db config is passed as param
. $EP_SCRIPTS/checkParams.sh


DB_CONFIG=$1
FILE_DIR=$2
FILE_NAME=$3
DELETE_FILE=$4

#compute experimental evidence in swissprot Accessions in enzyme portal
echo "[INFO] Request to start processing EEV - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/../..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.analysis.main.AnalysisTest" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME $DELETE_FILE"
cd $WD
echo "[INFO] Finished computing evidence and writing data to file  - $(date)"

