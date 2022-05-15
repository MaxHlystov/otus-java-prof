package ru.fmtk.khlystov.otus_java.processor;

import ru.fmtk.khlystov.otus_java.model.Message;

public interface Processor {

    Message process(Message message);
}
