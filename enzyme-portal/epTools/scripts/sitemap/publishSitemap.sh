#!/bin/bash
# Reads dbConfig, file directory and filename from a file.
# Param:
# $1: DBConfig
# $2: fileDirectory
# $3: filename
# $4: testMode


#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config
#SITEMAP_CONFIG=$EP_CONFIG_DIR/ep-mm-db-${1}.properties

SITEMAP_CONFIG=$(dirname $0)/sitemapProp.properties
DB_CONFIG=$(grep '^dbConfig=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_DIR=$(grep '^fileDir=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_NAME=$(grep '^fileName=' $SITEMAP_CONFIG | cut -d '=' -f 2)
TEST_MODE=$(grep '^testMode=' $SITEMAP_CONFIG | cut -d '=' -f 2)


# Running the application:
echo "[INFO] Running siteMap Generator -  $(date)"
echo "[INFO] You are about to run the SitemapGenerator using these parameters :"
echo "[INFO] *******************************************************************"
echo "[INFO] " $DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.eptools.main.SiteMapMain" -Dexec.cleanupDaemonThreads=false -Dexec.args="$DB_CONFIG $FILE_DIR $FILE_NAME $TEST_MODE"
echo "[INFO] Running complete -  $(date)"
echo "SUCCESS -$(date)"
echo "Please check that the sitemap is generated at $FILE_DIR before you run the copySitemap.sh"
