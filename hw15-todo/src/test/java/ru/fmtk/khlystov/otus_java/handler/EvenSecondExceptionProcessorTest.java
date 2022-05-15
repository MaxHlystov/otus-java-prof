package ru.fmtk.khlystov.otus_java.handler;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import ru.fmtk.khlystov.otus_java.model.Message;
import ru.fmtk.khlystov.otus_java.processor.EvenSecondExceptionProcessor;
import ru.fmtk.khlystov.otus_java.service.TimeService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EvenSecondExceptionProcessorTest {
    private static final LocalDateTime nowEven = LocalDateTime.of(2022, 5, 5, 13, 45, 12);
    private static final LocalDateTime nowOdd = nowEven.plusSeconds(1);

    @Test
    public void testEvenSecond_throwsException() {
        // given
        TimeService timeService = getConstTimeService(nowEven);
        EvenSecondExceptionProcessor processor = new EvenSecondExceptionProcessor(timeService);
        var message = new Message.Builder(1L).field1("field1").build();

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> processor.process(message));

        // then
        assertEquals("It's even second at 2022-05-05 13:45:12", exception.getMessage());
    }

    @Test
    public void testOddSecond_returnsMessage() {
        TimeService timeService = getConstTimeService(nowOdd);
        EvenSecondExceptionProcessor processor = new EvenSecondExceptionProcessor(timeService);
        var message = new Message.Builder(1L).field1("field1").build();

        // when
        Message newMsg = processor.process(message);

        // then
        assertEquals(message, newMsg);
    }

    private static TimeService getConstTimeService(LocalDateTime dateTime) {
        return () -> dateTime;
    }

}
