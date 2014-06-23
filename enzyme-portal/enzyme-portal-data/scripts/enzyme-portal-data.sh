#!/bin/bash

#**************************************************
# Script Name:    enzyme-portal-data.sh
# Author:    Joseph Onwubiko
# Date Created:    5th June 2014
# Description:    This is the core script for re-generating the enzyme portal database.
#        The script can:
#        1. Truncate the ENZYME_PORTAL schema.
#        2. Rebuild UNIPROT_ENTRY, UNIPROT_XREF, ENZYME_SUMMARY, tables.
#        3. Parses various data sources such as UNIMED, ChEMBL, INTENZ xml files. This process also involves some web services queries.
#	 4. Populate DISEASE, COMPOUNDS AND REACTION INFORMATION

#
#        Expected run time for this procedure is up to 5hrs.
#

MM_SCRIPTS=$(cd $(dirname $0) && pwd)

EBINOCLE_DATA=/ebi/extserv/projects/ebinocle/data

CHEBI=$EBINOCLE_DATA/chebi/latest/chebi_prod.xml
INTENZ_XML=$EBINOCLE_DATA/intenz/latest/intenz.xml
#CHEMBL_TARGET=$EBINOCLE_DATA/chembl/latest/chembl-target_component.xml
CHEMBL_TARGET=$EBINOCLE_DATA/chembl-target_component/latest/chembl-target_component.xml
#UNIMED=http://research.isb-sib.ch/unimed/Swiss-Prot_mesh_mapping.html
UNIMED=http://research.isb-sib.ch/unimed/SP_MeSH.tab


EP_CONFIG_DIR=/ebi/uniprot/production/enzyme_portal/dbconfig


#ensure that db config is passed as param
. $MM_SCRIPTS/checkParams.sh
#mvn clean package
. $MM_SCRIPTS/mvnBuild.sh

DB_CONFIG="$1"

#first truncate the Schema and populate fresh data to the Database
echo "[INFO] Request to Truncate and populate the Enzyme Portal database - $(date)"
. $MM_SCRIPTS/enzyme_portal_uniprot.sh -r $DB_CONFIG -p ALL 
echo "[INFO] Enzyme Portal database (part-1) has been populate - $(date)"

echo "[INFO] About to start populating Enzyme Portal database (part-2) - $(date)"

#parse unimed_tsv file for diseases
echo "[INFO] Request to parse UNIMED File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $UNIMED
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.DiseaseFileParser" -Dexec.args="$DB_CONFIG $UNIMED"
cd $WD
echo "[INFO] Finished parsing unimed file and updating Enzyme Portal database with Diseases Data  - $(date)"


#parse chembl-target_component.xml for ChEMBL compounds
echo "[INFO] Request to parse chembl-target_component.xml File - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $CHEMBL_TARGET
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.ChEMBLXmlParser" -Dexec.args="$DB_CONFIG $CHEMBL_TARGET"
cd $WD
echo "[INFO] Finished parsing chembl-target_component.xml file and updating Enzyme Portal database with ChEMBL Compounds  - $(date)"






#parse uniprot data for ChEBI Compounds (activators & inhibitors)
echo "[INFO] Request to query & parse UniProt data for ChEBI compounds (Activators/Inhibitors)  - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.ChEBIParser" -Dexec.args="$DB_CONFIG"
cd $WD
echo "[INFO] Finished querying and parsing UniProt data and updating Enzyme Portal database with ChEBI Compounds  - $(date)"




#Parse intenz.xml file

echo "[INFO] Request to parse intenz.xml - $(date)"
echo "[INFO] The dbconfig passed as parameter = " $DB_CONFIG
echo "[INFO] The file to be parsed =" $INTENZ_XML
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.data.main.IntenzXmlParser" -Dexec.args="$DB_CONFIG $INTENZ_XML"
cd $WD
echo "[INFO] Finished parsing intenz.xml and updating Enzyme Portal database with ChEBI & Reaction (Rhea) Data  - $(date)"

#first truncate the Schema and populate fresh data to the Database
echo "[INFO] Request to gather release statistics from the Enzyme Portal database - $(date)"
. $MM_SCRIPTS/finish_release.sh -r $DB_CONFIG
echo "[INFO] Enzyme Portal database release is now complete - $(date)"
