#!/bin/bash

#**************************************************
# Script Name:    enzyme_portal.sh
# Author:    Steven Rosanoff
# Date Created:    07/05/2014
# Description:    This script is part of the enzyme portal build procedure.
#        The script can:
#        1. Truncate the ENZYME_PORTAL schema.
#	 2. Truncate specific ENZYME_PORTAL tables
#        4. Rebuild UNIPROT_ENTRY, UNIPROT_XREF, ENZYME_SUMMARY, tables.
#        5. Populate DISEASE Evidence data.
#
#        Expected run time for this procedure is roughly 30 minutes.
#


echo "Sourcing Oracle 11.2 home"
#. /homes/trembl/ora11R2client.sh
#export PATH=$PATH:$ORACLE_HOME/bin

echo "Setting Environment variables"
UZPHOME=/ebi/uniprot/production/enzyme_portal
export UZPHOME


# Setting output file name
OUTFILE="$UZPHOME/logs/enzyme_portal_release_`date +%m%d%y`_`date +%H%M%S`.out"

    

#*****************************************************
# Run Truncate Schema database procedure 
#*****************************************************
function TRUNCATE_SCHEMA() {
    echo "Connecting to database to truncate schema"
	sql_out=`sqlplus $connDetails <<-ENDOFSQL >>$OUTFILE
	SET SERVEROUTPUT ON
	exec enzyme_portal_build.truncate_schema;
	commit;
	exit;
	ENDOFSQL`;
    echo "Finished Truncating Schema"
}

#*****************************************************
# Run Truncate Table database procedure 
#*****************************************************
function TRUNCATE_TABLE() {
    tab_name=$1
    echo "Connecting to database to truncate table"
    sql_out=`sqlplus $connDetails <<-ENDOFSQL >>$OUTFILE
	SET SERVEROUTPUT ON
	exec enzyme_portal_build.truncate_table('$tab_name');
	commit;
	exit;
	ENDOFSQL`;
    echo "Finished Truncating Table"
}

#*****************************************************
# Run Populate All database procedure 
#*****************************************************
function POPULATE_ALL() {
    tab_name=$1
    echo "Connecting to database to populate $tab_name table"
    sql_out=`sqlplus $connDetails <<-ENDOFSQL >>$OUTFILE
	SET SERVEROUTPUT ON
	exec enzyme_portal_build.populate_all_tables;
	commit;
	exit;
	ENDOFSQL`;
    echo "Finished Populating Schema"
}

#*****************************************************
# Run Populate Table database procedure 
#*****************************************************
function POPULATE_TABLE() {
    tab_name=$1
    echo "Connecting to database to populate $tab_name table"
    sql_out=`sqlplus $connDetails <<-ENDOFSQL >>$OUTFILE
	SET SERVEROUTPUT ON
	exec enzyme_portal_build.populate_table('$tab_name');
	commit;
	exit;
	ENDOFSQL`;
    echo "Finished Populating Table"
}

#*****************************************************
# Function to build All UniProt Data in Enzyme Portal
#*****************************************************
function ALL() {
    echo ALL
    TRUNCATE_SCHEMA
    POPULATE_ALL
    SEND_EMAIL
}


#**************************************************
# Send Email Notification to DBAs
#**************************************************
function SEND_EMAIL() {
LOGFILE=`ls -rt $UZPHOME/logs/ | tail -1`
ERRORS=`cat $UZPHOME/logs/$LOGFILE | grep ORA`
template_uniprot="
FYI,

The Enzyme Portal data import from UniProt has finished. Any errors should be listed below.

$ERRORS

Regards,
Uniprot Team
"

echo "$template_uniprot" | mail -s "Enzyme Portal Data Import from UniProt Finished" ${EMAIL}
echo "Email about enzyme portal release sent to ${EMAIL}"
}

#**************************************************
# Usage Instructions
#**************************************************
function usage() {
    echo "syntax: $0"
    echo " -r uzprel|uzpdev : RUNMODE"
    echo " -p : subprocess to run. Options are:
                                TRUNCATE_SCHEMA     : Truncate all Enzyme Portal tables
                                TRUNCATE_TABLE      : Truncate a particular Enzyme Portal table
                                POPULATE_ALL	    : Populate UniProt data into Enzyme Portal
                                POPULATE_TABLE      : Populate data from UniProt for a particulare table in Enzyme Portal
                                SEND_EMAIL          : Send email
                                ALL                 : Does Truncate Schema, Populate All, and Send Email
                                you can also enlist the subprocesses with a '-', e.g. '-p CALCULATE_STATS-BACKUP_IMPORT_TABS', and they
                                will be executed in that order.
        "
    echo " -t table: table name for truncate table or populate table functions"
    echo " -e email : email to send report to (default value is $EMAIL)"
    echo " -h : prints this message"
    exit 1
}


#**************************************************
# Main
#**************************************************
function main() {
    # Arguments
    process_to_run=
    EMAIL=joseph@ebi.ac.uk,srosanof@ebi.ac.uk #trembldev@ebi.ac.uk, 


    #parse the args
    while getopts "r:p:t:e:h" optionName
    do
        case "$optionName" in
        r) run_mode=$OPTARG;;
        p) process_to_run=$OPTARG;;
        t) table_name=$OPTARG;;
        e) EMAIL=$OPTARG;;
        h) usage 0;;
        ?) usage 0;;
        esac
    done

    
    #Setting the instance properties file
    if [ "$run_mode" = "uzprel" ]
    then
    	DBFILE="$UZPHOME/dbconfig/ep-db-uzprel.properties"	
    elif [ "$run_mode" = "uzpdev" ]
    then
    	DBFILE="$UZPHOME/dbconfig/ep-db-uzpdev.properties"
    else
        usage
    fi
    #Get the connection details for SQLPLUS
    username=`cat $DBFILE | grep username= | sed 's/.*=//'`
    password=`cat $DBFILE | grep password= | sed 's/.*=//'`
    instance=`cat $DBFILE | grep instance= | sed 's/.*=//'`
    connDetails=$username/$password@$instance

    for task in `echo $process_to_run| tr "-" " "`; do
        #the -p arg requires further processing
        case $task in
            TRUNCATE_SCHEMA) TRUNCATE_SCHEMA;;
            TRUNCATE_TABLE) TRUNCATE_TABLE $table_name;;
            POPULATE_ALL) POPULATE_ALL;;
            POPULATE_TABLE) POPULATE_TABLE $table_name;;
            SEND_EMAIL) SEND_EMAIL;;
            ALL) ALL;;
            *) usage ;;
        esac
    done
}

### MAIN
main $*