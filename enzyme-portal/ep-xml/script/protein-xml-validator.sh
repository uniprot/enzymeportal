#Validate protein-centric XML
#This is how to run this script
# ./protein-xml-validator.sh 

EP_SCRIPTS=$(cd $(dirname $0) && pwd)



echo "[INFO] Request to start validating protein-centric XML - $(date)"
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.xml.main.ProteinXmlValidator" -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished validating Protein-centric XML.Please check the logs.  - $(date)"

