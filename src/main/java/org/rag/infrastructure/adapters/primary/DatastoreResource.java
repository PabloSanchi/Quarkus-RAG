package org.rag.infrastructure.adapters.primary;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.rag.application.services.DatastoreService;
import org.rag.domain.Document;
import org.rag.infrastructure.adapters.primary.command.CreateDeleteCommand;
import org.rag.infrastructure.adapters.primary.command.InsertDocumentCommand;
import org.rag.infrastructure.adapters.primary.command.SearchCommand;

import java.util.List;


@Path("/datastore")
public class DatastoreResource {

    private DatastoreService datastoreService;

    @Inject
    public DatastoreResource(DatastoreService datastoreService) {
        this.datastoreService = datastoreService;
    }

    @GET
    @Path("/list")
    public List<String> listCollections() {
        return this.datastoreService.listCollections();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createCollection(CreateDeleteCommand createDeleteCommand) {
        this.datastoreService.createCollection(createDeleteCommand.getCollectionName());
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteCollection( CreateDeleteCommand createDeleteCommand) {
        this.datastoreService.deleteCollection(createDeleteCommand.getCollectionName());
    }

    @POST
    @Path("/insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insertDocument(InsertDocumentCommand insertDocumentCommand) {
        this.datastoreService.insertDocuments(
                insertDocumentCommand.getCollectionName(),
                insertDocumentCommand.getDocuments()
        );
    }

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Document> search(SearchCommand searchCommand) {
        return this.datastoreService.search(searchCommand.getCollectionName(), searchCommand.getQuery());
    }
}

