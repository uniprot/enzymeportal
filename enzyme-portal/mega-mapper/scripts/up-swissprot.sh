#!/bin/bash
# Creates an initial mega-map with the Swiss-Prot file.
# Parameter:
# $1: runtime environment (dev|test|prod)
# $2 (optional): Swiss-Prot XML file to import.

. checkParams.sh

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
XML_FILE=${2:-$EBINOCLE_DATA/uniprot/latest/uniprot_sprot.xml}

#HIBERNATE_OPTS="-Dhibernate.hbm2ddl.auto=create"

. $(dirname $0)/mvnBuild.sh

echo "Starting Swiss-Prot import - $(date)"
java -Xms512M -Xmx1G -classpath $CP $HIBERNATE_OPTS \
	uk.ac.ebi.ep.mm.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $XML_FILE
echo "Finished Swiss-Prot import - $(date)"
