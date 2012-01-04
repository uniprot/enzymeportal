#!/bin/bash
# Imports EB-Eye XML files into an existing mega-map.
# Params:
# $1: EB-Eye XML file (ChEBI/ChEMBL-target)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

. $(dirname $0)/mvnBuild.sh

echo "Starting EB-Eye import - $(date)"
java -Xms512M -Xmx1G -classpath $CP \
	uk.ac.ebi.ep.mm.EbeyeSaxParser -xmlFile $1
echo "Finished EB-Eye import - $(date)"
