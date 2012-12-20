#!/bin/bash
# Drops all objects in the mega-map.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh

echo -e "Dropping whole schema in $1...\n"
echo 'commit; exit' | sqlplus ${DB_USER}/${DB_PASSWD}@${1} \
    @$(dirname $0)/../src/main/sql/drop_all

