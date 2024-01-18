package org.acme.infrastructure.adapters.secondary.datastore;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.ShowCollectionsResponse;
import io.milvus.param.ConnectParam;
import io.milvus.param.R;
import io.milvus.param.collection.ShowCollectionsParam;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MilvusConnection {

    private MilvusServiceClient client;

    public MilvusConnection() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("localhost")
                .withPort(19530)
                .build();
        this.client = new MilvusServiceClient(connectParam);

        ShowCollectionsParam param = ShowCollectionsParam.newBuilder().build();
        R<ShowCollectionsResponse> response = this.client.showCollections(param);
    }

    public MilvusServiceClient getClient() {
        return this.client;
    }
}
