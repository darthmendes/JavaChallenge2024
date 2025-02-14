package com.example.calculator_module.listener;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.calculator_module.model.CalculatorEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CalcKafkaConsumerListener {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CalcKafkaConsumerListener.class);

    public CalcKafkaConsumerListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "request-topic")
    public void listen(String message) throws JsonMappingException, JsonProcessingException {
        logger.info("Calc Received msg from Kafka: {}", message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            CalculatorEntity calculatorEntity = mapper.readValue(message, CalculatorEntity.class);
            BigDecimal res = calculatorEntity.doOp();
            
            kafkaTemplate.send("response-topic", String.valueOf(res));
            logger.info("Calc Sent result to Kafka: {}", res);
        } catch (Exception e) {
            logger.error("Calc Error processing message", e);
        }
    }
}
