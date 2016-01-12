#!/bin/bash
# This script must be run from a directory with a maven project, managed either
# by SVN or git.
# Switches the source code to the last released version (maven release) and runs
# the command passed as argument.
# Please note that any changes not commited in the source code will be lost by
# calling this script.
# Parameter:
# $1: command to run from the current directory.

if [ -e .svn ]
then
    VMS='svn'
else
    git branch &> /dev/null
    [ $? -eq 0 ] && VMS='git'
fi

[ -z "$VMS" ] && echo 'No SVN or git configuration found!' && exit 1
[ ! -e pom.xml ] && echo 'Not a maven project directory' && exit 2

. $(dirname $0)/functions.sh

XSLTPROC=$(which xsltproc)
MVN_COORD=$($XSLTPROC $(dirname $0)/mavenArtifact.xsl pom.xml)
MVN_ARTID=${MVN_COORD#*:}
MVN_PATH=$(echo $MVN_COORD | tr '.:' '/')
MVN_META=~maven/public_html/m2repo/$MVN_PATH/maven-metadata.xml
LAST_RELEASE=$($XSLTPROC $(dirname $0)/mavenLastRelease.xsl $MVN_META)
LAST_TAG=$MVN_ARTID-$LAST_RELEASE

echo "Switching to tag $LAST_TAG"
case "$VMS" in
    "svn")
        # The current svn URL in the working copy:
        SVN_URL=$(svn info | grep ^URL | sed 's/^URL: //')
        # The URL of the tag for the last maven release:
        TAG_URL=$(echo $SVN_URL | \
            sed 's/trunk\/.*//;s/tags\/.*//;s/branches\/.*//')tags/$LAST_TAG
        svn sw $TAG_URL
    ;;
    "git")
        # Go up to the repository root:
        while [ ! -d .git ]; do cd .. ; done
        git checkout $LAST_TAG
    ;;
esac

checkReturnCode $? "Switching source to tag $LAST_TAG"

# Run a script from this directory:
$@

# We cannot switch back yet, as we might be sending jobs to LSF!
#echo "Reverting to previous SVN URL $SVN_URL"
#svn sw $SVN_URL