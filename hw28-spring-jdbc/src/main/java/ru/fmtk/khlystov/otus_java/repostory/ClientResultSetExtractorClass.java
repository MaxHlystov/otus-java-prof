package ru.fmtk.khlystov.otus_java.repostory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.fmtk.khlystov.otus_java.domain.Address;
import ru.fmtk.khlystov.otus_java.domain.Client;
import ru.fmtk.khlystov.otus_java.domain.Phone;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Client> clients = new HashMap<>();
        while (rs.next()) {
            Long clientId = (Long) rs.getObject("id");
            Client client = clients.get(clientId);
            if (client == null) {
                Long addressId = (Long) rs.getObject("address_id");
                String addressStr = rs.getString("address");
                Address address = addressId == null ? null : new Address(addressId, addressStr);
                client = new Client(clientId, rs.getString("name"), address, new ArrayList<>());
                clients.put(clientId, client);
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            String phoneNumber = rs.getString("phone");
            if (phoneId != null) {
                client.getPhones().add(new Phone(phoneId, phoneNumber, clientId));
            }
        }
        return clients.values().stream().toList();
    }
}
