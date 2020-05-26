package com.github.webkunx.grpc.Calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Server started");
        Server grpcServer = ServerBuilder
                .forPort(50051)
                .addService(new CalculatorServiceImpl())
                .build();
        grpcServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received shutdown request");
            grpcServer.shutdown();
            System.out.println("grpc Server stopped");

        }));
        grpcServer.awaitTermination();

    }
}
