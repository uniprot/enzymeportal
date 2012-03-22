-- Script to generate tab-delimited statistics for spreadsheet.
-- Parameter:
-- &1: output file

set termout off
set heading off
set feedback off
set serveroutput off
set trimspool on
--set trimout on

spool &1

select 'Entries' || chr(9) || 'DB' from dual;
select count(*) || chr(9) || db_name
  from enzyme_portal.mm_entry
  group by db_name order by db_name;

select 'Accessions' || chr(9) || 'DB' from dual;
select count(mma.accession) || chr(9) || mme.db_name
  from enzyme_portal.mm_accession mma, enzyme_portal.mm_entry mme
  where mma.id = mme.id
  group by mme.db_name order by mme.db_name;

select 'Xrefs' || chr(9) || 'DB' from dual;
select count(mmx.id) || chr(9) || mme.db_name
  from enzyme_portal.mm_xref mmx, enzyme_portal.mm_entry mme
  where mmx.from_entry = mme.id or mmx.to_entry = mme.id
  group by db_name order by db_name;

spool off
exit

