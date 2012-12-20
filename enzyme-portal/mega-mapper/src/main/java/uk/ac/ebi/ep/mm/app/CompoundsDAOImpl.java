/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.*;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 *This class is to retrieve compounds from chebi database and write them to the mega mapper
 * @author joseph
 */
public class CompoundsDAOImpl extends DatabaseResources implements ICompoundsDAO {

    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DatabaseResources.class);
    private SessionFactory sessionFactory;
    private Session session = null;
    //private StatelessSession session = null;
    private Transaction transaction = null;
    private Entry chebiEntry;
    private Entry uniprotEntry;
    private XRef xRef;

    private enum MEGA_RELATIONSHIP {

        COFACTOR("CC - COFACTOR"), CATALYTIC_ACTIVITY("CC - CATALYTIC ACTIVITY"), ENZYME_REGULATION("CC - ENZYME REGULATION"), INDUCTION("CC - INDUCTION"),
        PHARMACEUTICAL("CC - PHARMACEUTICAL"), PTM("CC - PTM"), TOXIC_DOSE("CC - TOXIC DOSE");

        private MEGA_RELATIONSHIP(String name) {
            this.name = name;
        }
        private String name;

        public String getName() {
            return name;
        }

        public static MEGA_RELATIONSHIP getEnumFromString(String inputString) {
            if (inputString != null) {
                for (MEGA_RELATIONSHIP theRelationship : MEGA_RELATIONSHIP.values()) {
                    if (inputString.equalsIgnoreCase(theRelationship.getName())) {
                        return theRelationship;
                    }
                }
            }

            throw new IllegalArgumentException("No constant with name " + inputString + " found");


        }
    }

    public CompoundsDAOImpl(String dbConfig) {
        super(dbConfig);
        this.init();
    }

    private void init() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void getCHEBI_Compounds() {


        Set<CustomEntity> allCompounds = new HashSet<CustomEntity>();
        List<XRef> xRefList = new ArrayList<XRef>();
        Compounds compounds = null;
        Reference reference = null;


        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        transaction.begin();


//
//        String queryString3 = "SELECT * FROM CHEBI.COMPOUNDS c, CHEBI.REFERENCE r WHERE c.ID = r.COMPOUND_ID"
//                + " AND (r.LOCATION_IN_REF = 'CC - COFACTOR' OR r.LOCATION_IN_REF = 'CC - CATALYTIC ACTIVITY') AND ROWNUM <= 200";
//
//
//        String queryString1 = "select c.chebiAccession,c.name,r.reference_id,r.reference_name,r.location_in_ref"
//                + " FROM Reference r, Compounds c where c.id = r.compound_id and "
//                + "(r.location_in_ref = 'CC - COFACTOR' OR r.location_in_ref = 'CC - CATALYTIC ACTIVITY') AND ROWNUM <= 1000";
//
//        String queryString2 = "select c,r"
//                + " FROM Reference r, Compounds c where c.id = r.compound_id and "
//                + "(r.location_in_ref = 'CC - COFACTOR' OR r.location_in_ref = 'CC - CATALYTIC ACTIVITY')";


        String queryString = "select new uk.ac.ebi.ep.mm.app.CustomEntity(c.chebiAccession,c.name,r.reference_id,r.reference_name,r.location_in_ref)"
                + " FROM Reference r, Compounds c where c.id = r.compound_id and "
                + "(r.location_in_ref = 'CC - COFACTOR' OR r.location_in_ref = 'CC - CATALYTIC ACTIVITY')";


        Query query = session.createQuery(queryString).setCacheMode(CacheMode.IGNORE).setCacheable(false);
        query.setFetchSize(5000);


        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        int count = 0;
        while (results.next()) {
            CustomEntity entity = (CustomEntity) results.get(0);


            if (++count % 50 == 0) {

                session.flush();
                session.clear();
            }


            allCompounds.add(entity);
            if (allCompounds.size() >= 5000) {
                processData(allCompounds);
                allCompounds.clear();
            }

            session.evict(entity);
            session.evict(chebiEntry);
            session.evict(uniprotEntry);
            session.evict(xRef);

        }

        session.close();


    }

    private void processData(Set<CustomEntity> allCompounds) {
        List<XRef> xRefList = new ArrayList<XRef>();
        for (CustomEntity customEntity : allCompounds) {


            chebiEntry = computeChebiEntry(customEntity.getCompounds().getChebiAccession(), customEntity.getCompounds().getName());

            uniprotEntry = computeUniprotEntry(customEntity.getReference().getReference_id(), customEntity.getReference().getReference_name());

            xRef = computeXref(chebiEntry, uniprotEntry, customEntity.getRelationship());

            xRefList.add(xRef);


        }

        try {
            
            LOGGER.info("About to Write to Mega Mapper Database....");
            this.writeXrefs(xRefList);
            LOGGER.info("Done writing.");
        } catch (IOException ex) {
            Logger.getLogger(CompoundsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String buildRelationship(String relations) {


        MEGA_RELATIONSHIP mega_relationship = MEGA_RELATIONSHIP.getEnumFromString(relations);
        Relationship relationship = null;// Relationship.is_related_to;


        switch (mega_relationship) {
            case CATALYTIC_ACTIVITY:
                relationship = Relationship.is_substrate_or_product_of;
                break;
            case COFACTOR:
                relationship = Relationship.is_cofactor_of;
                break;
            case ENZYME_REGULATION:
                relationship = Relationship.is_related_to;
                break;
            case INDUCTION:
                relationship = Relationship.is_related_to;
                break;
            case PHARMACEUTICAL:
                relationship = Relationship.is_related_to;
                break;
            case PTM:
                relationship = Relationship.is_related_to;
                break;
            case TOXIC_DOSE:
                relationship = Relationship.is_related_to;
                break;

        }



        return relationship.name();

    }

    // use this generic method to return the value of the enum
    public static <T extends Enum<T>> T getValueOfEnum(Class<T> enumeration, String name) {
        for (T enumValue : enumeration.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("There is no value with name '" + name + " in Enum " + enumeration.getClass().getName());
    }

    public Entry computeChebiEntry(String chebiAccession, String entryName) {

        Entry entry = new Entry();
        entry.setDbName(MmDatabase.ChEBI.name());
        entry.setEntryId(chebiAccession);
        entry.setEntryName(entryName);


        return entry;
    }

    public Entry computeUniprotEntry(String reference_id, String reference_name) {

        Entry entry = new Entry();
        entry.setDbName(MmDatabase.UniProt.name());
        entry.setEntryId(reference_id);
        entry.setEntryName(reference_name);


        return entry;
    }

    public XRef computeXref(Entry chebiEntry, Entry uniprotEntry, String location_in_ref) {
        String relationship = buildRelationship(location_in_ref);
        XRef ref = new XRef();
        ref.setRelationship(relationship);
        ref.setFromEntry(chebiEntry);
        ref.setToEntry(uniprotEntry);

        return ref;
    }

    @Override
    void writeChebiCompounds(Collection<Entry> entries, Collection<XRef> xRefs) {
        try {
            this.writeEntries(entries);
            this.writeXrefs(xRefs);
        } catch (IOException ex) {
            LOGGER.fatal("Error while writing Chebi Compounds to Mega Mapper", ex);
        }
    }
}
