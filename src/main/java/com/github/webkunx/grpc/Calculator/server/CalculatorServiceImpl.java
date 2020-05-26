package com.github.webkunx.grpc.Calculator.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collections;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        PairOfNumbers pairOfNumbers = request.getPairOfNumbers();
        int a = Math.toIntExact(pairOfNumbers.getA());
        int b = Math.toIntExact(pairOfNumbers.getB());

        int sum = a + b;

        SumResponse response = SumResponse.newBuilder()
                .setResult(sum)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        int number = (int) request.getNumber();
        int divisor = 2;

        while (number > 1) {
            if (number % divisor == 0) {
                number = number / divisor;
                PrimeNumberDecompositionResponse response = PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(divisor)
                        .build();
                responseObserver.onNext(response);
            } else {
                divisor = divisor + 1;
            }
        }
        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        final int[] sum = {0};
        final int[] count = {0};
        StreamObserver<ComputeAverageRequest> requestStreamObserver = new StreamObserver<ComputeAverageRequest>() {
            @Override
            public void onNext(ComputeAverageRequest value) {
                sum[0] += value.getNumber();
                count[0] += 1;
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ComputeAverageResponse.newBuilder()
                        .setResult((double) sum[0]/count[0])
                        .build());
            }
        };
        return requestStreamObserver;
    }

}
