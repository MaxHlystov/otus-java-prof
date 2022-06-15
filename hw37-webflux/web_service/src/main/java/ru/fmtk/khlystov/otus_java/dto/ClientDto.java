package ru.fmtk.khlystov.otus_java.dto;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
public class ClientDto {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String address;
    private String phones;

    public ClientDto() {
    }

    public ClientDto(Long id, String name, String address, String phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientDto clientDto = (ClientDto) o;
        if (id != null && clientDto.id != null && Objects.equals(id, clientDto.id)) {
            return true;
        }
        return Objects.equals(name, clientDto.name) && Objects.equals(address,
                clientDto.address) && Objects.equals(phones, clientDto.phones);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, address, phones);
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones=" + phones +
                '}';
    }
}
