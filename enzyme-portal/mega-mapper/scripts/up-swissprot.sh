#!/bin/bash
# Creates an initial mega-map with the Swiss-Prot file.
# Parameter:
# $1: runtime environment (enzdev|ezprel)
# $2 (optional): Swiss-Prot XML file to import.

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
XML_FILE=${2:-$EBINOCLE_DATA/uniprot/latest/uniprot_sprot.xml}
#HIBERNATE_OPTS="-Dhibernate.hbm2ddl.auto=create"

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1}

echo "Starting Swiss-Prot import - $(date)"
java $JAVA_OPTS $HIBERNATE_OPTS -classpath $CP \
    uk.ac.ebi.ep.mm.app.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $XML_FILE
echo "Finished Swiss-Prot import - $(date)"
