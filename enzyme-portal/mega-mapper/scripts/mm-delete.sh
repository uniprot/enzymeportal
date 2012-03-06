#!/bin/bash
# Deletes all data in the mega-map.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh

echo 'commit; exit' | sqlplus ${DB_USER}/${DB_PASSWD}@${1} \
    $(dirname $0)/../src/main/sql/delete_all

