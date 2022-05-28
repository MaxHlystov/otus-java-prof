package ru.fmtk.khlystov.otus_java.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.fmtk.khlystov.otus_java.domain.Address;
import ru.fmtk.khlystov.otus_java.domain.Client;
import ru.fmtk.khlystov.otus_java.domain.Phone;
import ru.fmtk.khlystov.otus_java.dto.ClientDto;

public class ClientToDtoConverter {
    private ClientToDtoConverter() {
    }

    public static ClientDto convert(Client client) {
        if (client == null) {
            return null;
        }
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        if (client.getAddress() != null) {
            dto.setAddress(client.getAddress().getStreet());
        }
        if (client.getPhones() != null && !client.getPhones().isEmpty()) {
            dto.setPhones(client.getPhones().stream()
                    .map(Phone::getNumber)
                    .collect(Collectors.joining(ClientDto.PHONE_SEPARATOR + " ")));
        }
        return dto;
    }

    public static Client convert(ClientDto dto) {
        if (dto == null) {
            return null;
        }
        Address address = dto.getAddress() == null ?
                null :
                new Address(null, dto.getAddress());
        List<Phone> phones = dto.getPhones() == null || dto.getPhones().isEmpty() ?
                Collections.emptyList() :
                Arrays.stream(dto.getPhones().split(ClientDto.PHONE_SEPARATOR))
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !"".equals(s.trim()))
                        .map(number -> new Phone(null, number, dto.getId()))
                        .collect(Collectors.toList());
        return new Client(dto.getId(), dto.getName(), address, phones);
    }
}
