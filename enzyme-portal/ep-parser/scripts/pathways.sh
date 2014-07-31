
MM_SCRIPTS=$(cd $(dirname $0) && pwd)

REACTOME_PATHWAYS=http://www.reactome.org/download/current/Uniprot2Reactome.txt

#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"


#parse Reactome file for Pathways
echo "[INFO] Request to parse REACTOME_PATHWAYS File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $REACTOME_PATHWAYS
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.PathwaysParser" -Dexec.args="$DB_CONFIG $REACTOME_PATHWAYS"
cd $WD
echo "[INFO] Finished parsing REACTOME_PATHWAYS file and updating Enzyme Portal database with Pathways Data  - $(date)"
