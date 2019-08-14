
#Generate protein-centric XML for indexing
#This is how to run this script
# ./protein-centric-xml-generator.sh


echo "[INFO] Request to start generating protein-centric XML - $(date)"
echo "[INFO] *******************************************************************"
#fine-tune memory for this job.
#export MAVEN_OPTS="-Xmx64g -Xss512m"
export MAVEN_OPTS="-Xms1G -Xmx64G -XX:-UseGCOverheadLimit -XX:+UseStringDeduplication"
WD=$(pwd)
cd $(dirname $0)/..

mvn exec:java@protein-centric-batch -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished generating Protein-centric XML.  - $(date)"

#Dexec.args="-Xms1G -Xmx16G -XX:+UseG1GC -XX:+UseStringDeduplication"