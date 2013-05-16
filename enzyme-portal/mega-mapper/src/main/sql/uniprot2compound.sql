-- Table for quick retrieval of compounds related to UniProt entries.

-- Drop previous data:
drop index uniprot2compound_ind_upacc;
drop index uniprot2compound_ind_upid;
drop table uniprot2compound;

create table uniprot2compound as
SELECT DISTINCT MM_ENTRY2.ENTRY_NAME AS COMPOUND_NAME,
  MM_ENTRY2.DB_NAME                  AS COMPOUND_DB,
  MM_ENTRY2.ENTRY_ID                 AS COMPOUND_ID,
  MM_XREF1.RELATIONSHIP,
  MM_ENTRY.ENTRY_ID      AS UNIPROT_ID,
  MM_ENTRY.ENTRY_NAME    AS UNIPROT_NAME,
  MM_ACCESSION.ACCESSION AS UNIPROT_ACCESSION
FROM MM_ENTRY
INNER JOIN MM_XREF
ON MM_ENTRY.ID = MM_XREF.FROM_ENTRY
INNER JOIN MM_ENTRY MM_ENTRY1
ON MM_ENTRY1.ID = MM_XREF.TO_ENTRY
INNER JOIN MM_XREF MM_XREF1
ON MM_ENTRY1.ID = MM_XREF1.TO_ENTRY
INNER JOIN MM_ENTRY MM_ENTRY2
ON MM_ENTRY2.ID = MM_XREF1.FROM_ENTRY
INNER JOIN MM_ACCESSION
ON MM_ACCESSION.ID         = MM_ENTRY.ID
WHERE MM_ENTRY2.DB_NAME    = 'ChEBI'
AND MM_ENTRY.DB_NAME       = 'UniProt'
AND MM_ENTRY1.DB_NAME      = 'EC'
AND MM_ACCESSION.ACC_INDEX = 0
UNION
SELECT DISTINCT MM_ENTRY1.ENTRY_NAME AS COMPOUND_NAME,
  MM_ENTRY1.DB_NAME                  AS COMPOUND_DB,
  MM_ENTRY1.ENTRY_ID                 AS COMPOUND_ID,
  MM_XREF.RELATIONSHIP,
  MM_ENTRY.ENTRY_ID      AS UNIPROT_ID,
  MM_ENTRY.ENTRY_NAME    AS UNIPROT_NAME,
  MM_ACCESSION.ACCESSION AS UNIPROT_ACCESSION
FROM MM_ENTRY
INNER JOIN MM_XREF
ON MM_ENTRY.ID = MM_XREF.FROM_ENTRY
INNER JOIN MM_ENTRY MM_ENTRY1
ON MM_ENTRY1.ID = MM_XREF.TO_ENTRY
INNER JOIN MM_ACCESSION
ON MM_ACCESSION.ID          = MM_ENTRY.ID
WHERE MM_XREF.RELATIONSHIP IN ('is_target_of', 'is_inhibitor_of', 'is_activator_of')
AND MM_ACCESSION.ACC_INDEX  = 0;

comment on table uniprot2compound is
'Created from MM_ENTRY, MM_ACCESSION and MM_XREF for quick retrieval of
compounds related to a UniProt entry.';

comment on column uniprot2compound.compound_db is
'Cross references from ChEBI are either direct (inhibitors/activators) or
inferred from the EC classification (cofactors, reaction participants in
Rhea). Other databases - DrugBank, ChEMBL - are cross referenced with no
intermediate step.';

comment on column uniprot2compound.compound_id is
'The ID of the compound in the source database.';

comment on column uniprot2compound.compound_name is
'The name of the compound in the source database.';

comment on column uniprot2compound.relationship is
'The relationship between the compound and the UniProt entry (CV).';

comment on column uniprot2compound.uniprot_id is
'The UniProt ID ("entry name").';

comment on column uniprot2compound.uniprot_name is
'The recommended name of the UniProt entry.';

comment on column uniprot2compound.uniprot_accession is
'The UniProt accession (the primary one).';

-- Indexes to speed up selects:

CREATE index uniprot2compound_ind_upacc
ON UNIPROT2COMPOUND (UNIPROT_ACCESSION ASC);

create index uniprot2compound_ind_upid
on uniprot2compound (uniprot_id asc);
