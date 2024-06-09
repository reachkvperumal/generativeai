package com.kv.ai.demo.function;

import com.kv.ai.demo.dto.TickerReq;
import com.kv.ai.demo.dto.TickerResp;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@Log4j2
public class TickerSvc implements Function<TickerReq, TickerResp> {

    private final static String API_KEY = "X-RapidAPI-Key";

    private final static String X_RAPIDAPI_HOST = "x-rapidapi-host";

    private final RestClient yahoo;


    private final String key;


    private final String param;


    private final String header_host;

    public TickerSvc(String url, String key, String param, String header_host) {
        this.key = key;
        this.param = param;
        this.yahoo = RestClient.create(url);
        this.header_host = header_host;
    }

    @Override
    public TickerResp apply(TickerReq tickerReq) {
        log.info("tickerReq={}", tickerReq);
        String tickerResp = yahoo
                .get()
                .uri(String.format(param, tickerReq.symbol()))
                .header(API_KEY, key)
                .header(X_RAPIDAPI_HOST, header_host)
                .retrieve()
                .toEntity(String.class)
                .getBody();
        //log.info("tickerResp={}", tickerResp);
        return new TickerResp(tickerResp);
    }
}
