#!/bin/bash
# Refresh data from a backup of the mega-map.
# Param:
# $1: database instance to refresh (enzdev|ezprel)
# $2: backup file (may be gzipped)

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL OVERWRITE ALL DATA IN $1. ARE YOU SURE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

. $(dirname $0)/readDbConfig.sh $1

[ ! -e "$2" ] && echo "Please especify an oracle dump file" && exit 1

# Check if dump file is gzipped, unpack if needed:
if [ "$(file $2 | grep -c 'gzip compressed data')" == 1 ]
then
    echo "$2 - Unpacking gzipped dump file..."
    # Unpack dump file
    TMP_DMP=$$.dmp
    gunzip -c $2 > $TMP_DMP
    DMP=$TMP_DMP
fi

. $(dirname $0)/mm-drop.sh $1

echo "Importing from backup..."
$ORACLE_HOME/bin/imp ${DB_USER}/${DB_PASSWD}@$1 \
    file=$DMP log=$(dirname $0)/ep-mm-imp.log

$ORACLE_HOME/bin/sqlplus ${DB_USER}/${DB_PASSWD}@$1 \
    @$(dirname $0)/../src/main/sql/grant_web.sql

# Clean up:
[ -n "$TMP_DMP" ] && rm $TMP_DMP

