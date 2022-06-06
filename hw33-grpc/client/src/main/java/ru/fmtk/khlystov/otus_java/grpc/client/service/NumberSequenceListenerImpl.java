package ru.fmtk.khlystov.otus_java.grpc.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequenceListenerImpl implements NumberSequenceListener {
    private static final Logger log = LoggerFactory.getLogger(NumberSequenceListenerImpl.class);

    @Override
    public void onNext(int value) {
        log.info("Next value {}", value);
    }

    @Override
    public void onEnd() {
        log.info("End of number generation");
    }

    @Override
    public void onError(Exception exception) {
        log.error("Error in number generation", exception);
    }
}
