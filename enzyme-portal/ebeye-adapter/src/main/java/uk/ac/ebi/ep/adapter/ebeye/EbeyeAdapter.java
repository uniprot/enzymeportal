package uk.ac.ebi.ep.adapter.ebeye;

import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.Logger;
import uk.ac.ebi.ebinocle.webservice.ArrayOfEntryReferences;
import uk.ac.ebi.ebisearchservice.ArrayOfArrayOfString;
import uk.ac.ebi.ebisearchservice.ArrayOfString;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeCallable.GetEntriesCallable;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeCallable.GetResultsCallable;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeCallable.NumberOfResultsCaller;
import uk.ac.ebi.ep.adapter.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.adapter.ebeye.util.Transformer;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.result.Pagination;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EbeyeAdapter implements IEbeyeAdapter {

	private static final Logger LOGGER = Logger.getLogger(EbeyeAdapter.class);

	private EbeyeConfig config;

	public EbeyeConfig getConfig() {
		return config;
	}

	public void setConfig(EbeyeConfig config) {
		this.config = config;
	}

    private List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (
            List<ParamOfGetResults> paramList){
        List<Callable<ArrayOfArrayOfString>> callableList =
        		new ArrayList<Callable<ArrayOfArrayOfString>>();
        if (paramList.size() > 0) {
            for (ParamOfGetResults param : paramList) {
                List<Callable<ArrayOfArrayOfString>> callables =
                        prepareCallableCollection(param);
				if (callables.size()>0) {
					callableList.addAll(callables);
				}
			}
        }
        return callableList;
    }
    
    private List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (
            ParamOfGetResults param){
        List<Callable<ArrayOfArrayOfString>> callableList
                = new ArrayList<Callable<ArrayOfArrayOfString>>();
        int totalFound = param.getTotalFound();
        int size = config.getResultsLimit();
        //Work around to solve big result set issue
        Pagination pagination = new Pagination(totalFound, size);
        int nrOfQueries = pagination.getLastPage();
        int start = 0;
        for (int i = 0; i < nrOfQueries; i++) {
            if (i == nrOfQueries - 1 && (totalFound % size) > 0) {
                size = totalFound % size;
            }
            Callable<ArrayOfArrayOfString> callable;
            switch (Domains.valueOf(param.getDomain().replace('-', '_'))){
                case efo:
                case omim:
                case mesh:
                    callable = new EbeyeCallable.GetUniprotReferencesCallable(
                            param, start, size);
                    break;
                default:
                    callable = new GetResultsCallable(param, start, size);
            }
            callableList.add(callable);
            start = start + size;
        }
        return callableList;
    }

    private List<ArrayOfArrayOfString> executeCallables(
            List<Callable<ArrayOfArrayOfString>> callables) {
    	List<ArrayOfArrayOfString> ebeyeResultList =
    			new ArrayList<ArrayOfArrayOfString>();
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletionService<ArrayOfArrayOfString> ecs =
        		new ExecutorCompletionService<ArrayOfArrayOfString>(pool);
        try {
    		Map<Future<ArrayOfArrayOfString>, ArrayOfArrayOfString> future2aoaos =
    				new LinkedHashMap<Future<ArrayOfArrayOfString>, ArrayOfArrayOfString>();
        	for (Callable<ArrayOfArrayOfString> callable : callables) {
				future2aoaos.put(ecs.submit(callable), null);
			}
        	for (int i = 0; i < callables.size(); i++) {
				try {
					final Future<ArrayOfArrayOfString> taken =
							ecs.poll(config.getThreadTimeout(),
									TimeUnit.MILLISECONDS);
		        	if (taken != null){
						ArrayOfArrayOfString rawResults = taken.get();
						if (rawResults != null){
							future2aoaos.put(taken, rawResults);
						} else {
			        		LOGGER.warn("SEACH job result is null!");
						}
		        	} else {
		        		LOGGER.warn("SEACH future not retrieved!");
		        	}
				} catch (Exception e) {
                	// Don't stop the others
                	LOGGER.error("Callable " + (i+1) + " of " + callables.size()
                			+ " - " + e.getMessage(), e);
				}
			}
        	// Now finished, let's keep only successful futures:
        	for (Future<ArrayOfArrayOfString> future : future2aoaos.keySet()) {
				final ArrayOfArrayOfString aoaos = future2aoaos.get(future);
				if (aoaos != null){
					ebeyeResultList.add(aoaos);
				}
			}
        	LOGGER.debug("SEARCH after futures loop, successful = "
        			+ ebeyeResultList.size() + "/" + callables.size());
		} finally {
            pool.shutdown();
        }
        return ebeyeResultList;
    }

    public List<ParamOfGetResults> getNumberOfResults(
    		List<ParamOfGetResults> paramOfGetResults)
	throws MultiThreadingException {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletionService<Integer> ecs = new ExecutorCompletionService<Integer>(pool);
        try {
            Map<Future<Integer>, ParamOfGetResults> future2param =
            		new HashMap<Future<Integer>, ParamOfGetResults>();
            for (ParamOfGetResults param : paramOfGetResults) {
                Callable<Integer> callable = new NumberOfResultsCaller(param);
				future2param.put(ecs.submit(callable), param);
            }
            for (int i = 0; i < paramOfGetResults.size(); i++) {
				try {
					Future<Integer> future = ecs.poll(
							config.getThreadTimeout(),
							TimeUnit.MILLISECONDS);
					if (future != null){
	                    int totalFound = future.get();
	                    ParamOfGetResults param = future2param.get(future);
	                    param.setTotalFound(totalFound);
	                    params.add(param);
					}
				} catch (Exception e) {
                	// Don't stop the others
                	LOGGER.error("Future could not be retrieved", e);
				}
			}
        } finally {
            pool.shutdown();
        }
        return params;
    }

	public Map<String, String> getNameMapByAccessions(String domain,
			Collection<String> accessions) {
		Domains domains = Domains.valueOf(domain);
		List<String> configFields;
		switch (domains) {
		case chebi:
			configFields = IEbeyeAdapter.FieldsOfChebiNameMap.getFields();
			break;
		default:
			configFields = IEbeyeAdapter.FieldsOfUniprotNameMap.getFields();
		}
		ArrayOfString fields = Transformer
				.transformToArrayOfString(configFields);

		// TODO limited array size
		ArrayOfString idsArray = Transformer
				.transformToArrayOfString(accessions);

		GetEntriesCallable caller = new EbeyeCallable.GetEntriesCallable(
				domain, idsArray, fields);

		ArrayOfArrayOfString results = caller.callGetEntries();
        return Transformer.transformToMap(results);
	}

    public ParamOfGetResults getNumberOfResults(ParamOfGetResults param){
        NumberOfResultsCaller callable = new NumberOfResultsCaller(param);
        int totalFound = callable.getNumberOfResults();
        param.setTotalFound(totalFound);
        return param;
    }

	public Set<String> getValueOfFields(List<ParamOfGetResults> paramOfGetResults)
	throws MultiThreadingException {
		List<Callable<ArrayOfArrayOfString>> callableList =
				prepareCallableCollection(paramOfGetResults);
		List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
        return Transformer.transformFieldValueToList(rawResults, false);
	}
	
	public List<List<String>> getFields(ParamOfGetResults params){
		List<List<String>> results = null;
		getNumberOfResults(params);
		if (params.getTotalFound() > 0){
			GetResultsCallable callable =
					new GetResultsCallable(params, 0, params.getTotalFound());
			ArrayOfArrayOfString rawResults = callable.callGetResults(
					params.getDomain(), params.getQuery(), params.getFields(),
					0, params.getTotalFound());
			results = Transformer.transformToList(rawResults);
		}
		return results;
	}

    public Map<String, String> getMapOfFieldAndValue(List<ParamOfGetResults> params)
	throws MultiThreadingException{
        Map<String,String> fieldValueMap = new HashMap<String, String>();
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
        	// FIXME: submit one by one to a CompletionService,
        	// then loop again to take and get the Futures
            for (ParamOfGetResults param: params) {
                Callable<ArrayOfArrayOfString> callable = new GetResultsCallable(param, 0,1);
                Future<ArrayOfArrayOfString> future  = pool.submit(callable);
                ArrayOfArrayOfString rawResults;
                try {
                    rawResults = future.get(config.getThreadTimeout(),
                            TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (ExecutionException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                }
                 List<String> valueList =
                         new ArrayList<String>(Transformer.transformFieldValueToList(rawResults, false));
                 if (valueList.size() > 0) {
                     fieldValueMap.put(param.getQuery(), valueList.get(0));
                 }
            }
        } finally {
            pool.shutdown();
        }
        return fieldValueMap;
    }

    public List<String> getRelatedUniprotAccessionSet(List<ParamOfGetResults> paramOfGetResults)
	throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> upAccCallables =
                new ArrayList<Callable<ArrayOfArrayOfString>>();
        Set<String> accessionSet = new HashSet<String>();
        for (ParamOfGetResults param: paramOfGetResults)  {
            upAccCallables.addAll(prepareCallableCollection(param));
        }
        accessionSet.addAll(Transformer.transformFieldValueToList(
                executeCallables(upAccCallables), true));
        return new ArrayList<String>(accessionSet);
    }

    public Set<String> getValueOfFields(ParamOfGetResults paramOfGetResults) throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(paramOfGetResults);
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
         return Transformer.transformFieldValueToList(rawResults, false);
    }

    public Map<String, List<String>> getUniprotRefAccesionsMap(
    		ParamOfGetResults paramOfGetResults) throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(paramOfGetResults);
        List<ArrayOfArrayOfString> ids = executeCallables(callableList);
        
        EbeyeCallable.GetReferencedEntriesSet callable =
    			new EbeyeCallable.GetReferencedEntriesSet(
    					paramOfGetResults.getDomain(),
    					Transformer.transformToArrayOfString(ids),
    					Domains.uniprot.name(),
    					Transformer.transformToArrayOfString(
    					    FieldsOfUniprotNameMap.acc.name()));
        ArrayOfEntryReferences references = callable.callGetReferencedEntriesSet();
        return Transformer.transformToMap(references);
    }

}
