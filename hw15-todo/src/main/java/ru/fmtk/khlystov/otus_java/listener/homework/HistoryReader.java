package ru.fmtk.khlystov.otus_java.listener.homework;

import ru.fmtk.khlystov.otus_java.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
