#!/bin/bash

BSUB_OPTS='-R "rusage[mem=32000]" -M 32000 -q production-rh6'
bsub $BSUB_OPTS $(dirname $0)/uniprot.sh