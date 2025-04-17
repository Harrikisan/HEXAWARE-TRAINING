package com.hexaware.payexpert.dao.interfaces;

import java.time.LocalDate;

import com.hexaware.payexpert.entity.FinancialRecord;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;

public interface FinancialRecorddao {
	
	public void AddFinancialRecord(FinancialRecord record) throws DatabaseConnectionException
	,FinancialRecordException;
	public void UpdateFinancialRecord(FinancialRecord record) throws DatabaseConnectionException
	,FinancialRecordException;
	public void GetFinancialRecordById(int recordId)throws DatabaseConnectionException
	, FinancialRecordException;
	public void GetFinancialRecordsForEmployee(int employeeId)throws DatabaseConnectionException
	,EmployeeNotFoundException;
	public void GetFinancialRecordsForDate(String recordDate)throws DatabaseConnectionException, FinancialRecordException;
	public void DeleteFinancialRecord(int recordId) throws FinancialRecordException;
	
}
