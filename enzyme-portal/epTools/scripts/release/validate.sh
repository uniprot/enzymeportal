#This will execute the latest release version of ep tools
#./validate.sh ${file location}/enzyme-portal-ebeye.xml http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd
#./validate.sh /ebi/uniprot/production/enzyme_portal/xml4ebies/2015_12/enzyme-portal-ebeye.xml http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd

#git checkout master
#cd enzyme-portal/epTools
#/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/runScriptWithLastRelease.sh \
   # scripts/validateXML.sh 


git checkout master
cd /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/runScriptWithLastRelease.sh /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/scripts/validateXML.sh