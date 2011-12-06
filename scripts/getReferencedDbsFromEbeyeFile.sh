#!/bin/bash
# Filters the cross-referenced databases in a EBEye XML file to stdout.
# Parameter:
# $1: the EB-Eye XML file

grep "ref dbname" $1 \
    | sed 's/<ref dbname="\(.*\)" dbkey.*$/\1/g' \
    | sort | uniq
