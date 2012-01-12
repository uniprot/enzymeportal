#!/bin/bash
# Indexes TrEMBL enzymes into an existing mega-map.

. checkParams.sh

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
TREMBL=$EBINOCLE_DATA/uniprot/latest/uniprot_trembl.xml

. $(dirname $0)/mvnBuild.sh

echo "Starting TrEMBL import - $(date)"
java -Xms512M -Xmx1G -classpath $CP \
	uk.ac.ebi.ep.mm.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $TREMBL
echo "Finished TrEMBL import - $(date)"
