MM_SCRIPTS=$(cd $(dirname $0) && pwd)


#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh


DB_CONFIG="$1"

#parse chembl-target_component.xml for ChEMBL compounds
echo "[INFO] Request to parse chembl-target_component.xml File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.ChEMBLParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished parsing chembl-target file and updating Enzyme Portal database with ChEMBL Compounds  - $(date)"

