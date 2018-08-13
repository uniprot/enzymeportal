# This script is same as running molecule.sh & fda.sh
MM_SCRIPTS=$(cd $(dirname $0) && pwd)
#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh

DB_CONFIG="$1"

#LOAD CHEMBL molecules
echo "[INFO] Request to Parse CHEMBL Targets and Load Compounds to database  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.ChemblSmallMolecules" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished Parsing CHEMBL Targets and loading Compounds to Enzyme Portal database  - $(date)"

