package org.rag.infrastructure.adapters.primary.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeleteCommand {
    @JsonProperty("collectionName")
    private String collectionName;
}
