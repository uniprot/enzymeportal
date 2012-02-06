#!/bin/bash
# Creates a complete mega-map.
# Indexes UniProt enzymes (both Swiss-Prot and TrEMBL) and then ChEBI and
# ChEMBL.
# Param:
# $1: directory for the created lucene index.

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
UNIPROT_DATA=$EBINOCLE_DATA/uniprot/latest
SWISSPROT=$UNIPROT_DATA/uniprot_sprot.xml
TREMBL=$UNIPROT_DATA/uniprot_trembl.xml
CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target.xml

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh

echo "Starting Swiss-Prot import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $SWISSPROT
echo "Finished Swiss-Prot import - $(date)"

#echo "Starting TrEMBL import - $(date)"
#java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
#	-dbConfig ep-mm-db-$1 -xmlFile $TREMBL
#echo "Finished TrEMBL import - $(date)"

echo "Starting ChEBI import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.EbeyeSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $CHEBI
echo "Finished ChEBI import - $(date)"

echo "Starting ChEMBL import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.EbeyeSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $CHEMBL_TARGET
echo "Finished ChEMBL import - $(date)"

echo "Starting UniMed import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.Uniprot2DiseaseParser \
	-dbConfig ep-mm-db-$1
echo "Finished UniMed import - $(date)"
