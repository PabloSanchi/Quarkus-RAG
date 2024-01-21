package org.acme.infrastructure.utils;

import io.milvus.param.dml.InsertParam;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.infrastructure.adapters.secondary.datastore.MilvusDocument;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DocumentToMilvus {

    public InsertParam execute(String name, List<MilvusDocument> milvusDocuments) {

        List<InsertParam.Field> fields = new ArrayList<>();
        fields.add(new InsertParam.Field("title", milvusDocuments.stream().map(MilvusDocument::getTitle).toList()));
        fields.add(new InsertParam.Field("content", milvusDocuments.stream().map(MilvusDocument::getContent).toList()));
        fields.add(new InsertParam.Field("embedding", milvusDocuments.stream().map(MilvusDocument::getEmbedding).toList()));

        return InsertParam.newBuilder()
                .withCollectionName(name)
                .withFields(fields)
                .build();
    }
}
