package ru.fmtk.khlystov.otus_java.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ru.fmtk.khlystov.otus_java.model.Message;
import ru.fmtk.khlystov.otus_java.service.TimeService;

public class EvenSecondExceptionProcessor implements Processor {
    private final TimeService timeService;
    private final DateTimeFormatter dateTimeFormatter;

    public EvenSecondExceptionProcessor(TimeService timeService) {
        if (timeService == null) {
            throw new IllegalArgumentException("Time service must not be null");
        }
        this.timeService = timeService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Message process(Message message) {
        LocalDateTime now = timeService.getNowDateTime();
        if ((now.getSecond() & 1) == 0) {
            throw new RuntimeException("It's even second at " + now.format(dateTimeFormatter));
        }
        return message.toBuilder().build();
    }
}
