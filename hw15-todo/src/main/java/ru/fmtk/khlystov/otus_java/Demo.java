package ru.fmtk.khlystov.otus_java;

import ru.fmtk.khlystov.otus_java.handler.ComplexProcessor;
import ru.fmtk.khlystov.otus_java.listener.ListenerPrinterConsole;
import ru.fmtk.khlystov.otus_java.model.Message;
import ru.fmtk.khlystov.otus_java.processor.LoggerProcessor;
import ru.fmtk.khlystov.otus_java.processor.ProcessorConcatFields;
import ru.fmtk.khlystov.otus_java.processor.ProcessorUpperField10;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
