#!/bin/bash
# Re-creates the table uniprot2compound in the mega-map after all data has been
# loaded.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh

echo -e "Re-creating table uniprot2compound in $1...\n"
echo 'commit; exit' | sqlplus ${DB_USER}/${DB_PASSWD}@${1} \
    @$(dirname $0)/../src/main/sql/uniprot2compound

