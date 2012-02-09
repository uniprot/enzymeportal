#!/bin/bash
# Build java code and prepare environment variables to run the mega-mapper.
# Parameter:
# $1: profile(s) used (see pom.xml)

PROFILE_OPT=${$1:-}

cd $(dirname $0)/..
mvn -DskipTests=true $PROFILE_OPT clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

JAVA_OPTS='-Xms512M -Xmx1G'
