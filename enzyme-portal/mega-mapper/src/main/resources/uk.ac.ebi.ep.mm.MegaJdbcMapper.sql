--insert.entry:\
INSERT INTO mm_entry (id, db_name, entry_id, entry_name) \
	VALUES (s_entry_id.nextval, ?, ?, ?)

--insert.accession:\
INSERT INTO mm_accession (id, accession, acc_index) VALUES (?, ?, ?)

--insert.xref:\
INSERT INTO mm_xref (id, from_entry, relationship, to_entry) \
	VALUES (s_xref_id.nextval, ?, ?, ?)
--update.entry:\
UPDATE MM_ENTRY SET ENTRY_NAME=? WHERE ENTRY_ID=?

--delete.entry:\
DELETE FROM mm_entry WHERE ID = ?

--delete.accessions:\
DELETE FROM mm_accession WHERE id = ?

--delete.xrefs:\
DELETE FROM mm_xref WHERE from_entry = ? OR to_entry = ?

--entry.by.id:\
SELECT * FROM mm_entry WHERE id = ?

--entry.by.entryid:\
SELECT * FROM mm_entry WHERE entry_id = ? AND db_name = ?

--AllUniProtAccessions.accession:\
SELECT ac.* FROM MM_ACCESSION ac, MM_ENTRY entry \
 WHERE ac.ID = entry.ID and entry.DB_NAME = ? 
--allEntry.by.dbName:\
SELECT entry.* FROM MM_ENTRY entry WHERE entry.DB_NAME = ?
--entry.by.accession:\
SELECT mme.* FROM mm_entry mme, mm_accession mma \
	WHERE mma.accession = ? AND mma.id = mme.id  AND mme.db_name = ?

--xref.by.id:\
SELECT * FROM mm_xref WHERE id = ?

--xref.by.entryids:\
SELECT mmx.* FROM mm_xref mmx, mm_entry mme1, mm_entry mme2 \
    WHERE mme1.db_name = ? AND mme1.entry_id = ? \
    AND mme1.id = mmx.from_entry AND mmx.to_entry = mme2.id \
    AND mme2.db_name = ? AND mme2.entry_id = ?

--xrefs.all.by.entry:\
SELECT * FROM mm_xref WHERE from_entry = ? OR to_entry = ?

--xrefs.by.entry:\
SELECT mmx.* FROM mm_xref mmx, mm_entry mme \
	WHERE (mmx.from_entry = ? AND mmx.to_entry = mme.id AND mme.db_name IN ({0})) \
	OR (mmx.to_entry = ? AND mmx.from_entry = mme.id AND mme.db_name IN ({0}))

--xrefs.all.by.accession:\
SELECT mmx.* FROM mm_xref mmx, mm_accession mma, mm_entry mme \
	WHERE mma.accession = ? AND mma.id = mme.id AND mme.db_name = ? \
	AND (mma.id = mmx.from_entry OR mma.id = mmx.to_entry)

--xrefs.by.accession:\
SELECT mmx.*,mme1.* FROM mm_accession mma, mm_entry mme1, mm_entry mme2, mm_xref mmx \
		WHERE mma.accession = ? AND mma.id = mme1.id AND mme1.db_name = ? \
		AND ((mma.id = mmx.from_entry AND mmx.to_entry = mme2.id AND mme2.db_name IN ({0})) \
		OR (mma.id = mmx.to_entry AND mmx.from_entry = mme2.id AND mme2.db_name IN ({0})))

--xrefs.by.accession.and.relationship:\
SELECT mmx.* FROM mm_accession mma, mm_xref mmx, mm_entry mme \
		WHERE mma.accession = ? AND mma.id = mme.id AND mme.db_name = ? \
		AND (mma.id = mmx.from_entry OR mma.id = mmx.to_entry) \
		AND mmx.relationship = ?

--entries.by.ChEMBL:\
SELECT mme2.* FROM mm_accession mma, mm_entry mme1, mm_entry mme2, mm_xref mmx \ 
		WHERE mma.accession = ? AND mma.id = mme1.id AND mme1.db_name = ? \
AND ((mme1.id = mmx.from_entry AND mmx.to_entry = mme2.id AND mme2.db_name IN ({0}) \
and mme2.entry_name is not null)) and rownum <= 5 order by mme2.entry_name asc

--xrefs.by.accession.total:\
SELECT COUNT(*)as rowcount FROM mm_accession mma, mm_entry mme1, mm_entry mme2, mm_xref mmx \
		WHERE mma.accession = ? AND mma.id = mme1.id AND mme1.db_name = ? \
		AND ((mma.id = mmx.from_entry AND mmx.to_entry = mme2.id AND mme2.db_name IN ({0})) \
		OR (mma.id = mmx.to_entry AND mmx.from_entry = mme2.id AND mme2.db_name IN ({0})))

--xrefs.by.id.fragment:\
SELECT mmx.* FROM mm_xref mmx /*\
	*/WHERE from_entry = (SELECT id FROM mm_entry mme1 WHERE entry_id {0} {1}) /*\
	*/OR to_entry = (SELECT id FROM mm_entry mme1 WHERE entry_id {0} {1}) {2}

--xrefs.by.id.fragment.and.db:\
select mmx.* from mm_xref mmx, mm_entry mme1, mm_entry mme2 /*\
	*/where (mme1.entry_id {0} {1} and mme2.db_name in ({2})) and ( /*\
	*/	(mme1.id = mmx.from_entry and mmx.to_entry = mme2.id) /*\
	*/	or /*\
	*/	(mme2.id = mmx.from_entry and mmx.to_entry = mme1.id) /*\
	*/)

--compounds.by.uniprot.id:\
select compound_name, compound_id, relationship from uniprot2compound /*\
  */where uniprot_id {0} and compound_name is not null order by relationship

--constraint.equals:\
= ?
--constraint.like:\
LIKE ? ESCAPE '\\'
--constraint.db:\
AND mme1.db_name = ?
--constraint.relationship:\
AND mmx.relationship = ?
