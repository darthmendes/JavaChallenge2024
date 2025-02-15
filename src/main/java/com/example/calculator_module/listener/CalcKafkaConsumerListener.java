package com.example.calculator_module.listener;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.calculator_module.model.CalculatorEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Service
public class CalcKafkaConsumerListener {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CalcKafkaConsumerListener.class);

    public CalcKafkaConsumerListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "request-topic")
    public void listen(String message, String Id) throws JsonMappingException, JsonProcessingException {            
        Map<String, Object> payload = parseMessage(message);
        String requestId = (String) payload.get("requestId");

        String operation = (String) payload.get("operation");
        BigDecimal a = new BigDecimal((String) payload.get("a"));
        BigDecimal b = new BigDecimal((String) payload.get("b"));
        
        CalculatorEntity calculatorEntity = new CalculatorEntity(operation,a,b); 

        MDC.put("requestId",requestId);
        logger.info("Calc Received msg from Kafka: {}", message);
        try {
            BigDecimal res = calculatorEntity.doOp();
            
            kafkaTemplate.send("response-topic", requestId, String.valueOf(res));
            logger.info("Calc Sent result to Kafka: {} with id {}", res, requestId);
        } catch (Exception e) {
            logger.error("Calc Error processing message", e);
        } finally {
            MDC.clear();
        }
    }
    private Map<String, Object> parseMessage(String message) {
        // Simple JSON parsing logic (use a library like Jackson for production code)
        return Map.of(
                "requestId", message.split(",")[0].split(":")[1],
                "operation", message.split(",")[1].split(":")[1].replace("\"", ""),
                "a", message.split(",")[2].split(":")[1],
                "b", message.split(",")[3].split(":")[1].replace("}", "")
        );
    }

}
