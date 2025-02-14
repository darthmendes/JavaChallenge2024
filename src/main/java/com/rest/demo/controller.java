package com.rest.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.calculator.model.CalculatorEntity;
import com.rest.Restkafka.RestKafkaConsumer;
import com.rest.Restkafka.RestKafkaProducer;

@RestController
@RequestMapping(value = "/calculator")
public class Controller {

    @Autowired
    RestKafkaProducer kafkaProducer;

    @Autowired
    RestKafkaConsumer kafkaConsumer;

    @PostMapping("/{operation}")
    public BigDecimal calc(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        CalculatorEntity calculatorEntity = new CalculatorEntity();
        calculatorEntity.setA(a);
        calculatorEntity.setB(b);
        calculatorEntity.setOperation(operation);

        return switch (operation.toUpperCase()) {
            case "SUM" ->  {
                kafkaProducer.postcalc("operation",  calculatorEntity);
                yield a.add(b);
            }
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