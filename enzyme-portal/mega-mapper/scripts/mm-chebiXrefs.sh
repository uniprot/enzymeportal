#!/bin/bash
# Generates a XML file with xrefs for ChEBI.
# Param:
# $1: database environment (enzdev|ezprel)

. $(dirname $0)/readDbConfig.sh
. $(dirname $0)/ts.sh

EP_BACKUP_DIR=/nfs/panda/production/steinbeck/ep/backup
XMLFILE=$EP_BACKUP_DIR/chebiXrefs-${1}-${NOW}.xml

echo -e "Generating cross-references XML for ChEBI from $1...\n"
echo 'exit' | sqlplus -S ${DB_USER}/${DB_PASSWD}@${1} \
    @$(dirname $0)/../src/main/sql/util/chebiXrefs.sql > $XMLFILE
echo "XML file generated: $XMLFILE"

