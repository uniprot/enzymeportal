#This will execute the latest release version of ep tools

git checkout master
cd enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/runScriptWithLastRelease.sh \
    scripts/validateXML.sh 