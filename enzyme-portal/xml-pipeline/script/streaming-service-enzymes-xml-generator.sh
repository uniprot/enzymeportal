

echo "[INFO] Request to start generating enzyme-centric XML - $(date)"
echo "[INFO] *******************************************************************"
#fine-tune memory for this job.
export MAVEN_OPTS="-Xmx64g -Xss512m"
WD=$(pwd)
cd $(dirname $0)/..
#mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.xml.streaming.XmlStreamingServiceMain" -Dexec.cleanupDaemonThreads=false
mvn exec:java@stream-xml -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished generating Enzyme-centric XML.  - $(date)"


