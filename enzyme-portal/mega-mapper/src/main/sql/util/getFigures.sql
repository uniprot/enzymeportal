column db_name format a10;
column from_db format a10;
column to_db format a10;
column relationship format a30;

select count(*) entries, db_name
  from mm_entry
  group by db_name;
select count(mma.accession) accessions, mme.db_name
  from mm_accession mma, mm_entry mme
  where mma.id = mme.id
  group by mme.db_name;
select count(mmx.id) xrefs, mme.db_name
  from mm_xref mmx, mm_entry mme
  where mmx.from_entry = mme.id or mmx.to_entry = mme.id
  group by db_name;
select mme1.db_name from_db, count(mmx.id) xrefs, mmx.relationship,
  mme2.db_name to_db
  from mm_entry mme1, mm_xref mmx, mm_entry mme2
  where mme1.id = mmx.from_entry
  and mmx.to_entry = mme2.id
  group by mme1.db_name, mmx.relationship, mme2.db_name
  order by mme1.db_name, mme2.db_name;
