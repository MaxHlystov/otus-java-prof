package ru.fmtk.khlystov.otus_java.grpc.client;

import ru.fmtk.khlystov.otus_java.grpc.client.service.CurrentNumberFromServerServiceImpl;
import ru.fmtk.khlystov.otus_java.grpc.client.service.HwClientNumbersGenerator;
import ru.fmtk.khlystov.otus_java.grpc.client.service.NumberSequenceListenerImpl;
import ru.fmtk.khlystov.otus_java.grpc.client.service.TwistedComputation;

public class ClientApp {

    private static final int START_SERVER_VALUE = 0;
    public static final int END_SERVER_VALUE = 30;
    private static final int START_CLIENT_VALUE = 0;
    public static final int END_CLIENT_VALUE = 50;

    public static void main(String[] args) {
        try (CurrentNumberFromServerServiceImpl serverNumberService = new CurrentNumberFromServerServiceImpl(
                START_SERVER_VALUE, END_SERVER_VALUE)
        ) {
            HwClientNumbersGenerator generator = new HwClientNumbersGenerator(
                    START_CLIENT_VALUE, END_CLIENT_VALUE,
                    serverNumberService,
                    TwistedComputation::new);
            generator.generate(new NumberSequenceListenerImpl());
        }
    }
}
