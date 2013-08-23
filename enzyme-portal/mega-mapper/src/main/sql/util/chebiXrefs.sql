-- This script generates a XML file to be consumed by ChEBI in order to
-- generate cross-references in their database.
-- These cross-references are needed to restrict chemical structure searches
-- to the enzyme portal.

-- Parameter:
-- &1: the output XML file

set serveroutput on
set feedback off
set termout off
set trimspool on

spool &1

declare
  cid uniprot2compound.compound_id%type := 'foo';
begin
  dbms_output.put_line('<doc>');
  dbms_output.put_line('<database_name>Enzyme Portal</database_name>');
  dbms_output.put_line('<database_description>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds. It brings together lots of diverse information about enzymes, and covers a large number of species including the key model organisms.</database_description>');
  dbms_output.put_line('<link_url>http://www.ebi.ac.uk/enzymeportal/search/*/enzyme</link_url>');
  dbms_output.put_line('<entities>');
  for rec in (select compound_id, uniprot_id, uniprot_accession, uniprot_name
              from uniprot2compound
              where compound_db = 'ChEBI'
              order by compound_id)
  loop
    if (rec.compound_id != cid)
    then
      if (cid != 'foo')
      then
        dbms_output.put_line('</xrefs>');
        dbms_output.put_line('</entity>');
      end if;
      dbms_output.put_line('<entity>');
      dbms_output.put_line('<chebi_id>' || rec.compound_id || '</chebi_id>');
      dbms_output.put_line('<xrefs>');
      cid := rec.compound_id;
    end if;
    dbms_output.put_line('  <xref>');
    dbms_output.put_line('    <display_id>' || rec.uniprot_id || '</display_id>');
    dbms_output.put_line('    <link_id>' || rec.uniprot_accession || '</link_id>');
    dbms_output.put_line('    <name>' || rec.uniprot_name || '</name>');
    dbms_output.put_line('  </xref>');
  end loop;
  dbms_output.put_line('</xrefs>');
  dbms_output.put_line('</entity>');
  dbms_output.put_line('</entities>');
  dbms_output.put_line('</doc>');

end;
/

spool off

exit

