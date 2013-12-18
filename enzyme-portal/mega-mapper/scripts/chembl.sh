#!/bin/bash
# Imports EB-Eye XML files (ChEMBL target component) into an existing mega-map.
# Params:
# $1: runtime environment (enzdev|ezprel)
# $2: EB-Eye XML file (chembl-target_component.xml)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh

echo "Starting ChEMBL import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.ChemblSaxParser \
	-dbConfig ep-mm-db-$1 -file $2
echo "Finished ChEMBL import - $(date)"
