#!/bin/bash
# Generate sitemap
# Param:
# $1: database environment (uzpdev|uzprel)

TODAY=$(date +%Y%m%d_%H-%M-%S)
LOG_NAME=$(basename $1 .sh)
LOG_DIR=/ebi/uniprot/production/enzyme_portal/logs/sitemap/bsub-$LOG_NAME-$TODAY.log

echo
echo "**************************** W A R N I N G ****************************"
echo "This will generate sitemap at /ebi/uniprot/production/enzyme_portal/sitemap"
echo "LSF logs can be found here $LOG_DIR "
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

#bsub -R "rusage[mem=32000]" -M 32000 -q production-rh7 $(dirname $0)/$1
bsub -R "rusage[mem=32000]" -M 128000 -q production-rh7 -o $LOG_DIR $(dirname $0)/$1
