package com.hexaware.payexpert.dao.interfaces;

import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.exception.*;

public interface Employeedao {

	public void GetEmployeeById(int id)throws DatabaseConnectionException,EmployeeNotFoundException,
	InputValidateionException;
	public void GetAllEmployees()throws DatabaseConnectionException,EmployeeNotFoundException;
	public void AddEmployee(Employees employee)throws DatabaseConnectionException,InputValidateionException;
	public void UpdateEmployee(Employees employee)throws DatabaseConnectionException,
	InputValidateionException,EmployeeNotFoundException;
	public void RemoveEmployee(int id) throws DatabaseConnectionException,EmployeeNotFoundException,
	InputValidateionException;
	
}
