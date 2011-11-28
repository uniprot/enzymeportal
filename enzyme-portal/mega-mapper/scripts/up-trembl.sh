#!/bin/bash
# Indexes TrEMBL enzymes into an existing mega-map.

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
TREMBL=$EBINOCLE_DATA/uniprot/latest/uniprot_trembl.xml

cd $(dirname $0)/..
mvn clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

echo "Starting TrEMBL import - $(date)"
java -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser -xmlFile $TREMBL
echo "Finished TrEMBL import - $(date)"
