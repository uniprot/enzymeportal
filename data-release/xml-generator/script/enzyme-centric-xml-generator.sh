#Generate enzyme-centric XML for indexing
#This is how to run this script
# ./enzyme-centric-xml-generator.sh


echo "[INFO] Request to start generating enzyme-centric XML - $(date)"
echo "[INFO] *******************************************************************"
#fine-tune memory for this job.
#export MAVEN_OPTS="-Xmx64g -Xss512m"
export MAVEN_OPTS="-Xms100m -Xmx100G -XX:-UseGCOverheadLimit -XX:+UseG1GC"
WD=$(pwd)
cd $(dirname $0)/..
#mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.xml.EnzymeCentricBatchJob" -Dexec.cleanupDaemonThreads=false
mvn exec:java@enzyme-centric-batch -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished generating Enzyme-centric XML.  - $(date)"

#Not_Used_export MAVEN_OPTS="-Xms100m -Xmx100G -XX:-UseGCOverheadLimit -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseG1GC"

