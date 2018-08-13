MM_SCRIPTS=$(cd $(dirname $0) && pwd)


#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh


DB_CONFIG="$1"

#parse chembl target to get compounds
echo "[INFO] Request to parse chembl targets and compute activities to get compounds  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.MoleculeParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished parsing data and updating Enzyme Portal database  - $(date)"

