#!/bin/bash

# Submits job to LSF

TODAY=$(date +%Y%m%d)
LOG_NAME=$(basename $1 .sh)
LOG_DIR=/ebi/uniprot/production/enzyme_portal/logs/xml/bsub-$LOG_NAME-$TODAY.log

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL RUN THIS SCRIPT $1 TO LOAD UNISAVE DATA. DO YOU WANT TO CONTINUE?"
echo "LSF logs can be found here $LOG_DIR "
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

#bsub -R "rusage[mem=128000]" -M 128000 -q production-rh7 -o $LOG_DIR  $1
bsub -R "rusage[mem=128000]" -M 128000 -q production-rh7 sh loadUnisave.sh

