#!/bin/bash
# Imports entries and cross-references from an IntEnzXML file.
# Parameter:
# $1: runtime environment (enzdev|ezprel)
# $2 (optional): IntEnzXML file to import.

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
XML_FILE=${2:-$EBINOCLE_DATA/intenz/latest/intenz.xml}

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1}

echo "Starting IntEnz import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.IntenzSaxParser \
	-dbConfig ep-mm-db-$1 -file $XML_FILE
echo "Finished IntEnz import - $(date)"
