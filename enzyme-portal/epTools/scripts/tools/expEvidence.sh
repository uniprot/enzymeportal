#populate the enzyme portal database with computed experimental evidences
#The database (uzpdev|uzprel) must be passed as a parameter

DB_CONFIG=$1
SCRIPT_DIR=/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/scripts

$SCRIPT_DIR/checkParams.sh

git checkout master
cd /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/runScriptWithLastRelease.sh $SCRIPT_DIR/eev.sh $DB_CONFIG


rm -rf /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/evidence.log
cp /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/logs/analysis.log /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/evidence.log