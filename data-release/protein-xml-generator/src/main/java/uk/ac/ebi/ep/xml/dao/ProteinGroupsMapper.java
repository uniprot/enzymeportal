package uk.ac.ebi.ep.xml.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;

/**
 *
 * @author joseph
 */
public class ProteinGroupsMapper implements RowMapper<ProteinGroups> { //ResultSetExtractor<ProteinGroups>{

//    @Override
//    public ProteinGroups extractData(ResultSet rs) throws SQLException, DataAccessException {
//         ProteinGroups pg = new ProteinGroups();
//       String name =  rs.getString("PROTEIN_NAME");
//        System.out.println("name "+ name);
//        AtomicInteger index = new AtomicInteger(0);
//        pg.setProteinName(name);
//        if(rs.next()){
//            System.out.println("DATA "+ rs.getString(index.incrementAndGet()));
//        }
//        return pg;
//    }
    @Override
    public ProteinGroups mapRow(ResultSet rs, int i) throws SQLException {
        ProteinGroups pg = new ProteinGroups();
        pg.setProteinName(rs.getString("PROTEIN_NAME"));
        String type = rs.getString("ENTRY_TYPE");
        pg.setEntryType(new BigInteger(type));

        pg.setProteinGroupId(rs.getString("PROTEIN_GROUP_ID"));
        //pg.setUniprotEntrySet(rs.getString(""));
        System.out.println("INDEX "+ i);
        AtomicInteger index = new AtomicInteger(0);
        String data = rs.getString("proteinGroupId");
        System.out.println("MY DATA "+ data);
//        if (rs.next()) {
//            System.out.println("DATA " + rs.getString(index.incrementAndGet()));
//        }

        return pg;

    }

}
