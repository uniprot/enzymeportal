#!/bin/bash
# Reads dbConfig, file directory and filename from a file.
# Param:
# $1: fileDirectory  /ebi/uniprot/production/enzyme_portal/sitemap

#SITEMAP_CONFIG=$(dirname $0)/sitemapProp.properties
#FILE_DIR=$(grep '^fileDir=' $SITEMAP_CONFIG | cut -d '=' -f 2)

FILE_DIR=/ebi/uniprot/production/enzyme_portal/sitemap


echo "About to copy sitemap to this directory /nfs/public/rw/uniprot/enzyme_portal/sitemap "

become uni_adm cp $FILE_DIR/sitemap-ep*.xml $FILE_DIR/sitemap_index.xml /nfs/public/rw/uniprot/enzyme_portal/sitemap

echo "Done Copying files. - $(date)"
echo "deleting the generated files - clean up"
rm -f $FILE_DIR/sitemap*
echo "Done cleaning up the directory"
echo "SUCCESS -$(date)"