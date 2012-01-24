#!/bin/bash
# Imports EB-Eye XML files into an existing mega-map.
# Params:
# $1: runtime environment (dev|test|prod)
# $2: EB-Eye XML file (ChEBI/ChEMBL-target)

. $(dirname $0)/checkParams.sh

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

. $(dirname $0)/mvnBuild.sh

echo "Starting EB-Eye import - $(date)"
java -Xms512M -Xmx1G -classpath $CP \
	uk.ac.ebi.ep.mm.app.EbeyeSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $2
echo "Finished EB-Eye import - $(date)"
