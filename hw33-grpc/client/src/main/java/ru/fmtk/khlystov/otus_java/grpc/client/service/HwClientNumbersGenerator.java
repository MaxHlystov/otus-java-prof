package ru.fmtk.khlystov.otus_java.grpc.client.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwClientNumbersGenerator {
    private static final long SLEEP_TIME_MILLS = TimeUnit.SECONDS.toMillis(1);
    private static final Logger log = LoggerFactory.getLogger(HwClientNumbersGenerator.class);

    private final int start;
    private final int end;
    private final CurrentNumberFromServerService serverNumberService;
    private final NumberSequenceListener listener;
    private final Function<Supplier<Integer>, IntUnaryOperator> computationAlgorithmFactory;
    private final AtomicBoolean isStarted;

    public HwClientNumbersGenerator(final int start, final int end,
                                    final CurrentNumberFromServerService serverNumberService,
                                    final NumberSequenceListener listener,
                                    final Function<Supplier<Integer>, IntUnaryOperator> computationAlgorithmFactory) {
        this.start = start;
        this.end = end;
        this.serverNumberService = serverNumberService;
        this.listener = listener;
        this.computationAlgorithmFactory = computationAlgorithmFactory;
        this.isStarted = new AtomicBoolean(false);

    }

    public void generate() {
        if (this.isStarted.compareAndSet(false, true)) {
            if (!serverNumberService.start()) {
                log.error("Couldn't start server number generator");
                return;
            }
            final IntUnaryOperator computationAlgorithm =
                    computationAlgorithmFactory.apply(serverNumberService::getCurrent);
            try {
                for (int i = start; i <= end; ++i) {
                    listener.onNext(computationAlgorithm.applyAsInt(i));
                    try {
                        Thread.sleep(SLEEP_TIME_MILLS);
                    } catch (InterruptedException e) {
                        log.error("Generator has aborted");
                        break;
                    }
                }
            } catch (Exception e) {
                listener.onError(e);
            } finally {
                listener.onEnd();
            }
        }
    }
}
