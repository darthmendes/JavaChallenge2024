package com.calculator.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.calculator.model.CalculatorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "operation")
    public void operations(String msgjson){
        logger.info("CALC received content = '{}'", msgjson);
        try{
            logger.info("CALC Success process ");
            ObjectMapper mapper = new ObjectMapper();
            CalculatorEntity calculatorEntity = mapper.readValue(msgjson, CalculatorEntity.class);
            
            BigDecimal res = calculatorEntity.doOp();
            
            Map<String,Object> map = new LinkedHashMap<String,Object>();
            map.put("result",res);
            ObjectMapper mapper2 = new ObjectMapper();
            String json = mapper2.writeValueAsString(map);
            kafkaTemplate.send("results", json);
            // send result to kafka topic=results

        } catch (Exception e){
            logger.error("CALC An error occurred! '{}'", e.getMessage());
        }
    }

}