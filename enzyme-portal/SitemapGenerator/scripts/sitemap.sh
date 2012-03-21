#!/bin/bash

#DB_CONFIG='ep-mm-db-enzdev'
#FILE_DIR="C:/Users/joseph"
#FILE_NAME="EnzymePortalSiteMap"

DB_CONFIG=$1
FILE_DIR=$2
FILE_NAME=$3


#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config

#build the application using maven
. $(dirname $0)/mvnBuild.sh


# Running the application:
echo "[INFO] Running siteMap Generator -  $(date)"
echo "[INFO] You are about to run the SitemapGenerator using these parameters :"
echo "[INFO] *******************************************************************"
echo "[INFO] " $DB_CONFIG $FILE_DIR $FILE_NAME
echo "[INFO] *******************************************************************"
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.SiteMapMain" -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME"
echo "[INFO] Running complete -  $(date)"

