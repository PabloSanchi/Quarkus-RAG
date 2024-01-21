package org.rag.infrastructure.adapters.primary.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rag.domain.Document;
import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertDocumentCommand {
    @JsonProperty("collectionName")
    private String collectionName;

    @JsonProperty("document")
    private List<Document> documents;
}
