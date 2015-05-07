
MM_SCRIPTS=$(cd $(dirname $0) && pwd)


MM_SCRIPTS=$(cd $(dirname $0) && pwd)

#/ebi/extserv/projects/ebinocle/data/chembl-target_component/latest/

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
CHEMBL_TARGET=$EBINOCLE_DATA/chembl-target_component/latest/chembl-target_component.xml


#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
#. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"

#parse chembl-target_component.xml for ChEMBL compounds
echo "[INFO] Request to parse chembl-target_component.xml File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $CHEMBL_TARGET
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.FDAParser" -Dexec.args="$DB_CONFIG $CHEMBL_TARGET"
cd $WD
echo "[INFO] Finished parsing chembl-target_component.xml file and updating Enzyme Portal database with ChEMBL Compounds  - $(date)"

