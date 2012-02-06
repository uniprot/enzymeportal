#!/bin/bash
# Build java code and prepare environment variables to run the mega-mapper.

cd $(dirname $0)/..
mvn -DskipTests=true -P apps,dev clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

JAVA_OPTS='-Xms512M -Xmx1G'
