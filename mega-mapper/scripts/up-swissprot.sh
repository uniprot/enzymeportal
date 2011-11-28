#!/bin/bash
# Creates an initial mega-mapper lucene index with the EB-Eye Swiss-Prot file.
# Param:
# $1: directory for the created lucene index.

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

java -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser \
    -xmlFile $SWISSPROT -indexDir $1

