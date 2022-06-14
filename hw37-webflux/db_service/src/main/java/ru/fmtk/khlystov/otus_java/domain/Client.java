package ru.fmtk.khlystov.otus_java.domain;


import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public class Client {

    @Id
    private Long id;
    private String name;
    private Long addressId;
    @Transient
    private Address address;
    @Transient
    // Не работает. Не генерирует айдишники в phone
    //@MappedCollection(idColumn = "client_id", keyColumn = "id")
    private List<Phone> phones;

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        if (address != null) {
            this.addressId = address.getId();
        }
    }

    @PersistenceConstructor
    public Client(Long id, String name, Address address, Long addressId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.addressId = addressId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
        if (this.address != null && !Objects.equals(this.address.getId(), addressId)) {
            this.address.setId(addressId);
        }
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones={" + phones + '}' +
                '}';
    }
}
