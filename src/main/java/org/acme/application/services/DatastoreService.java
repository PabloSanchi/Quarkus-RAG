package org.acme.application.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.ports.DatastoreRepository;
import org.acme.domain.Document;

import java.util.List;

@ApplicationScoped
public class DatastoreService {

    private DatastoreRepository datastoreRepository;

    @Inject
    public DatastoreService(DatastoreRepository datastoreRepository) {
        this.datastoreRepository = datastoreRepository;
    }

    public void createCollection(String name) {
        this.datastoreRepository.createCollection(name);
    }

    public void deleteCollection(String name) {
        this.datastoreRepository.deleteCollection(name);
    }

    public List<String> listCollections() {
        return this.datastoreRepository.listCollections();
    }

    public void insertDocument(String collectionName, Document document) {
        this.datastoreRepository.insertDocument(collectionName, document);
    }

    public List<Document> search(String collectionName, String query) {
        return this.datastoreRepository.search(collectionName, query);
    }
}
