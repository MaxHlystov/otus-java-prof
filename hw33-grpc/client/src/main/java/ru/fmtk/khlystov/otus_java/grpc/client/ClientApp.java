package ru.fmtk.khlystov.otus_java.grpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fmtk.khlystov.otus_java.grpc.client.service.CurrentNumberFromServerServiceImpl;
import ru.fmtk.khlystov.otus_java.grpc.client.service.HwClientNumbersGenerator;
import ru.fmtk.khlystov.otus_java.grpc.client.service.NumberSequenceListenerImpl;

public class ClientApp {

    private static final int START_SERVER_VALUE = 0;
    public static final int END_SERVER_VALUE = 30;
    private static final int START_CLIENT_VALUE = 0;
    public static final int END_CLIENT_VALUE = 50;

    private static final Logger log = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) throws Exception {
        final CurrentNumberFromServerServiceImpl serverNumberService = new CurrentNumberFromServerServiceImpl(
                START_SERVER_VALUE, END_SERVER_VALUE);
        final NumberSequenceListenerImpl listener = new NumberSequenceListenerImpl();
        try(HwClientNumbersGenerator generator = new HwClientNumbersGenerator(
                START_CLIENT_VALUE, END_CLIENT_VALUE, serverNumberService, listener)) {

            if (generator.start()) {
                log.info("Start generation of numbers");
                listener.await();
                log.info("End of number generation");
            } else {
                log.error("We couldn't start generation");
            }
        }
    }
}
