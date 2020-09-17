
MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh

DB_CONFIG="$1"

OUTPUT_DIR=/ebi/uniprot/production/enzyme_portal/data/cofactors.csv
#OUTPUT_DIR=/Users/joseph/data/csv/cofactors.csv

echo "[INFO] Request to query & parse UniProt data for ChEBI compounds (Cofactors)  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] CSV File output directory =" $OUTPUT_DIR
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.ChebiCofactorParserCSV" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG $OUTPUT_DIR"
cd $WD
echo "[INFO] Finished Writing Cofactors to FILE ($OUTPUT_DIR)  - $(date)"

