package com.example.rest_api_module.listener;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaResponseListener {
    
    private CompletableFuture<String> future;
    
    public CompletableFuture<String> listenForResponse() {
        future = new CompletableFuture<>();
        return future;
    }

    @KafkaListener(topics = "response-topic")
    public void listen(String message) {
        System.out.println("Rest Received message: " + message);
        if (future != null) {
            future.complete(message);
        }
    }
}
