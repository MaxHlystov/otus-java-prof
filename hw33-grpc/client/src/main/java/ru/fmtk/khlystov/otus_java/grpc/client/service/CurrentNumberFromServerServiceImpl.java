package ru.fmtk.khlystov.otus_java.grpc.client.service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fmtk.khlystov.otus_java.protobuf.generated.GenerateRangeRequest;
import ru.fmtk.khlystov.otus_java.protobuf.generated.GenerateRangeResponse;
import ru.fmtk.khlystov.otus_java.protobuf.generated.NumbersStreamServiceGrpc;

public class CurrentNumberFromServerServiceImpl implements CurrentNumberFromServerService {
    private static final Logger log = LoggerFactory.getLogger(CurrentNumberFromServerServiceImpl.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private final int startValue;
    private final int endValue;
    private final AtomicInteger currentValue;
    private final AtomicBoolean isStarted;
    private ManagedChannel channel;

    public CurrentNumberFromServerServiceImpl(int startValue, int endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = new AtomicInteger(0);
        this.isStarted = new AtomicBoolean(false);
    }

    @Override
    public int getCurrent() {
        return this.currentValue.get();
    }

    @Override
    public boolean start() {
        if (!this.isStarted.compareAndSet(false, true)) {
            return false;
        }
        this.channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        final NumbersStreamServiceGrpc.NumbersStreamServiceStub stub =
                NumbersStreamServiceGrpc.newStub(channel);


        log.info("Send request numbers from {} to {}", startValue, endValue);
        stub.range(
                GenerateRangeRequest.newBuilder()
                        .setFirstValue(startValue)
                        .setLastValue(endValue)
                        .build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(GenerateRangeResponse value) {
                        currentValue.set(value.getValue());
                        log.info("Receive number {}", value.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("Error when getting numbers from server", t);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("The end.");
                    }
                });
        return true;
    }

    @Override
    public void close() {
        channel.shutdown();
    }
}
