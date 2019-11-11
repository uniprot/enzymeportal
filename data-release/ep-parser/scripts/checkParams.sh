#!/bin/bash

#use this java setup
export JAVA_HOME=/nfs/public/rw/webadmin/java/jdks/latest_1.8
export PATH=$PATH:$JAVA_HOME/bin
source ~/.bashrc
echo $JAVA_HOME
echo $MAVEN_HOME
case $1 in
	uzpdev|uzprel);;
	*) echo 'runtime environment (uzpdev|uzprel) required as $1' && exit 1;;
esac
