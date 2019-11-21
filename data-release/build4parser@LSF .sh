#!/bin/bash

# Submits job to LSF

TODAY=$(date +%Y%m%d)

LOG_DIR=/ebi/uniprot/production/enzyme_portal/logs/ep/bsub-enzyme-portal-$TODAY.log

echo
echo "**************************** W A R N I N G ****************************"
echo "You are about to build Enzyme Portal Project. DO YOU WANT TO CONTINUE?"
echo "** Note ** LSF logs can be found here $LOG_DIR "
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

bsub -R "rusage[mem=16000]" -M 16000 -q production-rh74 -o $LOG_DIR mvn clean install -DskipTests -rf :ep-config