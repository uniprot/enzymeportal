#!/bin/bash
# Build java code and prepare environment variables to run the mega-mapper.
# Parameter:
# $1: profile(s) used (see pom.xml)

WD=$(pwd)


cd $(dirname $0)/..
mvn -DskipTests=true clean package

CP=$(pwd)
for JAR in $(pwd)/target/*.jar
do
    CP=$CP:$JAR
done

JAVA_OPTS='-Xms512M -Xmx1G'

cd $WD

