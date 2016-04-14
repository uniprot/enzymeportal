#!/bin/bash
# Generates the Enzyme Portal sitemap using the latest stable version of SitemapGenerator module.

# Make sure the current branch is not another tag (ep-parser, for example),
# as we need to have the SitemapGenerator pom file available in order to find
# its latest stable version:
#git checkout master
#cd enzyme-portal/SitemapGenerator
#/nfs/panda/production/steinbeck/scripts/runScriptWithLastRelease.sh \
    #scripts/sitemap.sh ep-mm-db-ezprel-ro \
    #/nfs/panda/production/steinbeck/ep/work sitemap-ep false

#git checkout  master
#cd enzyme-portal/SitemapGenerator
#/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/runScriptWithLastRelease.sh \
   #/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/enzyme-portal/SitemapGenerator/scripts/publishSitemap.sh



git checkout  master
cd /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/tools/runScriptWithLastRelease.sh /ebi/uniprot/production/enzyme_portal/enzyme_portal_release/ep-releases/enzyme-portal/epTools/scripts/sitemap/publishSitemap.sh

