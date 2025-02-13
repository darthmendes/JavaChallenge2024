package com.rest.demo;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/calculator")
public class controller {

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