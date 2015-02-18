-- Drop all objects in the mega-map schema.

drop sequence S_ENTRY_ID;
drop sequence S_XREF_ID;

drop index MM_ACCESSION_INDEX;
drop index MM_DBNAME_INDEX;
drop index MM_FROMENTRY_INDEX;
drop index MM_TOENTRY_INDEX;
drop index MM_ENTRY_UK1;
drop index MM_XREF_UK1;

drop table MM_ACCESSION;
drop table MM_XREF;
drop table MM_ENTRY;
drop table UNIPROT2COMPOUND;
