#!/bin/bash
# Imports EB-Eye XML files into an existing mega-map.
# Params:
# $1: runtime environment (enzdev|ezprel)
# $2: EB-Eye XML file (ChEMBL-target)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1}

echo "Starting EB-Eye import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.EbeyeSaxParser \
	-dbConfig ep-mm-db-$1 -file $2
echo "Finished EB-Eye import - $(date)"
