package org.acme.infrastructure.adapters.secondary.datastore;

import io.milvus.grpc.DataType;
import io.milvus.grpc.SearchResultData;
import io.milvus.grpc.SearchResults;
import io.milvus.grpc.ShowCollectionsResponse;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.*;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.ports.DatastoreRepository;
import org.acme.domain.Document;
import org.acme.infrastructure.utils.DocumentToMilvus;
import org.acme.infrastructure.utils.EmbeddingUtil;

import java.util.List;

@ApplicationScoped
public class DatastoreRepositoryMilvus implements DatastoreRepository {

    private MilvusConnection connection;
    private EmbeddingUtil embeddingUtil;
    private DocumentToMilvus documentToMilvus;

    @Inject
    public DatastoreRepositoryMilvus(MilvusConnection connection, EmbeddingUtil embeddingUtil, DocumentToMilvus documentToMilvus) {
        this.connection = connection;
        this.embeddingUtil = embeddingUtil;
        this.documentToMilvus = documentToMilvus;
    }

    @Override
    public void createCollection(String name) {

        List<FieldType> fieldsSchema = List.of(
                FieldType.newBuilder()
                        .withName("pk")
                        .withDataType(DataType.Int64)
                        .withPrimaryKey(true)
                        .withAutoID(true)
                        .build(),
                FieldType.newBuilder()
                        .withName("title")
                        .withDataType(DataType.VarChar)
                        .withMaxLength(500)
                        .build(),
                FieldType.newBuilder()
                        .withName("content")
                        .withDataType(DataType.VarChar)
                        .withMaxLength(500)
                        .build(),
                FieldType.newBuilder()
                        .withName("embedding")
                        .withDataType(DataType.FloatVector)
                        .withDimension(768)
                        .build()
        );

        CreateCollectionParam schema = CreateCollectionParam.newBuilder()
                .withCollectionName(name)
                .withFieldTypes(fieldsSchema)
                .build();

        R<RpcStatus> ret = this.connection.getClient().createCollection(schema);
        if (ret.getStatus() != R.Status.Success.getCode()) {
            throw new RuntimeException("Failed to create collection! Error: " + ret.getMessage());
        }

        this.buildIndex(name);
    }

    @Override
    public void deleteCollection(String name) {
        DropCollectionParam collection = DropCollectionParam.newBuilder()
                .withCollectionName(name).build();

        this.connection.getClient().dropCollection(collection);
    }

    @Override
    public List<String> listCollections() {
        R<ShowCollectionsResponse> respShowCollections = this.connection.getClient().showCollections(
                ShowCollectionsParam.newBuilder().build()
        );
        return respShowCollections.getData().getCollectionNamesList();
    }

    @Override
    public void insertDocument(String collectionName, Document document) {
        MilvusDocument milvusDocument = MilvusDocument.builder()
                .title(document.getTitle())
                .content(document.getContent())
                .embedding(this.embeddingUtil.encode(document.getContent()))
                .build();

        this.connection.getClient().insert(this.documentToMilvus.execute(collectionName, milvusDocument));
    }

    @Override
    public List<Document> search(String collectionName, String query) {
        this.connection.getClient().loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName(collectionName)
                        .build()
        );

        Integer SEARCH_K = 2;
        String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":5}";
        List<List<Float>> searchVectors = List.of(this.embeddingUtil.encode(query));

        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(collectionName)
                .withMetricType(MetricType.L2)
                .withOutFields(List.of("title", "content"))
                .withTopK(SEARCH_K)
                .withVectors(searchVectors)
                .withVectorFieldName("embedding")
                .withParams(SEARCH_PARAM)
                .build();

        R<SearchResults> respSearch = this.connection.getClient().search(searchParam);
        SearchResultData results = respSearch.getData().getResults();

        System.out.println(results.toString());
        return null;
    }

    private void buildIndex(String collectionName) {
        IndexType INDEX_TYPE = IndexType.IVF_FLAT;
        String INDEX_PARAM = "{\"nlist\":1024}";

        R<RpcStatus> ret = this.connection.getClient().createIndex(
                CreateIndexParam.newBuilder()
                        .withCollectionName(collectionName)
                        .withFieldName("embedding")
                        .withIndexType(INDEX_TYPE)
                        .withMetricType(MetricType.COSINE)
                        .withExtraParam(INDEX_PARAM)
                        .build()
        );

        if (ret.getStatus() != R.Status.Success.getCode()) {
            throw new RuntimeException("Failed to create index on vector field! Error: " + ret.getMessage());
        }
    }

}
