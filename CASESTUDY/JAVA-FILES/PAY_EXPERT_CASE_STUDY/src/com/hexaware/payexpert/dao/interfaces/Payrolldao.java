package com.hexaware.payexpert.dao.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.PayrollGenerationException;

public interface Payrolldao {

	public void GeneratePayroll(int employee_id,String startDate,String endDate ) 
			throws DatabaseConnectionException,PayrollGenerationException;
	public void GetPayrollById(int id) throws DatabaseConnectionException, PayrollGenerationException;
	public void GetPayrollsForEmployee(int employee_id) throws DatabaseConnectionException
	,EmployeeNotFoundException, PayrollGenerationException;
	public void GetPayrollsForPeriod(String startDate,String endDate) throws 
	DatabaseConnectionException,PayrollGenerationException;
	public void GetAllPayrolls() throws PayrollGenerationException;
	public void DeletePayroll(int employee_id,String startDate,String endDate ) throws PayrollGenerationException;
	public void DeletePayrollforEmployee(int employee_id) throws PayrollGenerationException, DatabaseConnectionException;
	public BigDecimal calculateGrossSalary(BigDecimal basicSalary, BigDecimal overtimePay);
	public BigDecimal calculateNetSalary(BigDecimal grossSalary, BigDecimal tax, BigDecimal deductions);
}
