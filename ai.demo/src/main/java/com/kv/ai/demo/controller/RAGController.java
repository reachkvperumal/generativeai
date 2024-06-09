package com.kv.ai.demo.controller;

import com.kv.ai.demo.dto.TickerReq;
import com.kv.ai.demo.dto.TickerResp;
import com.kv.ai.demo.service.YahooSvc;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Log4j2
@RestController()
@RequestMapping("api")
public class RAGController {
    //Retrieval-augmented generation (RAG)

    private final ChatClient chatClient;

    /*@Value("classpath:/template/prompt.st")
    private Resource promptTemplate;*/

    @Autowired
    private YahooSvc yahooSvc;


    public RAGController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a customer chat support agent of a bank named "MyBank". 
                        Respond in a friendly, helpful, and joyful manner.
                        Before providing information about the transaction inquiry, you MUST always
                        get the following information from the user: date or description or amount.
                        If stock related question is asked, valid stock ticker must be present.                                       
                        """)
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .defaultFunctions("tickerFunction")
                .build();
    }

    @GetMapping("/payload")
    @ResponseBody
    public Flux<String> getPayloadDetails(@RequestParam Optional<String> query) {
       /* List<Document> similaritySearch = vectorStore.similaritySearch(SearchRequest.query(query.orElseGet(() -> "Galaxy")).withTopK(3));
        List<String> content = similaritySearch.stream().map(Document::getContent).toList();
        PromptTemplate template = new PromptTemplate(promptTemplate);
        // Map<String, Object> prompt = Map.of("input", query, "documents", String.join("\n", content));
        return chatClient.prompt(template.create(), OpenAiChatOptions.builder().withFunction("tickerFunction").build()).call().chatResponse().getResult().getOutput().getContent();*/
        return chatClient
                .prompt()
                .user(query.orElseGet(() -> "AMAZON"))
                .stream()
                .content();
    }

    @GetMapping("/ticker")
    public TickerResp getTicker(@RequestParam Optional<String> query) {
        return yahooSvc.apply(new TickerReq(query.orElseGet(() -> "GOOG")));
    }
}
