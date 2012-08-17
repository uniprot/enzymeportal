#!/bin/bash


DB_CONFIG="ep-mm-db-$1"


#DB_CONFIG=$1



#build the application using maven
#. $(dirname $0)/mvnBuild.sh

# These two lines are only needed if the script is called standalone,
# if called from mm.sh the mvn project is already built:
. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1}

# Increase memory for maven (it's a lot of data):
export MAVEN_OPTS="-Xms512M -Xmx1G"

# Running the application:
echo "[INFO] Running Mega Mapper - ChEBI Compounds -  $(date)"
echo "[INFO] You are about to load ChEBI Compounds to Mega Mapper using this Database config :"
echo "[INFO] *******************************************************************"
echo "[INFO] " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn -P !noApps,apps,$1 exec:java -Dexec.mainClass="uk.ac.ebi.ep.mm.app.ChebiCompounds" -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Running complete -  $(date)"
