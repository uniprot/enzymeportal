PROJECT=uniprot

# Virtual users to manage LDCs:
PROJECT_ADMIN=uni_adm
TOMCAT_ADMIN=tc_uni01
ORACLE_ADMIN=uniprot-ora

# Database users and instances ** not used in this script***:
ORA_PROD_USER=enzyme_portal
ORA_PROD_INSTANCE=uzp
ORA_STAGE_USER=enzyme_portal
ORA_STAGE_INSTANCE=uzprel

TOMCAT_SERVER=tc-uni-ep_staging
TOMCAT_BASE=/nfs/public/rw/webadmin/tomcat/bases/$PROJECT/$TOMCAT_SERVER
EP_RW_BASE=/nfs/public/rw/uniprot/enzyme_portal

export JAVA_HOME=/nfs/public/rw/webadmin/java/jdks/jdk1.8.0_31

# Mail recipients:
DEVEL_MAILTO=joseph,srosanof