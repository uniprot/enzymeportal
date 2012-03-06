#!/bin/bash
# Reads the database username and password from a file.
# Param:
# $1: database environment (enzdev|ezprel)

EP_CONFIG_DIR=/nfs/panda/production/steinbeck/ep/config

. $(dirname $0)/checkParams.sh

DB_CONFIG=ep-mm-db-${1}.properties
DB_USER=$(grep '^user=' $EP_CONFIG_DIR/$DB_CONFIG | cut -d '=' -f 2)
DB_PASSWD=$(grep '^password=' $EP_CONFIG_DIR/$DB_CONFIG | cut -d '=' -f 2)
#DB_INSTANCE=$(grep '^instance=' $EP_CONFIG_DIR/$DB_CONFIG | cut -d '=' -f 2)

