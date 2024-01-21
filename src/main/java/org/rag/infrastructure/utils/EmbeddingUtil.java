package org.rag.infrastructure.utils;

import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class EmbeddingUtil {

    private final String MODEL_NAME = "sentence-transformers/paraphrase-multilingual-mpnet-base-v2";
    private Criteria<String, float[]> criteria;

    public EmbeddingUtil() {
        this.criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optModelUrls(
                        "djl://ai.djl.huggingface.pytorch/" + this.MODEL_NAME)
                .optEngine("PyTorch")
                .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                .optProgress(new ProgressBar())
                .build();
    }

    public List<Float> encode(String text) {
        try (ZooModel<String, float[]> model = criteria.loadModel(); Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] textEmbedding = predictor.predict(text);

            return IntStream.range(0, textEmbedding.length)
                    .mapToObj(i -> textEmbedding[i]).toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
