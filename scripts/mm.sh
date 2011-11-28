#!/bin/bash
# Creates a mega-mapper lucene index with EB-Eye files.
# Indexes UniProt enzymes (both Swiss-Prot and TrEMBL) and then ChEBI and
# ChEMBL.
# Param:
# $1: directory for the created lucene index.

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
UNIPROT_DATA=$EBINOCLE_DATA/uniprot/latest
SWISSPROT=$UNIPROT_DATA/uniprot_sprot.xml
TREMBL=$UNIPROT_DATA/uniprot_trembl.xml
CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
CHEMBL=$EBINOCLE_DATA/chembl/latest/chembl-target.xml

cd $(dirname $0)/..
mvn clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

java -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser-xmlFile $SWISSPROT \
&& java -classpath $CP uk.ac.ebi.ep.mm.UniprotSaxParser -xmlFile $TREMBL \
&& java -classpath $CP uk.ac.ebi.ep.mm.EbeyeParser -xmlFile $CHEBI \
&& java -classpath $CP uk.ac.ebi.ep.mm.EbeyeParser -xmlFile $CHEMBL

