package com.github.webkunx.grpc.Calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        doUnaryCall(channel);
        doServerStreamingCall(channel);
        doClientStreamingCall(channel);
        channel.shutdown();
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub client = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestStreamObserver = client.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Received response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        requestStreamObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(1).build());
        requestStreamObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(3).build());
        requestStreamObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(211).build());
        requestStreamObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(10).build());
        requestStreamObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(8).build());

        requestStreamObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doUnaryCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub client = CalculatorServiceGrpc
                .newBlockingStub(channel);

        PairOfNumbers pairOfNumbers = PairOfNumbers.newBuilder()
                .setA(12)
                .setB(228)
                .build();
        SumRequest request = SumRequest.newBuilder()
                .setPairOfNumbers(pairOfNumbers)
                .build();
        SumResponse response = client.sum(request);
        System.out.println(response.getResult());
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub client = CalculatorServiceGrpc
                .newBlockingStub(channel);
        PrimeNumberDecompositionRequest request = PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(232131829)
                .build();
        client.primeNumberDecomposition(request).forEachRemaining(primeNumberDecompositionResponse -> {
            System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
        });
    }

    public static void main(String[] args) {

        CalculatorClient main = new CalculatorClient();
        main.run();

    }

}
