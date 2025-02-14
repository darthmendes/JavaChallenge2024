package com.rog_rest.Restkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RestKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RestKafkaConsumer.class);

    @KafkaListener(topics = "results")
    public void sum(String msgjson){
        logger.info("results received content = '{}'", msgjson);
        try{
            logger.info("Success process ");
        } catch (Exception e){
            logger.error("An error occurred! '{}'", e.getMessage());
        }
    }

}