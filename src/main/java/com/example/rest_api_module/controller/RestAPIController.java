package com.example.rest_api_module.controller;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.rest_api_module.listener.KafkaResponseListener;
import com.example.rest_api_module.service.KafkaProducerService;

@RestController
@RequestMapping("/api")
public class RestAPIController {    

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaResponseListener kafkaResponseListener;

    @GetMapping("/{operation}")
    public String calculateSum(@PathVariable String operation, @RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws InterruptedException {
        try {       
             operation = operation.toLowerCase();
            
             // Verify if operation is sum, subtract, multiplication or division
            if (!operation.equals("sum") && !operation.equals("subtraction") && !operation.equals("multiplication") && !operation.equals("division")) {
                return "Invalid operation; Possible commands: sum, subtraction, multiplication, division";
            }
            // Check if division is valid
            if (operation.equals("division")) { 
                if (BigDecimal.ZERO.compareTo(b) == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
            }
            
            // Create request message
            String request = String.format("{\"operation\":\"%s\", \"a\":%s, \"b\":%s}",operation,a.toString(),b.toString());
            // Send the request to Kafka
            kafkaProducerService.sendMessage(request);
            // Wait for the response
            CompletableFuture<String> future = kafkaResponseListener.listenForResponse();
            String result = future.join();
            return "{ 'result': " + result + " }";
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
