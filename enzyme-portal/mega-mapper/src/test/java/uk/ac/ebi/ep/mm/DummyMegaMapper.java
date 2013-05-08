package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A dummy implementation of MegaMapper which stores a few entries and xrefs in
 * memory, just for JUnit tests.
 * @author rafa
 * @since 2013-04-29
 */
public class DummyMegaMapper implements MegaMapper {

    private Collection<Entry> entries;
    private Collection<XRef> xrefs;

    public void openMap() throws IOException {
        if (entries == null) entries = new HashSet<Entry>();
        if (xrefs == null) xrefs = new HashSet<XRef>();
    }

    public void writeEntry(Entry entry) throws IOException {
        entries.add(entry);
    }

    public void writeEntries(Collection<Entry> entries) throws IOException {
        this.entries.addAll(entries);
    }

    public int updateEntry(Entry entry) throws IOException {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public void writeXref(XRef xref) throws IOException {
        xrefs.add(xref);
    }

    public void writeXrefs(Collection<XRef> xrefs) throws IOException {
        this.xrefs.addAll(xrefs);
    }

    public void write(Collection<Entry> entries, Collection<XRef> xrefs)
    throws IOException {
        writeEntries(entries);
        writeXrefs(xrefs);
    }

    public Entry getEntryForAccession(MmDatabase db, String accession) {
        for (Entry entry : entries) {
            if (entry.getEntryAccessions().contains(accession)){
                return entry;
            }
        }
        return null;
    }

    public Collection<XRef> getXrefs(Entry entry) {
        Collection<XRef> matching = new HashSet<XRef>();
        for (XRef xref : xrefs) {
            if (xref.getFromEntry().equals(entry)
                    || xref.getToEntry().equals(entry)){
                matching.add(xref);
            }
        }
        return matching;
    }

    public Collection<XRef> getXrefs(Entry entry, MmDatabase... dbs) {
        Collection<XRef> matching = new HashSet<XRef>();
        for (XRef xref : xrefs) {
            boolean from, to;
            from = xref.getFromEntry().equals(entry);
            to = xref.getToEntry().equals(entry);
            if (!from && !to) continue;
            for (MmDatabase db : dbs) {
                if (db.name().equals(from? xref.getToEntry().getDbName()
                        : xref.getFromEntry().getDbName())){
                    matching.add(xref);
                }
            }
        }
        return matching;
    }

    public Collection<XRef> getXrefs(Collection<Entry> entries, MmDatabase... db) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession, MmDatabase... xDb) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession, Relationship relationship) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Collection<XRef> getXrefs(MmDatabase db, String idFragment, Constraint constraint, Relationship relationship) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Collection<XRef> getXrefs(MmDatabase db, String idFragment, Constraint constraint, MmDatabase... xDbs) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public List<Entry> getChMBLEntries(MmDatabase db, String accession, MmDatabase... xDb) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public int getXrefsSize(MmDatabase db, String accession, MmDatabase... xDb) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public void handleError() throws IOException {
        // no-op
    }

    public void closeMap() throws IOException {
        // no-op
    }

    public void commit() throws IOException {
        // no-op
    }

    public void rollback() throws IOException {
        // no-op
    }

    public List<String> getAllUniProtAccessions(MmDatabase database) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Map<?, ?> getCompounds(MmDatabase db, String accession, MmDatabase... xDbs) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public Map<String, String> getDisease(MmDatabase db, String accessions, MmDatabase... xDbs) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public List<String> getAllEntryIds(MmDatabase database) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }

    public ResultSet getAllEntryIds(MmDatabase database, String query) {
        throw new UnsupportedOperationException(
                "This is just a dummy implementation");
    }
}
