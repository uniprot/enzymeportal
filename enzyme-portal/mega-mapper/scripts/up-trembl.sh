#!/bin/bash
# Indexes TrEMBL enzymes into an existing mega-map.

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
TREMBL=$EBINOCLE_DATA/uniprot/latest/uniprot_trembl.xml

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh $1

echo "Starting TrEMBL import - $(date)"
java -Xms512M -Xmx1G -classpath $CP \
	uk.ac.ebi.ep.mm.app.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -file $TREMBL
echo "Finished TrEMBL import - $(date)"
