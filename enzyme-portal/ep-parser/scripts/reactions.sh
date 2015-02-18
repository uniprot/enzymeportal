
MM_SCRIPTS=$(cd $(dirname $0) && pwd)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
INTENZ_XML=$EBINOCLE_DATA/intenz/latest/intenz.xml

#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
#. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"


#Parse intenz.xml file

echo "[INFO] Request to parse intenz.xml - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $INTENZ_XML
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.IntenzXmlParser" -Dexec.args="$DB_CONFIG $INTENZ_XML"
cd $WD
echo "[INFO] Finished querying and parsing intenz.xml and updating Enzyme Portal database with Reactions & ChEBI Compounds  - $(date)"
