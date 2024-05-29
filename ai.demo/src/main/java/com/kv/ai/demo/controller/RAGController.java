package com.kv.ai.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController()
public class RAGController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/template/prompt.st")
    private Resource promptTemplate;

    @GetMapping("/api/movie")
    @ResponseBody
    public String getMovieDetails(@RequestParam Optional<String> query) {
        List<Document> similaritySearch = vectorStore.similaritySearch(SearchRequest.query(query.orElseGet(() -> "Galaxy")).withTopK(3));
        List<String> content = similaritySearch.stream().map(Document::getContent).toList();
        PromptTemplate template = new PromptTemplate(promptTemplate);
        HashMap<String, Object> params = new HashMap<>();
        params.put("input", query);
        params.put("documents", String.join("\n", content));
        return chatClient.call(template.create(params)).getResult().getOutput().getContent();
    }
}
