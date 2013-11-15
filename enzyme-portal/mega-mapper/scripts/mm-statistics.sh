#!/bin/bash
# Generates statistics for the mega-map and saves them in a tsv file.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh
. $(dirname $0)/ts.sh

STATS_SQL=$(dirname $0)/../src/main/sql/util/statistics
STATS_DIR=$(dirname $0)/../src/site/resources/statistics
TSV_FILE=${STATS_DIR}/stats-${NOW}.tsv

echo "Generating statistics..."
sqlplus ${DB_USER}/${DB_PASSWD}@${1} @${STATS_SQL} ${TSV_FILE}
echo "Statistics dumped to file $TSV_FILE"

# Add the statistics file to git and commit:
# We are probably working on a tag:
git add ${TSV_FILE}
git commit -m 'Statistics for a new release' ${TSV_FILE}
# Let's create a temporary branch to store the file:
git checkout -b ${TMP_BRANCH}
# Switch to master and retrieve the file:
git checkout master
git checkout ${TMP_BRANCH} ${TSV_FILE}
git add ${TSV_FILE}
git commit -m 'Statistics for a new release' ${TSV_FILE}
git push
# Remove the temporary branch:
git branch -D ${TMP_BRANCH}

