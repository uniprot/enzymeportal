#!/bin/bash

#DB_CONFIG='ep-mm-db-enzdev-ro'
#FILE_DIR="Users/joseph"
#FILE_NAME="EnzymePortalSiteMap"
#TEST_MODE="true";

DB_CONFIG=$1
FILE_DIR=$2
FILE_NAME=$3
TEST_MODE=$4


#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config

#build the application using maven
. $(dirname $0)/mvnBuild.sh

# Increase memory for maven (it's a lot of data):
export MAVEN_OPTS="-Xms512M -Xmx1G"

# Running the application:
echo "[INFO] Running siteMap Generator -  $(date)"
echo "[INFO] You are about to run the SitemapGenerator using these parameters :"
echo "[INFO] *******************************************************************"
echo "[INFO] " $DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE
echo "[INFO] *******************************************************************"
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.SiteMapMain" -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE"
echo "[INFO] Running complete -  $(date)"

