package com.rog_rest.Restkafka;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.og_calculator.model.CalculatorEntity;

@Service
public class RestKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(RestKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void postcalc(String topic, CalculatorEntity calcEntity) {   
        try {
            logger.info("REST Sending data to kafka: topic= '{}', calcEntity = '{}'", topic, calcEntity.hashCode());
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send(topic,mapper.writeValueAsString(calcEntity));
            
        } catch (Exception e) {
            logger.error("REST An error occurred! '{}'", e.getMessage());
        }
    }    
}
