package com.og_calculator.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CalculatorEntity {
    private String operation;
    private BigDecimal a;
    private BigDecimal b; 

    public CalculatorEntity(){}
    public CalculatorEntity(String op, BigDecimal a, BigDecimal b) {
        this.operation = op;
        this.a = a;
        this.b = b;
    }

    public void setA(BigDecimal a){
        this.a = a; 
    }
    public void setB(BigDecimal b){
        this.b = b;
    }
    public void setOperation(String operation){
        this.operation = operation;
    }
    
    public String getOperation() {
        return this.operation;
    }
    public BigDecimal getA() {
        return this.a;
    }
    public BigDecimal getB() {
        return this.b;
    }
        
    public BigDecimal doOp(){
        switch (this.operation) {
            case "sum":
                return this.a.add(this.b);
            case "subtraction":
                return this.a.subtract(this.b);
            case "multiplication":
                return this.a.multiply(this.b);
            case "division":
                if (this.b.equals(BigDecimal.ZERO)) {
                    return BigDecimal.ZERO;
                }
                return this.a.divide(this.b);
            default:
                return BigDecimal.ZERO;
            }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculatorEntity that = (CalculatorEntity) o;
        return operation == that.operation &&
                Objects.equals(a, that.a) &&
                Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, a, b);
    }
}
