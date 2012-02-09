#!/bin/bash
# Parses a UniProt-to-OMIM_and_MeSH HTML file into an existing mega-map.
# Params:
# $1: runtime environment (dev|rel)
# $2: [optional] HTML file containing the UniProt-to-OMIM_and_MeSH table.
#     Defaults to
#     http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html

UP_FILE=${2:-http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html}

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1},apps

echo "Starting UniMed import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.Uniprot2DiseaseParser \
	-dbConfig ep-mm-db-$1 -xmlFile $UP_FILE
echo "Finished UniMed import - $(date)"
