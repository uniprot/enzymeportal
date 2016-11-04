#!/bin/bash
# Deployment to LDCs (to be run from ebi-00X)


. $(dirname $0)/functions.sh
. $(dirname $0)/variables.sh

# London data centers:

DCS="oy pg"


# Virtual machines in each DC:
VMS="94 95"

# Check that we are not deploying a SNAPSHOT war file:

    # What war file is the symbolic link pointing at?
    WAR=$(sudo -H -u uni_adm ls -l $TOMCAT_BASE/deploy/enzymeportal.war \
        | sed 's/.*-> //')
    # If matches timestamp or SNAPSHOT, it is a snapshot:
    IS_SNAPSHOT=$(echo $WAR | grep -P '(\-\d{8}\.\d{6}\-\d+|-SNAPSHOT)\.war')
    echo "$WAR ($IS_SNAPSHOT)"
    if [ -n "$IS_SNAPSHOT" ]
    then
        echo "Trying to deploy $WAR to LDCs?"
        echo "Don't try to synchronize a SNAPSHOT to LDCs, only releases!"
        exit 1
    fi

#tomcat dir in HH & HH-FALLBACK is not same.
TOMCAT_SERVER=tc-uni-ep
TOMCAT_BASE=/nfs/public/rw/webadmin/tomcat/bases/$PROJECT/$TOMCAT_SERVER

for DC in $DCS
do
    for VM in $VMS
    do
        # Stop tomcat
        MACHINE=ves-$DC-$VM
        echo "Stopping tomcat at ${MACHINE}..."
        sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/stop
        checkReturnCode $? "Stopping tomcat at $MACHINE"
        sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/clean_dirs
        echo -e "[OK] Stopped\n"
    done
    # Once tomcats are stopped in one DC, every request is routed to the other


        # rsync tomcat in r/w area (one VM will do for the whole DC):
        echo "Synchronizing tomcat directories at ${DC}..."
        sudo -H -u $PROJECT_ADMIN rsync \
            -rptgoDv --delete --exclude="/logs/" --links --safe-links \
            $TOMCAT_BASE/deploy/ $MACHINE:$TOMCAT_BASE/deploy/
        checkReturnCode $? "Synchronizing tomcat at $DC"
        echo -e "[OK] Tomcat directories synchronized at $DC"


    for VM in $VMS
    do
        # Start tomcat
        MACHINE=ves-$DC-$VM
        echo "Starting tomcat at ${MACHINE}..."
        sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/start
        checkReturnCode $? "Starting tomcat at $MACHINE"
        echo -e "[OK] Started tomcat at ${MACHINE}\n"
    done
done