#!/bin/bash
# Generate sitemap
# Param:
# $1: database environment (uzpdev|uzprel)

echo
echo "**************************** W A R N I N G ****************************"
echo "This will generate sitemap at /ebi/uniprot/production/enzyme_portal/sitemap"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

bsub -R "rusage[mem=32000]" -M 32000 -q production-rh7 $(dirname $0)/$1
