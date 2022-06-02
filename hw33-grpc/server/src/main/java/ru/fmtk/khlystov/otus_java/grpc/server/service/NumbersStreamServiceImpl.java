package ru.fmtk.khlystov.otus_java.grpc.server.service;

import io.grpc.stub.StreamObserver;
import ru.fmtk.khlystov.otus_java.protobuf.generated.GenerateRangeRequest;
import ru.fmtk.khlystov.otus_java.protobuf.generated.GenerateRangeResponse;
import ru.fmtk.khlystov.otus_java.protobuf.generated.NumbersStreamServiceGrpc;

public class NumbersStreamServiceImpl extends NumbersStreamServiceGrpc.NumbersStreamServiceImplBase {

    @Override
    public void range(GenerateRangeRequest request, StreamObserver<GenerateRangeResponse> responseObserver) {
        for (int i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            responseObserver.onNext(GenerateRangeResponse.newBuilder().setValue(i).build());
            if (i != request.getLastValue()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        responseObserver.onCompleted();
    }
}
