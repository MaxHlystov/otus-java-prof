package ru.fmtk.khlystov.otus_java.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;
import ru.fmtk.khlystov.otus_java.service.DbDataService;

@Controller
public class ClientController {

    private final DbDataService dbDataService;

    public ClientController(DbDataService dbDataService) {
        this.dbDataService = dbDataService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(dbDataService.findAll());
        model.addAttribute("clients", reactiveDataDrivenMode);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientDto());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public Mono<String> clientSave(@ModelAttribute final ClientDto client) {
        return dbDataService.save(client)
                .map((newClient) -> "redirect:/");
    }

}
