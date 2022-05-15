package ru.fmtk.khlystov.otus_java.handler;

import ru.fmtk.khlystov.otus_java.model.Message;
import ru.fmtk.khlystov.otus_java.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
