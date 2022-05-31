package ru.fmtk.khlystov.otus_java.repostory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.fmtk.khlystov.otus_java.domain.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Override
    @Query(value = """
            select c.id      as id,
                   c.name    as name,
                   a.id      as address_id,
                   a.street  as address,
                   ph.id     as phone_id,
                   ph.number as phone
            from client c
                     left join address a on c.address_id = a.id
                     left join phone ph on c.id = ph.client_id
            where c.id = :id
            """,
            resultSetExtractorClass = ClientResultSetToOptionalExtractorClass.class)
    Optional<Client> findById(@Param("id") Long id);

    Optional<Client> findByName(String Name);

    @Override
    @Query(value = """
            select c.id      as id,
                   c.name    as name,
                   a.id      as address_id,
                   a.street  as address,
                   ph.id     as phone_id,
                   ph.number as phone
            from client c
                     left join address a on c.address_id = a.id
                     left join phone ph on c.id = ph.client_id
            """,
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
