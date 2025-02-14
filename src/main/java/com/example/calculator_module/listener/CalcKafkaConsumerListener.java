package com.example.calculator_module.listener;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.calculator_module.model.CalculatorEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CalcKafkaConsumerListener {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CalcKafkaConsumerListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "request-topic")
    public void listen(String message) throws JsonMappingException, JsonProcessingException {
        System.out.println("Calc Received Message: " + message);

        // Parse the message and perform the calculation
        ObjectMapper mapper = new ObjectMapper();
        CalculatorEntity calculatorEntity = mapper.readValue(message, CalculatorEntity.class);
        BigDecimal res = calculatorEntity.doOp();
        
        System.out.println(String.valueOf(res));
        // Send the result back to Kafka with the correlation ID
        kafkaTemplate.send("response-topic", String.valueOf(res));
    }
}
