package com.kv.ai.demo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Log4j2
public class AIConfig {

    @Value("classpath:/data/sample.json")
    private Resource data;

    private final static String vectorStoreName = "vectorstore.json";

    String[] tags = new String[]{"name", "author", "year"};

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        var vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(vectorStoreFile);
        } else {
            log.info("Vector Store File Not Exists, loading...");
            simpleVectorStore.add(new JsonReader(data, tags).get());
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;

    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }


}
