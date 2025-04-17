package com.hexaware.payexpert.dao.interfaces;

import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;
import com.hexaware.payexpert.exception.TaxCalculationException;

public interface Taxdao {

	public void CalculateTax(int employee_id,String TaxYear)throws DatabaseConnectionException,
	TaxCalculationException, EmployeeNotFoundException;
	public void GetTaxById(int taxId)throws DatabaseConnectionException, TaxCalculationException;
	public void GetTaxForEmployee(int employee_id)throws DatabaseConnectionException,
	EmployeeNotFoundException;
	public void GetTaxesForYear(String taxYear) throws DatabaseConnectionException;
	public void DeleteTaxRecord(int employee_id) throws EmployeeNotFoundException;
	public void GetAllTaxData() throws TaxCalculationException;;
}
