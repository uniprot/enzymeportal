#!/bin/bash
# Reads dbConfig, file directory and filename from a file.
# Param:
# $1: DBConfig
# $2: fileDirectory
# $3: filename
# $4: testMode


#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config
#SITEMAP_CONFIG=$EP_CONFIG_DIR/ep-mm-db-${1}.properties

SITEMAP_CONFIG=.$(dirname $0)/sitemapProp.properties
DB_CONFIG=$(grep '^dbConfig=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_DIR=$(grep '^fileDir=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_NAME=$(grep '^fileName=' $SITEMAP_CONFIG | cut -d '=' -f 2)
TEST_MODE=$(grep '^testMode=' $SITEMAP_CONFIG | cut -d '=' -f 2)

#build the application using maven
. $(dirname $0)/mvnBuild.sh


# Running the application:
echo "[INFO] Running siteMap Generator -  $(date)"
echo "[INFO] You are about to run the SitemapGenerator using these parameters :"
echo "[INFO] *******************************************************************"
echo "[INFO] " $DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE
echo "[INFO] *******************************************************************"
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.main.SiteMapMain" -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE"
echo "[INFO] Running complete -  $(date)"

echo "About to copy sitemap to this directory /nfs/public/rw/cm/ep/sitemap "
become cm_adm cp $FILE_DIR/sitemap-ep*.xml $FILE_DIR/sitemap_index.xml /nfs/public/rw/cm/ep/sitemap

echo "Done Copying files. - $(date)"
echo "deleting the generated files - clean up"
rm -f $FILE_DIR/sitemap*
echo "Done cleaning up the directory"
echo "SUCCESS -$(date)"
