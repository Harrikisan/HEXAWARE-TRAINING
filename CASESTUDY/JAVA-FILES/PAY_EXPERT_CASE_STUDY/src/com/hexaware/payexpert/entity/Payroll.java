package com.hexaware.payexpert.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payroll {

	private int payrollId;
	private BigDecimal basicSalary, overTimePay, deductions, netSalary,grossSalary;
	private Employees employeeId;
	private String payperiodStartTime, payperiodEndTime;
	
	
	//Constructor creation
	public Payroll(int payrollId, BigDecimal basicSalary, BigDecimal overTimePay, BigDecimal deductions, BigDecimal netSalary, Employees employeeId,
			String payperiodStartTime, String payperiodEndTime) {
		super();
		this.payrollId = payrollId;
		this.basicSalary = basicSalary;
		this.overTimePay = overTimePay;
		this.deductions = deductions;
		this.netSalary = netSalary;
		this.employeeId = employeeId;
		this.payperiodStartTime = payperiodStartTime;
		this.payperiodEndTime = payperiodEndTime;
		this.grossSalary = this.basicSalary.add(this.overTimePay);
	}

	//Getters and Setters
	public int getPayrollId() {
		return payrollId;
	}
	public BigDecimal getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(BigDecimal grossSalary) {
		this.grossSalary = grossSalary;
	}

	public void setPayrollId(int payrollId) {
		this.payrollId = payrollId;
	}
	
	
	public BigDecimal getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}


	public BigDecimal getOverTimePay() {
		return overTimePay;
	}
	public void setOverTimePay(BigDecimal overTimePay) {
		this.overTimePay = overTimePay;
	}


	public BigDecimal getDeductions() {
		return deductions;
	}
	public void setDeductions(BigDecimal deductions) {
		this.deductions = deductions;
	}


	public BigDecimal getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(BigDecimal netSalary) {
		this.netSalary = netSalary;
	}


	public Employees getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Employees employeeId) {
		this.employeeId = employeeId;
	}


	public String getPayperiodStartTime() {
		return payperiodStartTime;
	}
	public void setPayperiodStartTime(String payperiodStartTime) {
		this.payperiodStartTime = payperiodStartTime;
	}


	public String getPayperiodEndTime() {
		return payperiodEndTime;
	}
	public void setPayperiodEndTime(String payperiodEndTime) {
		this.payperiodEndTime = payperiodEndTime;
	}
	
	
}
