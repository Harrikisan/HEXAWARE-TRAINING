package com.hexaware.payexpert.entity;

import java.math.BigDecimal;

public class Tax {
	
	private int taxId;
	private BigDecimal taxableIncome, taxAmount;
	private String taxYear;
	private Employees employeeId;
	
	// Constructor creation
	public Tax() {}
	
	public Tax(int taxId, BigDecimal taxableIncome, BigDecimal taxAmount, String taxYear, Employees employeeId) {
		super();
		this.taxId = taxId;
		this.taxableIncome = taxableIncome;
		this.taxAmount = taxAmount;
		this.taxYear = taxYear;
		this.employeeId = employeeId;
	}

	// Getters and Setters
	public int getTaxId() {
		return taxId;
	}
	public void setTaxId(int taxId) {
		this.taxId = taxId;
	}

	public BigDecimal getTaxableIncome() {
		return taxableIncome;
	}
	public void setTaxableIncome(BigDecimal taxableIncome) {
		this.taxableIncome = taxableIncome;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTaxYear() {
		return taxYear;
	}
	public void setTaxYear(String taxYear) {
		this.taxYear = taxYear;
	}

	public Employees getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Employees employeeId) {
		this.employeeId = employeeId;
	}
}
