package ru.fmtk.khlystov.otus_java.grpc.client.service;

import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public class TwistedComputation implements IntUnaryOperator {

    private final Supplier<Integer> numberSupplier;
    private int current;
    private int fromServer;

    public TwistedComputation(Supplier<Integer> numberSupplier) {
        this.numberSupplier = numberSupplier;
        this.current = 0;
        this.fromServer = 0;
    }

    @Override
    public int applyAsInt(int index) {
        int newValue = current;
        int newFromServer = numberSupplier.get();
        if (newFromServer != fromServer) {
            fromServer = newFromServer;
            newValue += fromServer;
        }
        current = newValue + 1;
        return current;
    }
}
