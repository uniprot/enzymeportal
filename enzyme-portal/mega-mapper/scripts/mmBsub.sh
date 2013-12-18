#!/bin/bash
# Creates a complete mega-map, replacing the existing one. It asks for a
# password to delete all data and then sends the script mm.sh to LSF.
# Param:
# $1: database environment (enzdev|ezprel)

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL OVERWRITE ALL DATA IN $1. ARE YOU SURE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 $(dirname $0)/mm.sh $1
