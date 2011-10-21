#!/bin/bash
# Indexes EB-Eye XML files into an existing mega-mapper index.
# Params:
# $1: EB-Eye XML file (ChEBI/ChEMBL-target)
# $2: directory containing an existing mega-mapper lucene index

cd $(dirname $0)/..
mvn clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

java -classpath $CP uk.ac.ebi.ep.mm.EbeyeIndexer \
    -xmlFile $1 -indexDir $2

