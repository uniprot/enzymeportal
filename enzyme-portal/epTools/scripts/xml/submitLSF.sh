#!/bin/bash
# Submits job to LSF
#usage : ./submitLSF.sh <the script>  DB_CONFIG example : ./submitLSF.sh protein-xml-generator.sh uzprel
# Param:
# $1: database environment (uzpdev|uzprel)
# $2 : the script to be submitted to the farm with extension e.g protein-xml-generator.sh

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL UN THIS SCRIPT $2 AGAINST THE DATABASE $1. DO YOU WANT TO CONTINUE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 $(dirname $0)/$2 $1
