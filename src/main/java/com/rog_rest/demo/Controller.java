package com.rog_rest.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.og_calculator.model.CalculatorEntity;
import com.rog_rest.Restkafka.RestKafkaConsumer;
import com.rog_rest.Restkafka.RestKafkaProducer;

@RestController
@RequestMapping(value = "/calculator")
public class Controller {

    @Autowired
    RestKafkaProducer kafkaProducer;

    @Autowired
    RestKafkaConsumer kafkaConsumer;

    @GetMapping("/{operation}")
    public BigDecimal calc(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        CalculatorEntity calculatorEntity = new CalculatorEntity();
        calculatorEntity.setA(a);
        calculatorEntity.setB(b);
        calculatorEntity.setOperation(operation.toLowerCase());

        return switch (operation.toLowerCase()) {
            case "sum" ->  {
                kafkaProducer.postcalc("operation",  calculatorEntity);
                yield a.add(b);
            }
            case "subtraction" -> a.subtract(b);
            case "multiplication" -> a.multiply(b);
            case "division" -> {
                if (BigDecimal.ZERO.compareTo(b) == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                yield a.divide(b, RoundingMode.HALF_UP);
            }
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

}