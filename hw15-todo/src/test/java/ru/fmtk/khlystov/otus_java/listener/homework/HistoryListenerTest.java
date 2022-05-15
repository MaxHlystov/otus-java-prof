package ru.fmtk.khlystov.otus_java.listener.homework;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import ru.fmtk.khlystov.otus_java.model.Message;
import ru.fmtk.khlystov.otus_java.model.ObjectForMessage;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HistoryListenerTest {

    @Test
    void listenerTest() {
        //given
        var historyListener = new HistoryListener();

        final var id = 100L;
        final var data = "33";
        final var field13 = new ObjectForMessage();
        final var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message = new Message.Builder(id)
                .field10("field10")
                .field13(field13)
                .build();

        //when
        historyListener.onUpdated(message);
        message.getField13().setData(new ArrayList<>()); //меняем исходное сообщение
        field13Data.clear(); //меняем исходный список

        //then
        var messageFromHistory = historyListener.findMessageById(id);
        assertThat(messageFromHistory).isPresent();
        assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
    }
}