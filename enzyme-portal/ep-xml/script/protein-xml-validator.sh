#Validate protein-centric XML
#This is how to run this script
# ./protein-xml-validator.sh DB_CONFIG

EP_SCRIPTS=$(cd $(dirname $0) && pwd)


#ensure that db configuration is passed as parameter
. $EP_SCRIPTS/checkParams.sh


DB_CONFIG="$1"

echo "[INFO] Request to start validating protein-centric XML - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.xml.main.ProteinXmlValidator" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished validating Protein-centric XML.Please check the logs.  - $(date)"

