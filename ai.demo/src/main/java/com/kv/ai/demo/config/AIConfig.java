package com.kv.ai.demo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Paths;

@Configuration
@Log4j2
public class AIConfig {

    @Value("classpath:/data/sample.json")
    private Resource data;

    private final static String vectorStoreName = "txCredit.json";

    private final static File FILE_NAME = new File(Paths.get("ai.demo","src", "main", "resources", "data")
            .toFile().getAbsolutePath() + File.separator + vectorStoreName);

    private final static String[] tags = new String[]{"name", "author", "year"};

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        if (FILE_NAME.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(FILE_NAME);
        } else {
            log.info("Vector Store File Not Exists, loading...");
            simpleVectorStore.add(new JsonReader(data, tags).get());
            simpleVectorStore.save(FILE_NAME);
        }
        return simpleVectorStore;

    }
}
