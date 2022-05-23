package ru.fmtk.khlystov.otus_java.crm.service;

import java.util.List;
import java.util.Optional;

import ru.fmtk.khlystov.otus_java.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
