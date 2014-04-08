#!/bin/bash
# Reads the database username and password from a file.
# Param:
# $1: database environment (enzdev|ezprel)

#. $(dirname $0)/checkParams.sh

if [ -z $DB_CONFIG ]
then
    #EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config
    EP_CONFIG_DIR=/ebi/uniprot/production/enzyme_portal/ep/config
    DB_CONFIG=$EP_CONFIG_DIR/ep-mm-db-${1}.properties
    DB_USER=$(grep '^user=' $DB_CONFIG | cut -d '=' -f 2)
    DB_PASSWD=$(grep '^password=' $DB_CONFIG | cut -d '=' -f 2)
    #DB_INSTANCE=$(grep '^instance=' $DB_CONFIG | cut -d '=' -f 2)
fi

