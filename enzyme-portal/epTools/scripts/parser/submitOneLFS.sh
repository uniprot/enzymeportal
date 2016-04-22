#!/bin/bash
# Rebuilds a fresh enzyme portal database. 
# 
# Param:
# $1: database environment (uzpdev|uzprel)
# $2 : the script to be submitted to the farm with extension e.g chebi.sh

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL OVERWRITE ALL DATA IN $1. ARE YOU SURE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

bsub -u enzyme-portal@ebi.ac.uk -R "rusage[mem=32000]" -M 32000 -q production-rh6 $(dirname $0)/$2 $1
#bsub -q production-rh6 $(dirname $0)/$2 $1
