package ru.fmtk.khlystov.otus_java.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.otus_java.converter.ClientToDtoConverter;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;
import ru.fmtk.khlystov.otus_java.services.ClientService;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/api/client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDto> getClientById(@PathVariable(name = "id") long id) {
        var future = CompletableFuture
                .supplyAsync(() -> ClientToDtoConverter.convert(clientService.findById(id)), executor);
        return Mono.fromFuture(future);
    }

    @GetMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDto> getClientByName(@RequestParam(name = "name") String name) {
        var future = CompletableFuture
                .supplyAsync(() -> ClientToDtoConverter.convert(clientService.findByName(name)), executor);
        return Mono.fromFuture(future);
    }

    @PostMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDto> saveClient(@Valid @RequestBody ClientDto clientDto) {
        var future = CompletableFuture
                .supplyAsync(() -> ClientToDtoConverter.convert(
                        clientService.save(ClientToDtoConverter.convert(clientDto))), executor);
        return Mono.fromFuture(future);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/client/all/",
            produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<List<ClientDto>> getAllClients() {
        var future = CompletableFuture
                .supplyAsync(() -> clientService.findAll().stream()
                        .map(ClientToDtoConverter::convert)
                        .collect(Collectors.toList()), executor);
        return Mono.fromFuture(future);
    }

}
