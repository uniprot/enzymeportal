#!/bin/bash
# Parses a UniProt-to-OMIM_and_MeSH HTML file into an existing mega-map.
# Params:
# $1: runtime environment (enzdev|ezprel)
# $2: [optional] file containing the UniProt-to-OMIM_and_MeSH table.
#     Defaults to
#     http://research.isb-sib.ch/unimed/SP_MeSH.tab

#UNIMED=${2:-http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html}
UNIMED=${2:-http://research.isb-sib.ch/unimed/SP_MeSH.tab}

. $(dirname $0)/checkParams.sh
. $(dirname $0)/mvnBuild.sh ${1}

echo "Starting UniMed import - $(date)"
java $JAVA_OPTS -classpath $CP uk.ac.ebi.ep.mm.app.Uniprot2DiseaseParser \
	-dbConfig ep-mm-db-$1 -file $UNIMED
echo "Finished UniMed import - $(date)"
