package org.acme.infrastructure.adapters.primary.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCommand {
    @JsonProperty("collectionName")
    private String collectionName;

    @JsonProperty("query")
    private String query;
}
