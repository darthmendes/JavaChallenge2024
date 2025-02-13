package com.calculator.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.kafka.producer.MessageProducer;

@RestController
@RequestMapping(value = "/api/calculator")
public class CalculatorController {

    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        messageProducer.sendMessage("my-topic", message);
        return "Message sent: " + message;
    }

    @PostMapping("/{operation}")
    public BigDecimal calc(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return switch (operation.toUpperCase()) {
            case "SUM" -> a.add(b);
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