package com.calculator.kafka;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public BigDecimal postcalc(String op, BigDecimal a, BigDecimal b){
        BigDecimal as = new BigDecimal(2);
            
        try {
            logger.info("Sending data to kafka: a = '{}', b = '{}' , with topic '{}'", a, b, op);
            kafkaTemplate.send(op, a.toString(), b.toString());
            
        } catch (Exception e) {
            logger.error("An error occurred! '{}'", e.getMessage());
        }
        return as;
    }    
}
