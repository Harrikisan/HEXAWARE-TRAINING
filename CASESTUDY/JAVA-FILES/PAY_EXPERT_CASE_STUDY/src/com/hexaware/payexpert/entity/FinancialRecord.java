package com.hexaware.payexpert.entity;

import java.math.BigDecimal;

public class FinancialRecord {

    private int recordId;
    private Employees employeeId;
    private String description, recordType;
    private String date; 
    private BigDecimal amount;  
    
    // Constructor creation
    public FinancialRecord(Employees employeeId, String date, String description, String recordType,
                           BigDecimal amount) {
        super();
        this.employeeId = employeeId;
        this.date = date;
        this.description = description;
        this.recordType = recordType;
        this.amount = amount;
    }

    public FinancialRecord(int recordId, Employees employeeId, String date, String description, String recordType,
                           BigDecimal amount) {
        super();
        this.recordId = recordId;
        this.employeeId = employeeId;
        this.date = date;
        this.description = description;
        this.recordType = recordType;
        this.amount = amount;
    }

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Employees getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employees employeeId) {
        this.employeeId = employeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
