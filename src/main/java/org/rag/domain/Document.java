package org.rag.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Document {
    private String title;
    private String content;

}
