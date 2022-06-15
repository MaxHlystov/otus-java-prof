package ru.fmtk.khlystov.otus_java.services;

import java.util.List;

import ru.fmtk.khlystov.otus_java.domain.Client;

public interface ClientService {
    List<Client> findAll();

    Client findById(long id);

    Client findByName(String name);

    Client save(Client client);
}
