package org.acme.application.ports;

import org.acme.domain.Document;

import java.util.List;

public interface DatastoreRepository {
    public void createCollection(String name);
    public void deleteCollection(String name);

    public List<String> listCollections();

    public void insertDocuments(String collectionName, List<Document> document);
    public List<Document> search(String collectionName, String query);
}
