/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.unisave.repositories;

import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.repository.query.CouchbaseEntityInformation;
import org.springframework.data.couchbase.repository.support.MappingCouchbaseEntityInformation;

/**
 *
 * @author Joseph
 * @param <T> entity type
 * @param <S> entity ID type
 */
public abstract class Helpers<T, S> {

    protected CouchbaseEntityInformation<T, S> getCouchbaseEntityInformation(CouchbaseOperations couchbaseOperations, Class<T> entity) {
        CouchbasePersistentEntity<T> itemPersistenceEntity = (CouchbasePersistentEntity<T>) couchbaseOperations.getConverter()
                .getMappingContext()
                .getPersistentEntity(entity);

        return new MappingCouchbaseEntityInformation<>(itemPersistenceEntity);

    }
}
