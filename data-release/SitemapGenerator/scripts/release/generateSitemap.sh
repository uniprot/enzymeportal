#!/bin/bash
# Generates the Enzyme Portal sitemap using the latest stable version of SitemapGenerator module.
# This script should be run from the root of an EP working copy managed by git.

#[ ! -d .git ] && echo 'Not a git-managed working copy' && exit 1

# Make sure the current branch is not another tag (ep-parser, for example),
# as we need to have the SitemapGenerator pom file available in order to find
# its latest stable version:
#git checkout master
#cd enzyme-portal/SitemapGenerator
#/nfs/panda/production/steinbeck/scripts/runScriptWithLastRelease.sh \
    #scripts/sitemap.sh ep-mm-db-ezprel-ro \
    #/nfs/panda/production/steinbeck/ep/work sitemap-ep false

git checkout master
cd enzyme-portal/SitemapGenerator
/ebi/uniprot/production/enzyme_portal/enzyme_portal_release/runScriptWithLastRelease.sh \
    scripts/publishSitemap.sh 
