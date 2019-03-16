
#Generate protein XML for indexing
#This is how to run this script
# ./uniprot-protein-xml-generator.sh


echo "[INFO] Request to start generating protein XML - $(date)"
echo "[INFO] *******************************************************************"
#fine-tune memory for this job.
export MAVEN_OPTS="-Xmx64g -Xss512m"
WD=$(pwd)
cd $(dirname $0)/..

mvn exec:java@protein-batch -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished generating Protein XML.  - $(date)"

