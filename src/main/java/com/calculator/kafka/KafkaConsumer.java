package com.calculator.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "operation")
    public void sum(String msgjson){
        logger.info("received content = '{}'", msgjson);
        try{
            logger.info("Success process ");
        } catch (Exception e){
            logger.error("An error occurred! '{}'", e.getMessage());
        }
    }

}