package com.hexaware.payexpert.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.hexaware.payexpert.dao.interfaces.FinancialRecorddao;
import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.entity.FinancialRecord;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;
import com.hexaware.payexpert.util.DatabaseConnection;

public class FinancialRecorddaoImpl implements FinancialRecorddao {

	Connection conn;
	
	public FinancialRecorddaoImpl(Connection conn) {
		
		this.conn = conn;
	}


	@Override
	public void AddFinancialRecord(FinancialRecord record)
			throws DatabaseConnectionException, FinancialRecordException {
		// TODO Auto-generated method stub
		if(isValid(record)) {
			String query="insert into financialrecord(employeeid,recorddate,description,amount,recordtype)"
					+ " values(?,?,?,?,?)";
			
			try(PreparedStatement ps=conn.prepareStatement(query);) {
				ps.setInt(1, record.getEmployeeId().getEmployeeId());
				ps.setString(2, record.getDate());
				ps.setString(3, record.getDescription());
				ps.setBigDecimal(4, record.getAmount());
				ps.setString(5, record.getRecordType());
				ps.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new DatabaseConnectionException(e.getMessage());
			}
		}
		
	}

	
	@Override
	public void GetFinancialRecordById(int recordId) throws FinancialRecordException {
		// TODO Auto-generated method stub
		if (recordId <= 0) {
		    throw new FinancialRecordException("Record ID must be a positive integer.");
		}
		String query="Select * from financialrecord where recordid=?";
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, recordId);
			try(ResultSet rs=ps.executeQuery()){
				if(rs.next()) {
					FinancialRecord record=new FinancialRecord(
							new Employees(rs.getInt("employeeid")),
							rs.getString("recorddate"),
							rs.getString("description"),
							rs.getString("recordtype"),
							rs.getBigDecimal("amount"));
					
					System.out.println("Record ID: "+ recordId+
							"|Employee ID: "+record.getEmployeeId().getEmployeeId()+
							"|Record date: "+record.getDate()+"|Description:"+record.getDescription()+
							"|Record type: "+record.getRecordType()+"|Amount: "+record.getAmount());
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new FinancialRecordException("Record not found : "+ e.getMessage());
		}
		
	}

	@Override
	public void GetFinancialRecordsForEmployee(int employeeId)
			throws DatabaseConnectionException, EmployeeNotFoundException {
		// TODO Auto-generated method stub
		if (employeeId <= 0) {
		    throw new EmployeeNotFoundException("Employee ID must be a positive integer.");
		}
		String query="Select * from financialrecord where employeeid=?";
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, employeeId);
			try(ResultSet rs=ps.executeQuery()){
				while(rs.next()) {
					FinancialRecord record=new FinancialRecord(
							rs.getInt("recordid"),
							new Employees(rs.getInt("employeeid")),
							rs.getString("recorddate"),
							rs.getString("description"),
							rs.getString("recordtype"),
							rs.getBigDecimal("amount"));
					
					System.out.println("Record ID: "+ record.getRecordId()+
							"|Employee ID: "+ employeeId+
							"|Record date: "+record.getDate()+"|Description:"+record.getDescription()+
							"|Record type: "+record.getRecordType()+"|Amount: "+record.getAmount());
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new EmployeeNotFoundException("Record not found : "+ e.getMessage());
		}
	}

	@Override
	public void GetFinancialRecordsForDate(String recordDate) throws DatabaseConnectionException, 
	FinancialRecordException {
		// TODO Auto-generated method stub
		if (recordDate == null || recordDate.isEmpty()) {
		    throw new FinancialRecordException("Record date cannot be null or empty.");
		}
		try {
		    LocalDate.parse(recordDate);
		} catch (Exception e) {
		    throw new FinancialRecordException("Invalid date format. Expected format: yyyy-MM-dd");
		}

		String query="Select * from financialrecord where recordDate=?";
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setString(1, recordDate);
			try(ResultSet rs=ps.executeQuery()){
				if(rs.next()) {
					FinancialRecord record=new FinancialRecord(
							rs.getInt("recordid"),
							new Employees(rs.getInt("employeeid")),
							rs.getString("recorddate"),
							rs.getString("description"),
							rs.getString("recordtype"),
							rs.getBigDecimal("amount"));
					
					System.out.println("Record ID: "+ record.getRecordId()+
							"|Employee ID: "+ record.getEmployeeId().getEmployeeId()+
							"|Record date: "+recordDate+"|Description:"+record.getDescription()+
							"|Record type: "+record.getRecordType()+"|Amount: "+record.getAmount());
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new FinancialRecordException("Record not found : "+ e.getMessage());
		}
	}

	@Override
	public void UpdateFinancialRecord(FinancialRecord record)
			throws DatabaseConnectionException, FinancialRecordException {
		// TODO Auto-generated method stub
		if(isValid(record)) {
			String query="Update financialrecord set "
					+ "employeeid=?,"
					+ "recorddate=?,"
					+ "description=?,"
					+ "amount=?,"
					+ "recordtype=? where recordid=?";
			
			try(PreparedStatement ps=conn.prepareStatement(query);) {
				ps.setInt(1, record.getEmployeeId().getEmployeeId());
				ps.setString(2, record.getDate());
				ps.setString(3, record.getDescription());
				ps.setBigDecimal(4, record.getAmount());
				ps.setString(5, record.getRecordType());
				ps.setInt(6, record.getRecordId());
				ps.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new DatabaseConnectionException(e.getMessage());
			}
		}
		
	}

	@Override
	public void DeleteFinancialRecord(int recordId) throws FinancialRecordException {
		// TODO Auto-generated method stub
		if (recordId <= 0) {
		    throw new FinancialRecordException("Record ID must be a positive integer.");
		}
		String query="Delete from financialrecord where recordId=?";
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, recordId);
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new FinancialRecordException("Id not fount: "+e.getMessage());
		}
		
	}
	
	private boolean isValid(FinancialRecord record) throws FinancialRecordException {
	    if (record == null) {
	        throw new FinancialRecordException("Financial record cannot be null.");
	    }

	    if (record.getEmployeeId() == null || record.getEmployeeId().getEmployeeId() <= 0) {
	        throw new FinancialRecordException("Invalid or missing employee ID.");
	    }

	    if (record.getDate() == null || record.getDate().isEmpty()) {
	        throw new FinancialRecordException("Record date cannot be empty.");
	    }

	    try {
	        LocalDate.parse(record.getDate());
	    } catch (Exception e) {
	        throw new FinancialRecordException("Invalid date format. Expected format: yyyy-MM-dd");
	    }

	    if (record.getDescription() == null || record.getDescription().trim().isEmpty()) {
	        throw new FinancialRecordException("Description cannot be empty.");
	    }

	    if (record.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
	        throw new FinancialRecordException("Amount must be greater than 0.");
	    }

	    if (record.getRecordType() == null || 
	        !(record.getRecordType().equalsIgnoreCase("Income") ||
	        		record.getRecordType().equalsIgnoreCase("Deduction"))) {
	        throw new FinancialRecordException("Record type must be either 'Income' or 'Deduction'.");
	    }

	    return true;
	}



}
