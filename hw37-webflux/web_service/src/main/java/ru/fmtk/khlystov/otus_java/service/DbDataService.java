package ru.fmtk.khlystov.otus_java.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;

@Service
public class DbDataService {
    private final static Logger log = LoggerFactory.getLogger(DbDataService.class);

    private final WebClient client;

    public DbDataService(@NotNull WebClient.Builder builder,
                         @Value("${db.server.url}") String dbServerUrl) {
        client = builder
                .baseUrl(dbServerUrl)
                .build();
    }

    public Flux<ClientDto> findAll() {
        return client.get().uri("/api/client/all/")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ClientDto.class)
                .doOnNext(val -> log.info("val:{}", val));
    }

    public Mono<ClientDto> save(ClientDto clientDto) {
        return client.post().uri("/api/client")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(clientDto), ClientDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ClientDto.class)
                .doOnNext(val -> log.info("val:{}", val));
    }
}
