package ru.fmtk.khlystov.otus_java.grpc.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ServerBuilder;
import ru.fmtk.khlystov.otus_java.grpc.server.service.NumbersStreamServiceImpl;

public class ServerApp {
    private static final Logger log = LoggerFactory.getLogger(ServerApp.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var remoteService = new NumbersStreamServiceImpl();
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteService).build();
        server.start();
        log.info("Server waiting for client connections...");
        server.awaitTermination();
    }
}
