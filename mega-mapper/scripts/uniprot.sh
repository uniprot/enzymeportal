#!/bin/bash

#DOWNLOAD_BASE=ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete
DOWNLOAD_BASE=/ebi/extserv/projects/ebinocle/data/uniprot/latest
SWISSPROT=$DOWNLOAD_BASE/uniprot_sprot.xml
TREMBL=$DOWNLOAD_BASE/uniprot_trembl.xml

XSL=$(dirname $0)/../src/main/resources/uniprot2species-tab.xsl

xsltproc $XSL $SWISSPROT > SwissProt2species.tab
