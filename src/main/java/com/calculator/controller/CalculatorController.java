package com.calculator.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.calculator.kafka.KafkaConsumer;
import com.calculator.kafka.KafkaProducer;

@RestController
@RequestMapping(value = "/api/calculator")
public class CalculatorController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaConsumer kafkaConsumer;

    @PostMapping("/{operation}")
    public BigDecimal calc(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return switch (operation.toUpperCase()) {
            case "SUM" ->  kafkaProducer.postcalc("sum", a, b);
            case "SUBTRACTION" -> a.subtract(b);
            case "MULTIPLICATION" -> a.multiply(b);
            case "DIVISION" -> {
                if (BigDecimal.ZERO.compareTo(b) == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                yield a.divide(b, RoundingMode.HALF_UP);
            }
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

}