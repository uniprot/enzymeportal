#!/bin/bash
# Reads dbConfig, file directory and filename from a file.
# Param:
# $1: DBConfig
# $2: fileDirectory
# $3: filename


#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config
#SITEMAP_CONFIG=$EP_CONFIG_DIR/ep-mm-db-${1}.properties

SITEMAP_CONFIG=.$(dirname $0)/sitemapProp.properties
DB_CONFIG=$(grep '^dbConfig=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_DIR=$(grep '^fileDir=' $SITEMAP_CONFIG | cut -d '=' -f 2)
FILE_NAME=$(grep '^fileName=' $SITEMAP_CONFIG | cut -d '=' -f 2)

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