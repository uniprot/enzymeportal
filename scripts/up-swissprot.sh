#!/bin/bash
# Creates an initial mega-map with the Swiss-Prot file.

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
SWISSPROT=$EBINOCLE_DATA/uniprot/latest/uniprot_sprot.xml

cd $(dirname $0)/..
mvn clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

echo "Starting Swiss-Prot import - $(date)"
java -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser -xmlFile $SWISSPROT
echo "Finished Swiss-Prot import - $(date)"
