package ru.fmtk.khlystov.otus_java.repostory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.fmtk.khlystov.otus_java.domain.Address;
import ru.fmtk.khlystov.otus_java.domain.Client;
import ru.fmtk.khlystov.otus_java.domain.Phone;

public class ClientResultSetToOptionalExtractorClass implements ResultSetExtractor<Optional<Client>> {
    @Override
    public Optional<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Client client = null;
        while (rs.next()) {
            if (client == null) {
                Long clientId = (Long) rs.getObject("id");
                Long addressId = (Long) rs.getObject("address_id");
                String addressStr = rs.getString("address");
                Address address = addressId == null ? null : new Address(addressId, addressStr);
                client = new Client(clientId, rs.getString("name"), address, new ArrayList<>());
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (phoneId != null) {
                String phoneNumber = rs.getString("phone");
                client.getPhones().add(new Phone(phoneId, phoneNumber, client.getId()));
            }
        }
        return Optional.ofNullable(client);
    }
}
