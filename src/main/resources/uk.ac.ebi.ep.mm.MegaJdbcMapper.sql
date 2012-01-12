--insert.entry:\
INSERT INTO mm_entry (id, db_name, entry_id, entry_name) VALUES (?, ?, ?, ?)

--insert.accession:\
INSERT INTO mm_accession (id, accession, acc_index) VALUES (?, ?, ?)

--insert.xref:\
INSERT INTO mm_xref (id, from_entry, relationship, to_entry) VALUES (?, ?, ?, ?)

--delete.entry:\
DELETE FROM mm_entry WHERE ID = ?

--delete.accessions:\
DELETE FROM mm_accession WHERE id = ?

--delete.xrefs:\
DELETE FROM mm_xref WHERE from_entry = ? OR to_entry = ?

--entry.by.id:\
SELECT * FROM mm_entry WHERE id = ?

--entry.by.accession:\
SELECT mme.db_name, mme.entry_id, mme.entry_name \
	FROM mm_entry mme, mm_accession mma \
	WHERE mma.accession = ? AND mma.id = mme.id  AND mme.db_name = ?

--xref.by.id:\
SELECT * FROM mm_xref WHERE id = ?

--xrefs.all.by.entry:\
SELECT * FROM mm_xref WHERE from_entry = ? OR to_entry = ?

--xrefs.all.by.accession:\
SELECT mmx.* FROM mm_xref mmx, mm_accession mma, mm_entry mme \
	WHERE mma.accession = ? AND mma.id = mme.id AND mme.db_name = ? \
	AND (mma.id = mmx.from_entry OR mma.id = mmx.to_entry)

--xrefs.by.entry:\
SELECT mmx.* FROM mm_xref mmx, mm_entry mme \
	WHERE (mmx.from_entry = ? AND mmx.to_entry = mme.id AND mme.db_name IN ({0})) \
	OR (mmx.to_entry = ? AND mmx.from_entry = mme.id AND mme.db_name IN ({0}))

--xrefs.by.accession:\
SELECT mmx.* FROM mm_accession mma, mm_entry mme, mm_xref mmx \
		WHERE mma.accession = ? AND ( \
		(mma.id = mmx.from_entry AND mmx.to_entry = mme.id AND mme.db_name IN ({0})) \
		OR (mma.id = mmx.to_entry AND mmx.from_entry = mme.id AND mme.db_name IN ({0})))
