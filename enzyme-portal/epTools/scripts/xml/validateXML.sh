#validates an XML files against provided XSD files
#This is how to run this script
# ./validateXML.sh XML_FILE XSD_FILE
# e.g ./validateXML.sh /Users/joseph/ep-xml/enzyme-portal-ebeye.xml http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd



XML_FILE=$1
XSD_FILE=$2

#validate an xml file
echo "[INFO] Request to start validating XML File - $(date)"
echo "[INFO] *******************************************************************"
WD=$(pwd)
cd $(dirname $0)/../..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.eptools.main.ValidatorMain" -Dexec.cleanupDaemonThreads=false -Dexec.args="$XML_FILE $XSD_FILE"
cd $WD
echo "[INFO] Finished validating the XML file against the provided XSD  - $(date)"

