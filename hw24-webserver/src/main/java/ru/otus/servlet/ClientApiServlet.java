package ru.otus.servlet;

import java.io.IOException;
import java.util.Collections;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fmtk.khlystov.otus_java.crm.model.Address;
import ru.fmtk.khlystov.otus_java.crm.model.Client;
import ru.fmtk.khlystov.otus_java.crm.service.DBServiceClient;
import ru.otus.model.ClientDto;

public class ClientApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientDto clientDto = extractClientDtoFromRequest(req);
        if (clientDto == null || clientDto.getName() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client must has name");
            return;
        }
        Client clientToSave = convertDtoToClient(clientDto);
        Client clientSaved = dbServiceClient.saveClient(clientToSave);

        resp.setContentType("application/json;charset=UTF-8");
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.print(gson.toJson(clientSaved));
        }
    }

    private ClientDto extractClientDtoFromRequest(HttpServletRequest request) throws IOException {
        return gson.fromJson(request.getReader(), ClientDto.class);
    }

    private Client convertDtoToClient(ClientDto clientDto) {
        Address address = clientDto.getAddress() == null ? null : new Address(null, clientDto.getAddress());
        return new Client(null, clientDto.getName(), address, Collections.emptyList());
    }
}
