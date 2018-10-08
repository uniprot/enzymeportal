#load unisave data to couchbase
echo "[INFO] Request to load unisave data to couchbase - $(date)"
echo "[INFO] *******************************************************************"
#fine-tune memory for this job.
#export MAVEN_OPTS="-Xmx64g -Xss512m"
WD=$(pwd)
cd $(dirname $0)/..

mvn exec:java@load-unisave -Dexec.cleanupDaemonThreads=false
cd $WD
echo "[INFO] Finished loading unisave data to couchbase.  - $(date)"
