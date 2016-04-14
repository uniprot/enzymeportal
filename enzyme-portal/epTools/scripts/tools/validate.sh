#This will execute the latest release version of ep tools
#./validate.sh ${file location}/enzyme-portal-ebeye.xml http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd
#./validate.sh /ebi/uniprot/production/enzyme_portal/xml4ebies/2015_12/enzyme-portal-ebeye.xml http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd

XML_FILE=$1
XSD_FILE=$2
echo  "XML FILES : $XML_FILE"
echo "XSD USED : $XSD_FILE"

git checkout master
cd /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/runScriptWithLastRelease.sh /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/scripts/xml/validateXML.sh $XML_FILE $XSD_FILE

rm -rf /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/validation.log
cp /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/logs/ep-tools.log /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/validation.log