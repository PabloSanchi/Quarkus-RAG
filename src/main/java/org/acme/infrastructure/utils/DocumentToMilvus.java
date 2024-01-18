package org.acme.infrastructure.utils;

import io.milvus.param.dml.InsertParam;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.infrastructure.adapters.secondary.datastore.MilvusDocument;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DocumentToMilvus {

    public InsertParam execute(String name, MilvusDocument milvusDocument) {

        List<InsertParam.Field> fields = new ArrayList<>();
        fields.add(new InsertParam.Field("title", List.of(milvusDocument.getTitle())));
        fields.add(new InsertParam.Field("content", List.of(milvusDocument.getContent())));
        fields.add(new InsertParam.Field("embedding", List.of(milvusDocument.getEmbedding())));

        return InsertParam.newBuilder()
                .withCollectionName(name)
                .withFields(fields)
                .build();
    }
}

