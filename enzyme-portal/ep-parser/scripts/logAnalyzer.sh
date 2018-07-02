#!/bin/bash

LOG_INPUT_DATA=/ebi/uniprot/production/enzyme_portal/tools/enzymeportal.log
LOG_OUTPUT_DIR=/ebi/uniprot/production/enzyme_portal/tools/

#note example used locally. 
#LOG_INPUT_DATA=/Users/joseph/ep-logs/2018/enzymeportal.log
#LOG_OUTPUT_DIR=/Users/joseph/ep-logs/2018/

echo
echo "**************************** W A R N I N G ****************************"
echo "Please ensure you have a log data in $LOG_INPUT_DATA and also an output dir $LOG_OUTPUT_DIR for final result."
echo "[INFO] Log input data = " $LOG_INPUT_DATA
echo "[INFO] Log output directory =" $LOG_OUTPUT_DIR
echo "[INFO] *******************************************************************"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok
WD=$(pwd)
cd $(dirname $0)/..
mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.parser.main.LogAnalyzer" -Dexec.args="$LOG_INPUT_DATA  $LOG_OUTPUT_DIR"
cd $WD
echo "[INFO] Finished analyzing enzyme portal log. Please see $LOG_OUTPUT_DIR for result  - $(date)"

