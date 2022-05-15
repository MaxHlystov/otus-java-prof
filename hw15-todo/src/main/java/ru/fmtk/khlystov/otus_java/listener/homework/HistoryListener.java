package ru.fmtk.khlystov.otus_java.listener.homework;

import java.util.LinkedHashMap;
import java.util.Optional;

import ru.fmtk.khlystov.otus_java.listener.Listener;
import ru.fmtk.khlystov.otus_java.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final LinkedHashMap<Long, Message> messageById = new LinkedHashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageById.put(msg.getId(), msg.toBuilder().build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageById.get(id));
    }
}
