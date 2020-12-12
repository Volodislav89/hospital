package com.spring_webflux.hospital.route;

import com.spring_webflux.hospital.service.CatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CatRoute {
    @Bean
    RouterFunction<ServerResponse> routerFunction(CatHandler catHandler) {
        return route(GET("/api/cat").and(accept(MediaType.APPLICATION_JSON)), catHandler::getAll)
                .andRoute(GET("/api/cat/{id}").and(accept(MediaType.APPLICATION_JSON)), catHandler::getById)
                .andRoute(GET("/api/cat/{in}/{out}/{sum}").and(accept(MediaType.APPLICATION_JSON)), catHandler::fromWebClient);
    }
}
