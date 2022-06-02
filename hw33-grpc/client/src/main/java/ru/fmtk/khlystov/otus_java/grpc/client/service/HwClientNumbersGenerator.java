package ru.fmtk.khlystov.otus_java.grpc.client.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwClientNumbersGenerator implements AutoCloseable {
    private static final long SLEEP_TIME_MILLS = TimeUnit.SECONDS.toMillis(1);
    private static final Logger log = LoggerFactory.getLogger(HwClientNumbersGenerator.class);

    private final CurrentNumberFromServerService serverNumberService;
    private final NumberSequenceListener listener;
    private final AtomicBoolean isStarted;
    private final int start;
    private final int end;
    private int current;
    private int fromServer;

    public HwClientNumbersGenerator(int start, int end,
                                    CurrentNumberFromServerService serverNumberService,
                                    NumberSequenceListener listener) {
        this.serverNumberService = serverNumberService;
        this.listener = listener;
        this.isStarted = new AtomicBoolean(false);
        this.start = start;
        this.end = end;
        this.current = 0;
        this.fromServer = 0;
    }

    public boolean start() {
        if (this.isStarted.compareAndSet(false, true)) {
            Thread thread = new Thread(getGenerator());
            if (!serverNumberService.start()) {
                log.error("Couldn't start server number generator");
                return false;
            }
            thread.start();
            return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        this.serverNumberService.close();
    }

    private Runnable getGenerator() {
        return () -> {
            try {
                for (int i = start; i <= end; i++) {
                    int newValue = current;
                    int newFromServer = serverNumberService.getCurrent();
                    if (newFromServer != fromServer) {
                        fromServer = newFromServer;
                        newValue += fromServer;
                    }
                    current = newValue + 1;
                    // log.info("Send to listener {}", current);
                    listener.onNext(current);
                    try {
                        Thread.sleep(SLEEP_TIME_MILLS);
                    } catch (InterruptedException e) {
                        listener.onError(e);
                        break;
                    }
                }
            } catch (Exception e) {
                listener.onError(e);
            } finally {
                listener.onEnd();
            }
        };
    }
}
