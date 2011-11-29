#!/bin/bash
# Imports EB-Eye XML files into an existing mega-map.
# Params:
# $1: EB-Eye XML file (ChEBI/ChEMBL-target)

cd $(dirname $0)/..
mvn clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

echo "Starting EB-Eye import - $(date)"
java -classpath $CP uk.ac.ebi.ep.mm.EbeyeParser -xmlFile $1
echo "Finished EB-Eye import - $(date)"
