#!/bin/bash
# Creates a XML file with cross-references for ChEBI.
# Param:
# $1: database environment (enzdev|ezprel).

. $(dirname $0)/readDbConfig.sh

CM_RO_DIR=/nfs/public/ro/cm
CHEBI_EP_XREFS_XML=$CM_RO_DIR/other/ep/chebi-ep-xrefs.xml

echo "Creating chebi-ep-xrefs.xml file..."
$ORACLE_HOME/bin/sqlplus ${DB_USER}/${DB_PASSWD}@${1} \
    @$(dirname $0)/../src/main/sql/util/chebiXrefs.sql $CHEBI_EP_XREFS_XML
echo "Created"

