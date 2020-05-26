package com.github.webkunx.grpc.Greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello grpc");

        Server grpcServer = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();
        grpcServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Received shutdown request");
            grpcServer.shutdown();
            System.out.println("grpc Server stopped");

        }));
        grpcServer.awaitTermination();
    }
}