package ru.fmtk.khlystov.otus_java.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.fmtk.khlystov.otus_java.converter.ClientToDtoConverter;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;
import ru.fmtk.khlystov.otus_java.services.ClientService;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<ClientDto> clients = clientService.findAll().stream()
                .map(ClientToDtoConverter::convert)
                .collect(Collectors.toList());
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientDto());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientDto client) {
        clientService.save(ClientToDtoConverter.convert(client));
        return new RedirectView("/", true);
    }

}
