package com.spring_webflux.hospital.service;

import com.spring_webflux.hospital.model.Cat;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CatHandler {
    List<Cat> catList = new ArrayList<>();
    WebClient webClient;

    public CatHandler(WebClient webClient) {
        this.webClient = webClient;
        catList.add(new Cat(1L, "Puff", 3));
        catList.add(new Cat(2L, "Fluff", 2));
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        Flux<Cat> catFlux = Flux.fromIterable(catList);
        return ServerResponse
                .ok()
                .header("Token", "VR 123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(catFlux, Cat.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        Long catId = Long.valueOf(serverRequest.pathVariable("id"));
        System.out.println(serverRequest.queryParam("name").orElse("Hello"));
        System.out.println(serverRequest.headers());
        Mono<Cat> catMono = findById(catId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(catMono, Cat.class);
    }

    private Mono<Cat> findById(Long id) {
        return Mono.just(catList.stream().filter(c -> c.getId() == id).collect(Collectors.toList()).get(0));
    }

    public Mono<ServerResponse> saveCat(ServerRequest serverRequest) {
        Mono<Cat> catMono = serverRequest.bodyToMono(Cat.class);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(catMono, Cat.class);
//                .build();
    }

    public Mono<ServerResponse> fromWebClient(ServerRequest serverRequest) {
        ParameterizedTypeReference<Map<String, Double>> typeReference =
                new ParameterizedTypeReference<Map<String, Double>>() {};
        String getIn = serverRequest.pathVariable("in");
        String getOut = serverRequest.pathVariable("out");
        Integer sum = Integer.valueOf(serverRequest.pathVariable("sum"));
        Mono<Map<String, Double>> mono = webClient
                .get()
                .uri(String.format("?q=%s_%s&compact=ultra&apiKey=cb51f1f7ca77cca40ef7", getIn, getOut))
                .retrieve()
                .bodyToMono(typeReference);
       Mono<Double> mono1 = mono
                .map(x -> x.entrySet())
                .flatMapMany(Flux::fromIterable)
                .map(x -> x.getValue())
                .single()
                .map(x -> x * sum);
       Mono <Map> mapMono = mono.zipWith(mono1).map(x -> {
           HashMap<String, Double> hashMap = new HashMap<>();
           hashMap.put(String.format("%s_%s", getIn, getOut), x.getT2());
           return hashMap;
       });
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapMono, Map.class);
    }
}
