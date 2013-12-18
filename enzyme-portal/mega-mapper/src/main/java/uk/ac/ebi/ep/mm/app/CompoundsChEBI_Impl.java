/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.*;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 *This class is to retrieve compounds from chebi database and write them to the mega mapper
 * @author joseph
 * @deprecated Now ChEBI cross-references are imported either by text-mining
 *      the UniProtXML for inhibitors/activators, or from IntEnzXML for
 *      cofactors and reaction participants.
 */
 @Deprecated
public class CompoundsChEBI_Impl   implements ICompoundsDAO {

    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CompoundsChEBI_Impl.class);
    private SessionFactory sessionFactory;
    private Session session = null;
    //private StatelessSession session = null;
    private Transaction transaction = null;
    private Entry chebiEntry;
    private Entry uniprotEntry;
    private XRef xRef;
    private DatabaseResources databaseResources;
    private String dbConfig;
    
  

   
  
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

    public CompoundsChEBI_Impl(String dbConfig) {
       this.init(dbConfig);
       this.dbConfig = dbConfig;
                
            }
        
    
    private void init(String dbConfig) {
         databaseResources = new DatabaseResources(dbConfig);
       sessionFactory = HibernateUtil.getSessionFactory();
    }

        public void buildCompound(){


        Set<CustomEntity> allCompounds = new HashSet<CustomEntity>();

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
            
            // Process the ones put into allCompounds from 5000·n to the end.
            // I am sure there are some, unless we have exactly 5000·n results.
            if (!allCompounds.isEmpty()) {
                processData(allCompounds);
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
        } catch (Exception ex) {
            Logger.getLogger(CompoundsChEBI_Impl.class.getName()).log(Level.SEVERE, null, ex);
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

  
    public void writeEntriesAndXrefs(Collection<Entry> entries, Collection<XRef> xRefs) throws IOException{
             try {
            this.writeEntries(entries);
            this.writeXrefs(xRefs);
        } catch (Exception ex) {
            LOGGER.fatal("Error while writing Chebi Compounds to Mega Mapper", ex);
        }
    }
    
      public void writeEntry(Entry entry) throws IOException{
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeEntry(entry);
    }
    public void writeEntries(Collection<Entry> entries) throws IOException {
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeEntries(entries);
    }
    
    public void writeXref(XRef ref) throws IOException{
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeXref(ref);
    }

    public void writeXrefs(Collection<XRef> xRefs) throws Exception {

           Connection con = OracleDatabaseInstance.getInstance(dbConfig).getConnection();

            MegaMapper    mapper = new MegaJdbcMapper(con);
                mapper.openMap();
            mapper.writeXrefs(xRefs);
          
     
            }


    }
