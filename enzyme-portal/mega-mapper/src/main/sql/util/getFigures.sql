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