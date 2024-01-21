package org.rag.application.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.rag.application.ports.DatastoreRepository;
import org.rag.domain.Document;

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

    public void insertDocuments(String collectionName, List<Document> document) {
        this.datastoreRepository.insertDocuments(collectionName, document);
    }

    public List<Document> search(String collectionName, String query) {
        return this.datastoreRepository.search(collectionName, query);
    }
}
