package com.hexaware.payexpert.dao.interfaces;

import java.sql.SQLException;

import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;
import com.hexaware.payexpert.exception.InputValidateionException;
import com.hexaware.payexpert.exception.PayrollGenerationException;
import com.hexaware.payexpert.exception.TaxCalculationException;

public interface Userdao {

	public void manageEmployee() throws DatabaseConnectionException,
	EmployeeNotFoundException, InputValidateionException;
	public void managePayroll() throws DatabaseConnectionException, 
	PayrollGenerationException, EmployeeNotFoundException;
	public void manageTax() throws DatabaseConnectionException, 
	EmployeeNotFoundException, TaxCalculationException;
	public void manageFinancialRecord() throws DatabaseConnectionException, 
	FinancialRecordException, EmployeeNotFoundException;
	public void ReportGeneration() throws EmployeeNotFoundException, SQLException;
}
