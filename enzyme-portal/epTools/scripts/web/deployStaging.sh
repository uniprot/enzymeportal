#!/bin/bash
# Copies a war file from the maven repo to the deploy directory and restarts
# the tomcat server cleanly to get the change.
# Parameter:
# $1: the version of the maven war artifact to deploy

[ -z $1 ] && echo "Please specify a version" && exit 1

. $(dirname $0)/variables.sh

MVN_REPO=~maven/public_html/m2repo
MVN_REPO_SNAPSHOTS=~maven/public_html/m2repo_snapshots
EP_WAR_PATH=uk/ac/ebi/ep/ep-website
XSLTPROC=xsltproc

if [[ "$1" == *SNAPSHOT* ]]
then
    # Version-TimeStamp-BuildNumber:
    VTSBN=${1/SNAPSHOT/}$($XSLTPROC $(dirname $0)/mavenSnapshot.xsl \
        $MVN_REPO_SNAPSHOTS/$EP_WAR_PATH/$1/maven-metadata.xml)
	EP_WAR=$MVN_REPO_SNAPSHOTS/$EP_WAR_PATH/$1/ep-website-${VTSBN}.war
else
	EP_WAR=$MVN_REPO/$EP_WAR_PATH/$1/ep-website-$1.war
fi

# Directory with war and context files for staging and LDCs servers:
DEPLOY_DIR=/nfs/public/rw/webadmin/tomcat/bases/uniprot/tc-uni-ep_staging/deploy

# Machine running the staging tomcat server:
MACHINE=ves-hx-95

# Stop tomcat
echo "Stopping tomcat at ${MACHINE}..."
sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/controller stop
#checkReturnCode $? "Stopping tomcat at $MACHINE"
sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/clean_dirs
# Have to wait a bit, otherwise the following commands will be faster:
sleep 10
echo -e "[OK] Stopped\n"

# Copy the war file to the deploy directory:

echo "copying $EP_WAR to the deploy directory... $DEPLOY_DIR"
#sudo -H -u $PROJECT_ADMIN cp $EP_WAR deploy
sudo -H -u $PROJECT_ADMIN cp $EP_WAR $DEPLOY_DIR
# Recreate the symbolic link to the new war file:
echo "re-creating the enzymeportal.war sym link..."
#sudo -H -u $PROJECT_ADMIN rm deploy/enzymeportal.war
#sudo -H -u $PROJECT_ADMIN ln -s $(basename $EP_WAR) deploy/enzymeportal.war

sudo -H -u $PROJECT_ADMIN rm $DEPLOY_DIR/enzymeportal.war
sudo -H -u $PROJECT_ADMIN ln -s $(basename $EP_WAR) $DEPLOY_DIR/enzymeportal.war

# Start tomcat
echo "Starting tomcat at ${MACHINE}..."
sudo -H -u $TOMCAT_ADMIN ssh $MACHINE $TOMCAT_BASE/bin/start
#checkReturnCode $? "Starting tomcat at $MACHINE"
echo -e "[OK] Started tomcat at ${MACHINE}\n"