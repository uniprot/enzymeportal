#!/bin/bash
# Creates an initial mega-map with the Swiss-Prot file.
# Parameter:
# $1 (optional): Swiss-Prot XML file to import.

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
XML_FILE=${1:-$EBINOCLE_DATA/uniprot/latest/uniprot_sprot.xml}

. $(dirname $0)/mvnBuild.sh

echo "Starting Swiss-Prot import - $(date)"
java -Xms512M -Xmx1G -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser \
    -xmlFile $XML_FILE
echo "Finished Swiss-Prot import - $(date)"
