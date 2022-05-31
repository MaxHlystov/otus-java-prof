package ru.fmtk.khlystov.otus_java.repostory;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.otus_java.domain.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Optional<Address> findFirstByStreet(String street);
}
