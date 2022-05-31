package ru.fmtk.khlystov.otus_java.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fmtk.khlystov.otus_java.converter.ClientToDtoConverter;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;
import ru.fmtk.khlystov.otus_java.services.ClientService;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public ClientDto getClientById(@PathVariable(name = "id") long id) {
        return ClientToDtoConverter.convert(clientService.findById(id));
    }

    @GetMapping("/api/client")
    public ClientDto getClientByName(@RequestParam(name = "name") String name) {
        return ClientToDtoConverter.convert(clientService.findByName(name));
    }

    @PostMapping("/api/client")
    public ClientDto saveClient(@Valid @RequestBody ClientDto clientDto) {
        return ClientToDtoConverter.convert(
                clientService.save(ClientToDtoConverter.convert(clientDto)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/client/all/")
    public List<ClientDto> getAllClients() {
        return clientService.findAll().stream()
                .map(ClientToDtoConverter::convert)
                .collect(Collectors.toList());
    }

}
