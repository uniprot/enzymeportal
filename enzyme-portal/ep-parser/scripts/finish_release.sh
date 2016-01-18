#!/bin/bash

#**************************************************
# Script Name:    enzyme_portal_finish_release.sh
# Author:    Steven Rosanoff
# Date Created:    02/06/2014
# Description:    This script is part of the enzyme portal build procedure.
#        	  The script gathers statistics about the release, stores them in a 
#        	  table and sends out an email notification.
#


echo "Sourcing Oracle 11.2 home"
#. /homes/trembl/ora11R2client.sh
#export PATH=$PATH:$ORACLE_HOME/bin

echo "Setting Environment variables"
EZPHOME=/ebi/uniprot/production/enzyme_portal
export EZPHOME


# Setting output file name
OUTFILE="$EZPHOME/logs/release_stats_`date +%m%d%y`_`date +%H%M%S`.txt"

    

#*****************************************************
# Run Gather Release Statistics 
#*****************************************************
function GATHER_RELEASE_STATS() {
    echo "Gathering release statistics for all tables"
    sql_out=`sqlplus $connDetails <<-ENDOFSQL >>$OUTFILE
	SET SERVEROUTPUT ON
	exec enzyme_portal_build.gather_statistics;
	commit;
	exit;
	ENDOFSQL`;
    echo "Finished Gathering Statistics"
}


#**************************************************
# Send Email Notification to DBAs
#**************************************************
function SEND_EMAIL() {
#LOGFILE=`ls -rt $EZPHOME/logs/ | tail -1`
PRINT_STATS=`cat /$EZPHOME/logs/$LOGFILE | grep -A 21 'Enzyme Portal Release Statistics'`
template_uniprot="
FYI,

The Enzyme Portal release has finished and is now ready to be pushed to the LDC.

$PRINT_STATS

Regards,
Uniprot Team
"

echo "$template_uniprot" | mail -s "Enzyme Portal Release Statistics" ${EMAIL}
echo "Email sent to ${EMAIL}"
}

#**************************************************
# Usage Instructions
#**************************************************
function usage() {
    echo "syntax: $0"
    echo " -r ezprelvm|vezpdev : RUNMODE"
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
    EMAIL=joseph@ebi.ac.uk,srosanof@ebi.ac.uk 


    #parse the args
    while getopts "r:e:h" optionName
    do
        case "$optionName" in
        r) run_mode=$OPTARG;;
        e) EMAIL=$OPTARG;;
        h) usage 0;;
        ?) usage 0;;
        esac
    done

    
    #Setting the instance properties file
    if [ "$run_mode" = "ezprelvm" ]
    then
    	DBFILE="$EZPHOME/ep/config/ep-mm-db-hib-ezprelvm.properties"	
    elif [ "$run_mode" = "vezpdev" ]
    then
    	DBFILE="$EZPHOME/ep/config/ep-mm-db-hib-ezpdevvm.properties"
    else
        usage
    fi
    #Get the connection details for SQLPLUS
    username=`cat $DBFILE | grep username= | sed 's/.*=//'`
    password=`cat $DBFILE | grep password= | sed 's/.*=//'`
    instance=`cat $DBFILE | grep instance= | sed 's/.*=//'`
    connDetails=$username/$password@$instance

    GATHER_RELEASE_STATS;
    LOGFILE=`ls -rt $EZPHOME/logs/ | tail -1` 
    chmod 775 $EZPHOME/logs/$LOGFILE    
    SEND_EMAIL;
}

### MAIN
main $*
