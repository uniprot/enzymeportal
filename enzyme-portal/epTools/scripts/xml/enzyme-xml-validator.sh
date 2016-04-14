#Validate enzyme-centric XML
#This is how to run this script
# ./enzyme-xml-validator.sh

EP_SCRIPTS=$(cd $(dirname $0) && pwd)



echo "[INFO] Request to start validate enzyme-centric XML - $(date)"
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.xml.main.EnzymeXmlValidator" -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished validating Enzyme-centric XML. Please check the logs.  - $(date)"

