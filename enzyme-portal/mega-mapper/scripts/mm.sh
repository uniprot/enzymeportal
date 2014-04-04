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
INTENZ_XML=$EBINOCLE_DATA/intenz/latest/intenz.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target_component.xml
#UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html
UNIMED=http://research.isb-sib.ch/unimed/SP_MeSH.tab

#EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config
EP_CONFIG_DIR=/ebi/uniprot/production/enzyme_portal/ep/config

. $MM_SCRIPTS/checkParams.sh
. $MM_SCRIPTS/mvnBuild.sh

# Delete previous data:
echo -e "\n*************************************************************\n"
. $MM_SCRIPTS/mm-delete.sh

# Import all database IDs, accessions and xrefs:
echo -e "\n*************************************************************\n"

echo "Starting Swiss-Prot import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
	-dbConfig ep-mm-db-$1 -file $SWISSPROT
echo "Finished Swiss-Prot import - $(date)"

#echo "Starting TrEMBL import - $(date)"
#java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.UniprotSaxParser \
#	-dbConfig ep-mm-db-$1 -file $TREMBL
#echo "Finished TrEMBL import - $(date)"

echo "Starting IntEnz import - $(date)"
java ${JAVA_OPTS} -classpath $CP uk.ac.ebi.ep.mm.app.IntenzSaxParser \
	-dbConfig ep-mm-db-$1 -file $INTENZ_XML
echo "Finished IntEnz import - $(date)"

echo "Starting ChEMBL import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.ChemblSaxParser \
	-dbConfig ep-mm-db-$1 -file $CHEMBL_TARGET
echo "Finished ChEMBL import - $(date)"

echo "Starting UniMed import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.Uniprot2DiseaseParser \
	-dbConfig ep-mm-db-$1 -file $UNIMED
echo "Finished UniMed import - $(date)"

. $MM_SCRIPTS/mm-uniprot2compound.sh

if [ "$1" = "ezprel" ]
then
	# Generate xrefs XML for ChEBI:
	echo -e "\n*************************************************************\n"
	. $MM_SCRIPTS/mm-chebiXrefs.sh
	
	# Backup the new data:
	echo -e "\n*************************************************************\n"
	. $MM_SCRIPTS/mm-backup.sh
	
	# Generate statistics:
	echo -e "\n*************************************************************\n"
	. $MM_SCRIPTS/mm-statistics.sh
fi
