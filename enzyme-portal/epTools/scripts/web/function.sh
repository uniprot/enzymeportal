# Function to check return codes.
# Parameters:
# $1: return code
# $2: phase of release
function checkReturnCode(){
    if [ $1 != 0 ]
    then
        echo "[ERROR] $2 (return code: $1)"
        # Notify by mail:
        echo -e "${2}\nReturn code: $1" \
            | mail -s "$2: FAILURE" $DEVEL_MAILTO
        exit $1
    fi
}

# Reads the password for the Oracle user
# Parameter:
# $1: PROD|STAGE (which instance are we talking about?)
function readOrapasswd(){
    read -s -p "Enter password for $1 instance, or Ctrl-C to cancel: " \
        ORA_$1_PASSWD
    echo
    export ORA_$1_PASSWD
}

# Waits for jobs sent to LSF to complete successfully.
# The following variables must be defined:
# · $JOBNAME must be an array of job names
# · $UNFINISHED must be set to a string concatenating the indexes used in
#   $JOBNAME
# Parameter:
# $1: the name of the job(s) we are waiting for (may contain *)
function waitLsfJobs(){
    echo -e "Waiting for LSF jobs to finish...\n"
    UNFINISHED=1
    while [ $UNFINISHED -gt 0 ]
    do
        # bjobs -J would give us only the running ones:
        JOBS=$(bjobs -awJ $1 | grep -v JOBID)
        let UNFINISHED=0
        let FINISHED=0
        while read JOB
        do
            read JOBID LSFUSER STATUS QUEUE FROMHOST EXECHOST JOB_NAME S_TIME\
                < <(echo $JOB)
            case "$STATUS" in
                "PEND"|"RUN"|"WAIT")
                    let UNFINISHED=UNFINISHED+1
                    ;;
                "DONE")
                    let FINISHED=FINISHED+1
                    ;;
                "*") checkReturnCode 1 "Job ${JOB_NAME} with status $STATUS"
                    ;;
            esac
        done < <(echo "$JOBS")
        if [ $UNFINISHED -gt 0 ]
        then
            tput sc
            echo -n "Finished: $FINISHED, Unfinished: $UNFINISHED"
            sleep 1m
            tput el1
            tput rc
        else
            echo -e "\nJobs finished"
        fi
    done
}

function manageTomcat(){
    MACHINE=$1
    ACTION=$2
    echo "tomcat $ACTION at ${MACHINE}..."
    case $ACTION in
        start)
            sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/start
            ;;
        stop)
            sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/stop
            sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/clean_dirs
            ;;
        *)
            echo "Invalid option $ACTION for manageTomcat function"
            exit 1
            ;;
    esac
    checkReturnCode $? "Managing tomcat $ACTION at $MACHINE"
    echo -e "[OK] Started tomcat at ${MACHINE}\n"