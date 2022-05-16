package ru.fmtk.khlystov.otus_java.processor;

import ru.fmtk.khlystov.otus_java.model.Message;

public class ProcessorUpperField10 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder().field4(message.getField10().toUpperCase()).build();
    }
}
