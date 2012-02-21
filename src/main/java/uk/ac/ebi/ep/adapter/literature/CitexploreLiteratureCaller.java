package uk.ac.ebi.ep.adapter.literature;

import java.util.Collection;
import java.util.concurrent.Callable;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.cdb.webservice.Citation;
import uk.ac.ebi.cdb.webservice.QueryException_Exception;

public class CitexploreLiteratureCaller implements Callable<Collection<Citation>> {

	private String query;

	CitexploreLiteratureCaller(String query){
		this.query = query;
	}
	
	public Collection<Citation> call()
	throws QueryException_Exception, Exception {
		CitexploreWSClient citexploreClient = null;
		try {
			citexploreClient = CitexploreWSClientPool.getInstance().borrowObject();
			return citexploreClient.searchCitations(query);
		} finally {
			if (citexploreClient != null){
				CitexploreWSClientPool.getInstance().returnObject(citexploreClient);
			}
		}
	}

}
