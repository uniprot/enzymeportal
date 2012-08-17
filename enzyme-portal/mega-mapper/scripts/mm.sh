#!/bin/bash
# Creates a complete mega-map in an empty database schema.
# Indexes UniProt enzymes (only Swiss-Prot) and then ChEBI, ChEMBL and diseases.
# Param:
# $1: database environment (enzdev|ezprel)

MM_SCRIPTS=$(cd $(dirname $0) && pwd)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data
UNIPROT_DATA=$EBINOCLE_DATA/uniprot/latest
SWISSPROT=$UNIPROT_DATA/uniprot_sprot.xml
TREMBL=$UNIPROT_DATA/uniprot_trembl.xml
CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target.xml
UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html

EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config

. $MM_SCRIPTS/checkParams.sh
. $MM_SCRIPTS/mvnBuild.sh

# Delete previous data:
echo -e "\n*************************************************************\n"
. $MM_SCRIPTS/mm-delete.sh

# Import all database IDs, accessions and xrefs:
echo -e "\n*************************************************************\n"

echo "Starting Swiss-Prot import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -xmlFile $SWISSPROT
echo "Finished Swiss-Prot import - $(date)"

#echo "Starting TrEMBL import - $(date)"
#java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
#	-dbConfig ep-mm-db-$1 -xmlFile $TREMBL
#echo "Finished TrEMBL import - $(date)"

echo "Starting ChEBI import - $(date)"
. $MM_SCRIPTS/loadChEBICompounds.sh
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
echo -e "\n*************************************************************\n"
. $MM_SCRIPTS/mm-backup.sh

# Generate statistics:
echo -e "\n*************************************************************\n"
. $MM_SCRIPTS/mm-statistics.sh

