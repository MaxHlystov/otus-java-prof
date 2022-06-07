package ru.fmtk.khlystov.otus_java.grpc.client.service;

public interface CurrentNumberFromServerService extends AutoCloseable {
    int getCurrent();
    boolean start();
}
