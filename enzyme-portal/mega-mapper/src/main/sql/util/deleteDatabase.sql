/*
 * Deletes all accessions, cross references and entries for a given database
 * from the mega-map.
 * Parameter: &1 - the database name.
 */
delete from mm_accession ma
  where exists (select 1 from mm_entry where db_name = '&1' and id = ma.id);
delete from mm_xref x
  where exists (select 1 from mm_entry where db_name = '&1' and (id = x.from_entry or id = x.to_entry));
delete from mm_entry where db_name = '&1';
