package com.example.rest_api_module.controller;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @GetMapping("/{operation}")
    public String calculateSum(@PathVariable String operation, @RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws InterruptedException {
        logger.info("Rest Received request off {}: a={}, b={}",operation, a, b);

        String req_id = UUID.randomUUID().toString();
        MDC.put("requestId", req_id);
        logger.info("Generated request ID: {}", req_id);


        operation = operation.toLowerCase();
        try {       
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
            String request = String.format("{\"requestId\":%s,\"operation\":\"%s\", \"a\":%s, \"b\":%s}",req_id,operation,a.toString(),b.toString());
            kafkaProducerService.sendMessage(request, req_id);
            // Wait for the response
            CompletableFuture<String> future = kafkaResponseListener.listenForResponse(req_id);
            String result = future.join();
            return ResponseEntity.ok()
                    .header("requestId", req_id)
                    .body("{ \"result\": " + result + " }")
                    .getBody();
        } catch (Exception e) {
            logger.error("Rest Error processing request {} : {}", req_id, e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            MDC.clear();
        }
    }
}
