#!/bin/bash

cd $(dirname $0)/..
mvn -P dev clean package

CP=.
for JAR in target/*.jar
do
    CP=$CP:$JAR
done
