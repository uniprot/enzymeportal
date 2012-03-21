#!/bin/bash
# Build the SiteMapgenerator using maven 

cd $(dirname $0)/..
mvn -DskipTests=true clean package
