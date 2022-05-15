package ru.fmtk.khlystov.otus_java.listener;

import ru.fmtk.khlystov.otus_java.model.Message;

public class ListenerPrinterConsole implements Listener {

    @Override
    public void onUpdated(Message msg) {
        var logString = String.format("oldMsg:%s", msg);
        System.out.println(logString);
    }
}
