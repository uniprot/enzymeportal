#!/bin/bash
# Indexes TrEMBL enzymes into an existing mega-mapper index.
# Param:
# $1: directory for the existing lucene index.

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

java -classpath $CP uk.ac.ebi.ep.mm.UniprotIndexer \
    -xmlFile $TREMBL -indexDir $1

