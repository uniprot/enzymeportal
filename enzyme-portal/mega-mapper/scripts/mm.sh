#!/bin/bash
# Creates a complete mega-map in an empty database schema.
# Indexes UniProt enzymes (only Swiss-Prot) and then ChEBI, ChEMBL and diseases.
# Param:
# $1: database environment (enzdev|ezprel)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
UNIPROT_DATA=$EBINOCLE_DATA/uniprot/latest
SWISSPROT=$UNIPROT_DATA/uniprot_sprot.xml
TREMBL=$UNIPROT_DATA/uniprot_trembl.xml
CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target.xml
UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html

EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh $1

# Delete previous data:
. $(dirname $0)/mm-delete.sh $1

# Import all database IDs, accessions and xrefs:

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
	-dbConfig ep-mm-db-$1 -xmlFile $UNIMED
echo "Finished UniMed import - $(date)"

# Backup the new data:
. $(dirname $0)/mm-backup.sh $1

