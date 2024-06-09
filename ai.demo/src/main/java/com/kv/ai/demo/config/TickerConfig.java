package com.kv.ai.demo.config;

import com.kv.ai.demo.dto.TickerReq;
import com.kv.ai.demo.dto.TickerResp;
import com.kv.ai.demo.function.TickerSvc;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Log4j2
@Configuration
public class TickerConfig {

    @Value("${yahoo.url}")
    private String url;

    @Value("${yahoo.key}")
    private String key;

    @Value("${yahoo.param}")
    private String param;

    @Value("${yahoo.header_host}")
    private String header_host;

    @Bean
    @Description("Get ticker value from Yahoo.")
    public Function<TickerReq, TickerResp> tickerFunction(){
        log.info("URL: {} - KEY: {} - PARAM: {} X-HOST: {}", url, key, param, header_host);
        return new TickerSvc(url,key,param,header_host);
    }

}
