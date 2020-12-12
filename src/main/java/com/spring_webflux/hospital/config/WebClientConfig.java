package com.spring_webflux.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl("https://free.currconv.com/api/v7/convert")
//                .baseUrl("https://free.currconv.com/api/v7/convert?q=")
                .build();
    }
}
