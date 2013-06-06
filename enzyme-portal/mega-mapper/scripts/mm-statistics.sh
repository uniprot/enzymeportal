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
git add ${TSV_FILE}
git commit -m 'Statistics for a new release' ${TSV_FILE}
git push

# TODO: copy to master to have all statistics in one place, not urgent
