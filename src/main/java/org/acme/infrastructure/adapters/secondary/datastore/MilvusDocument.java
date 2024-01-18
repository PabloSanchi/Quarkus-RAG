package org.acme.infrastructure.adapters.secondary.datastore;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MilvusDocument {
    private String title;
    private String content;
    private List<Float> embedding;
}
