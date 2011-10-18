#!/bin/bash

bsub -R "rusage[mem=32000]" -M 32000 -q production-rh6 \
    $(dirname $0)/uniprot.sh
