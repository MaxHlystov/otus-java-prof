package ru.fmtk.khlystov.otus_java.processor;

import ru.fmtk.khlystov.otus_java.model.Message;

public class Swap11And12FieldsProcessor implements Processor {
    @Override
    public Message process(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
