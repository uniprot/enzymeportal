/*
 * Deletes all cross references and entries for a given database
 * from the mega-map.
 * Parameter: &1 - the database name.
 */
delete from mm_entry where db_name = '&1';
