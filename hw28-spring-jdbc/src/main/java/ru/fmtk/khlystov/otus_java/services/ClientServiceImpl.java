package ru.fmtk.khlystov.otus_java.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.fmtk.khlystov.otus_java.domain.Address;
import ru.fmtk.khlystov.otus_java.domain.Client;
import ru.fmtk.khlystov.otus_java.domain.Phone;
import ru.fmtk.khlystov.otus_java.repostory.AddressRepository;
import ru.fmtk.khlystov.otus_java.repostory.ClientRepository;
import ru.fmtk.khlystov.otus_java.repostory.PhoneRepository;
import ru.fmtk.khlystov.otus_java.sessionmanager.TransactionManager;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final TransactionManager transactionManager;

    public ClientServiceImpl(ClientRepository clientRepository,
                             AddressRepository addressRepository,
                             PhoneRepository phoneRepository,
                             TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client findByName(String name) {
        return clientRepository.findByName(name).orElse(null);
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> {
            if (client.getAddress() != null && client.getAddress().getStreet() != null) {
                Optional<Address> maybeAddress = addressRepository.findFirstByStreet(client.getAddress().getStreet());
                if (maybeAddress.isPresent()) {
                    client.setAddressId(maybeAddress.get().getId());
                    log.info("found address: {}", maybeAddress.get());
                } else {
                    Address address = addressRepository.save(client.getAddress());
                    client.setAddressId(address.getId());
                    log.info("saved address: {}", address);
                }
            }
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            if (client.getPhones() != null && !client.getPhones().isEmpty()) {
                List<Phone> phones = client.getPhones().stream()
                        .map(phone -> {
                            phone.setClientId(client.getId());
                            return phoneRepository.save(phone);
                        })
                        .collect(Collectors.toList());
                log.info("saved phones: {}", phones);
            }
            return savedClient;
        });
    }
}
