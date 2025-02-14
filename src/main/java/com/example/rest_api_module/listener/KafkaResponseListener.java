package com.example.rest_api_module.listener;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaResponseListener {
    
    private CompletableFuture<String> future;

    private static final Logger logger = LoggerFactory.getLogger(KafkaResponseListener.class);
    
    public CompletableFuture<String> listenForResponse() {
        future = new CompletableFuture<>();
        return future;
    }

    @KafkaListener(topics = "response-topic")
    public void listen(String message) {
        logger.info("Rest Received message from Kafka: {}", message);
        if (future != null) {
            future.complete(message);
        }
    }
}
