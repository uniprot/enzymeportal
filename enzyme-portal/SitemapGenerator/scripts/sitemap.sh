#!/bin/bash

#DB_CONFIG='uzpdev'
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
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.main.SiteMapMain" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE"
echo "[INFO] Running complete -  $(date)"
echo "About to copy sitemap to this directory /nfs/public/rw/uniprot/enzyme_portal/sitemap "
#become cm_adm cp $FILE_DIR/sitemap-ep*.xml $FILE_DIR/sitemap_index.xml /nfs/public/rw/cm/ep/sitemap

become uni_adm cp $FILE_DIR/sitemap-ep*.xml $FILE_DIR/sitemap_index.xml /nfs/public/rw/uniprot/enzyme_portal/sitemap

echo "Done Copying files. - $(date)"
echo "deleting the generated files - clean up"
rm -f $FILE_DIR/sitemap*
echo "Done cleaning up the directory"
echo "SUCCESS - $(date) "

