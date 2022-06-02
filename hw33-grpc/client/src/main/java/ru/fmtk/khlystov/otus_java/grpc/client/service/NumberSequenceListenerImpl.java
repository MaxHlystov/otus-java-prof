package ru.fmtk.khlystov.otus_java.grpc.client.service;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequenceListenerImpl implements NumberSequenceListener {
    private static final Logger log = LoggerFactory.getLogger(NumberSequenceListenerImpl.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onNext(int value) {
        log.info("Next value {}", value);
    }

    @Override
    public void onEnd() {
        latch.countDown();
    }

    @Override
    public void onError(Exception exception) {
        log.error("Error in number generation", exception);
        latch.countDown();
    }

    public void await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Generation interrupted");
        }
    }
}
