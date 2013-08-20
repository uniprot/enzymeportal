#!/bin/bash
# Creates a XML file with cross-references for ChEBI.
# Param:
# $1: database environment (enzdev|ezprel).

. $(dirname $0)/readDbConfig.sh

OUTPUT_DIR=/nfs/panda/production/steinbeck/ep/work
CHEBI_EP_XREFS_XML=$OUTPUT_DIR/chebi-ep-xrefs.xml
STAGING_DIR=/nfs/public/rw/cm/ep

echo "Creating chebi-ep-xrefs.xml file..."
$ORACLE_HOME/bin/sqlplus ${DB_USER}/${DB_PASSWD}@${1} \
    @$(dirname $0)/../src/main/sql/util/chebiXrefs.sql $CHEBI_EP_XREFS_XML
echo "Created: $CHEBI_EP_XREFS_XML"

echo "Copying generated file to $STAGING_DIR..."
become cm_adm cp $CHEBI_EP_XREFS_XML $STAGING_DIR
echo "Copied and ready to sync to LDCs."