package ru.fmtk.khlystov.otus_java.listener;

import ru.fmtk.khlystov.otus_java.model.Message;

public interface Listener {

    void onUpdated(Message msg);
}
