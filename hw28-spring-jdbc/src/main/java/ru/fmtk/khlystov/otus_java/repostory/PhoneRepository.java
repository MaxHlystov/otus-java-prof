package ru.fmtk.khlystov.otus_java.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.otus_java.domain.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
}
