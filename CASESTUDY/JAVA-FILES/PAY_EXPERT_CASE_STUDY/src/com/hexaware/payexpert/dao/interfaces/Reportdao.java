package com.hexaware.payexpert.dao.interfaces;

import java.sql.SQLException;

import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.ReportGenerationException;

public interface Reportdao {

	public void GenerateReportForEmployee(int employee_id,String year) throws EmployeeNotFoundException,
	SQLException;
	
}
