#This computes the experimental evidence and write to a file
#This is how to run this script
# ./writeToFile.sh db /dir filename.extension true or false
# e.g ./writeToFile.sh uzpdev /evidence evidence.txt false
#The database (uzpdev|uzprel) must be passed as a parameter


DB_CONFIG=$1
FILE_DIR=$2
FILE_NAME=$3
DELETE_FILE=$4

SCRIPT_DIR=/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/scripts/evidence

$SCRIPT_DIR/checkParams.sh

git checkout master
cd /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/runScriptWithLastRelease.sh $SCRIPT_DIR/writeToFile.sh $DB_CONFIG $FILE_DIR $FILE_NAME $DELETE_FILE


rm -rf /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/evidence.log
cp /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/logs/analysis.log /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/logs/evidence.log