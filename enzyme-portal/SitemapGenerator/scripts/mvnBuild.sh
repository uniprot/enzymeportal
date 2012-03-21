#!/bin/bash
# Build the SiteMapgenerator using maven 




cd $(dirname $0)/..
mvn -DskipTests=false clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done

JAVA_OPTS='-Xms512M -Xmx1G'
