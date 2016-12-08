#!/bin/bash

# Submits job to LSF
#usage : ./submitLSF.sh <the script>  DB_CONFIG example : ./submitLSF.sh protein-xml-generator.sh uzprel
#todo fix issue : joseph@ebi-001 $ bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 ./enzyme-xml-generator.sh uzprel
#$ bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 -o /ebi/uniprot/production/enzyme_portal/logs/xml/bsub-ep-xml-$(date +%Y%m%d).log ./protein-xml-generator.sh uzprel
# Param:
# $2: database environment (uzpdev|uzprel)
# $1 : the script to be submitted to the farm with extension e.g protein-xml-generator.sh

TODAY=$(date +%Y%m%d)
LOG_NAME=$(basename $1 .sh)
LOG_DIR=/ebi/uniprot/production/enzyme_portal/logs/xml/bsub-$LOG_NAME-$TODAY.log

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL RUN THIS SCRIPT $1 AGAINST THE DATABASE $2. DO YOU WANT TO CONTINUE?"
echo "LSF logs can be found here $LOG_DIR "
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

#bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 -o $LOG_DIR  $1 $2
bsub -o $LOG_DIR  $1 $2
