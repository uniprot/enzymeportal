#!/bin/bash
# Generates statistics for the mega-map and saves them in a tsv file.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh
. $(dirname $0)/ts.sh

STATS_SQL=$(dirname $0)/../src/main/sql/util/statistics
TSV_FILE=$(dirname $0)/../src/site/resources/statistics/stats-${NOW}.tsv

sqlplus ${DB_USER}/${DB_PASSWD}@${1} @${STATS_SQL} > ${TSV_FILE}

