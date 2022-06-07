package ru.fmtk.khlystov.otus_java.grpc.client.service;

public interface NumberSequenceListener {
    void onNext(int value);

    void onEnd();

    void onError(Exception exception);
}
