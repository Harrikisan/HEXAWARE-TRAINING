package com.hexaware.payexpert.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import com.hexaware.payexpert.dao.interfaces.Taxdao;
import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.entity.FinancialRecord;
import com.hexaware.payexpert.entity.Tax;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;
import com.hexaware.payexpert.exception.TaxCalculationException;
import com.hexaware.payexpert.util.DatabaseConnection;

public class TaxdaoImpl implements Taxdao{

	Connection conn;
	public TaxdaoImpl(Connection conn) {
		this.conn = conn;
	}



	@Override
	public void CalculateTax(int employee_id, String TaxYear)
	        throws DatabaseConnectionException, TaxCalculationException {
		
		if (employee_id <= 0) {
	        throw new TaxCalculationException("Invalid employee ID. Must be a positive integer.");
	    }
	    if (TaxYear == null || !TaxYear.matches("\\d{4}")) {
	        throw new TaxCalculationException("Invalid tax year. Must be a 4-digit year.");
	    }
		
		String query="SELECT COUNT(*) FROM tax WHERE employeeid = ? AND taxyear = ?";
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, employee_id);
		    ps.setString(2, TaxYear);
		    try (ResultSet rs = ps.executeQuery()) {
		        if (rs.next() && rs.getInt(1) > 0) {
		            throw new TaxCalculationException("Tax record already exists for employee " + employee_id + " in year " + TaxYear);
		        }
		    }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw new DatabaseConnectionException(e1.getMessage());
		}
	    ArrayList<FinancialRecord> record = new ArrayList<>();
	    query = "SELECT * FROM financialrecord WHERE employeeid = ? AND YEAR(recorddate) = ?";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, employee_id);
	        ps.setString(2, TaxYear);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                record.add(new FinancialRecord(
	                        rs.getInt("recordid"),
	                        new Employees(rs.getInt("employeeid")),
	                        rs.getString("recorddate"),
	                        rs.getString("description"),
	                        rs.getString("recordtype"),
	                        rs.getBigDecimal("amount")
	                ));
	            }

	            BigDecimal taxableIncome = calculateTaxableIncome(record);
	            BigDecimal taxamount = calculateTax(taxableIncome);

	            String insertQuery = "INSERT INTO tax(employeeid, taxyear, taxableincome, taxamount) VALUES (?, ?, ?, ?)";

	            try (PreparedStatement ps1 = conn.prepareStatement(insertQuery)) {
	                ps1.setInt(1, employee_id);
	                ps1.setString(2, TaxYear);
	                ps1.setBigDecimal(3, taxableIncome);
	                ps1.setBigDecimal(4, taxamount);

	                ps1.executeUpdate();
	            } catch (Exception e) {
	                throw new TaxCalculationException("Error inserting tax record: " + e.getMessage());
	            }

	        }
	    } catch (SQLException e) {
	        throw new TaxCalculationException("Error retrieving financial records: " + e.getMessage());
	    }
	}



	@Override
	public void GetTaxById(int taxId) throws DatabaseConnectionException, TaxCalculationException {
		// TODO Auto-generated method stub
		if (taxId <= 0) {
	        throw new TaxCalculationException("Invalid tax ID. Must be a positive integer.");
	    }
		String query="Select * from tax where taxid=?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
		    ps.setInt(1, taxId);
		    try(ResultSet rs=ps.executeQuery()){
		    	try {
		    		if(rs.next()) {
		    			Tax tax = new Tax(
                                rs.getInt("taxid"),
                                rs.getBigDecimal("taxableincome"),
                                rs.getBigDecimal("taxamount"),
                                rs.getString("taxyear"),
                                new Employees(rs.getInt("employeeid"))
                        );
			    		
			    		System.out.println("Tax id: "+ tax.getTaxId()+
			    		"|EmployeeId: "+tax.getEmployeeId().getEmployeeId()+
			    		"|Taxyear: "+tax.getTaxYear()+
			    		"|Taxable income: "+tax.getTaxableIncome()+
			    		"|TaxAmount: "+tax.getTaxAmount());
			    	}
		    	}catch (Exception e) {
				    throw new TaxCalculationException("ID does not exist: "+e.getMessage());
				}
		    	
		    }
		} catch (Exception e) {
		    throw new TaxCalculationException(e.getMessage());
		}
		
		
	}

	@Override
	public void GetTaxForEmployee(int employee_id) throws DatabaseConnectionException, EmployeeNotFoundException {
		// TODO Auto-generated method stub
		if (employee_id <= 0) {
	        throw new EmployeeNotFoundException("Invalid employee ID. Must be a positive integer.");
	    }
		ArrayList<Tax> tax=new ArrayList<>();
		String query="Select * from tax where employeeid=?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
		    ps.setInt(1, employee_id);
		    try(ResultSet rs=ps.executeQuery()){
		    	try {
		    		while (rs.next()) {
                        tax.add(new Tax(
                                rs.getInt("taxid"),
                                rs.getBigDecimal("taxableincome"),
                                rs.getBigDecimal("taxamount"),
                                rs.getString("taxyear"),
                                new Employees(rs.getInt("employeeid"))
                        ));
		    		}	
			    	for(Tax t:tax) {
			    		System.out.println("Tax id: "+ t.getTaxId()+
			    		"|EmployeeId: "+employee_id+
			    		"|Taxyear: "+t.getTaxYear()+
			    		"|Taxable income: "+t.getTaxableIncome()+
			    		"|TaxAmount: "+t.getTaxAmount());
			    	}
		    	}catch (Exception e) {
				    throw new TaxCalculationException("ID does not exist: "+e.getMessage());
				}
		    	
		    }
		} catch (Exception e) {
		    throw new DatabaseConnectionException(e.getMessage());
		}
	}

	@Override
	public void GetTaxesForYear(String taxYear) throws DatabaseConnectionException {
		// TODO Auto-generated method stub
		if (taxYear == null || !taxYear.matches("\\d{4}")) {
	        throw new DatabaseConnectionException("Invalid tax year. Must be a 4-digit year.");
	    }
		String query="Select * from tax where taxyear=?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
		    ps.setString(1, taxYear);
		    try(ResultSet rs=ps.executeQuery()){
		    	try {
		    		if(rs.next()) {
		    			Tax tax = new Tax(
                                rs.getInt("taxid"),
                                rs.getBigDecimal("taxableincome"),
                                rs.getBigDecimal("taxamount"),
                                rs.getString("taxyear"),
                                new Employees(rs.getInt("employeeid"))
                        );
			    		
			    		System.out.println("Tax id: "+ tax.getTaxId()+
			    		"|EmployeeId: "+tax.getEmployeeId().getEmployeeId()+
			    		"|Taxyear: "+taxYear+
			    		"|Taxable income: "+tax.getTaxableIncome()+
			    		"|TaxAmount: "+tax.getTaxAmount());
			    	}
		    	}catch (Exception e) {
				    throw new TaxCalculationException("ID does not exist: "+e.getMessage());
				}
		    	
		    }
		} catch (Exception e) {
		    throw new DatabaseConnectionException(e.getMessage());
		}
	}
	
	@Override
	public void DeleteTaxRecord(int employee_id) throws EmployeeNotFoundException {
		// TODO Auto-generated method stub
		if (employee_id <= 0) {
	        throw new EmployeeNotFoundException("Invalid employee ID. Must be a positive integer.");
	    }
		String query="delete from tax where employeeid=?";
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, employee_id);
			ps.executeUpdate();
		} catch (SQLException e) {
	        throw new EmployeeNotFoundException("Error deleting tax record: " + e.getMessage());
	    }
	}
	
	@Override
	public void GetAllTaxData() throws TaxCalculationException {
		// TODO Auto-generated method stub
		ArrayList<Tax> t=new ArrayList<>();
		String query="Select * from tax";
		
		try(Statement s=conn.createStatement();ResultSet rs=s.executeQuery(query)){
			while(rs.next()) {
				t.add(new Tax(
                        rs.getInt("taxid"),
                        rs.getBigDecimal("taxableincome"),
                        rs.getBigDecimal("taxamount"),
                        rs.getString("taxyear"),
                        new Employees(rs.getInt("employeeid"))
                ));
			}
			
			for(Tax tax:t) {
				System.out.println("Tax id: "+ tax.getTaxId()+
			    		"|EmployeeId: "+tax.getEmployeeId().getEmployeeId()+
			    		"|Taxyear: "+tax.getTaxYear()+
			    		"|Taxable income: "+tax.getTaxableIncome()+
			    		"|TaxAmount: "+tax.getTaxAmount());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new TaxCalculationException("Error detecting data: "+e.getMessage());
		}
	}
	
	private BigDecimal calculateTaxableIncome(ArrayList<FinancialRecord> record) {
	    BigDecimal income = BigDecimal.ZERO, deduction = BigDecimal.ZERO;
	    for (FinancialRecord r : record) {
	        if (r.getRecordType().equalsIgnoreCase("Income")) {
	            income = income.add(r.getAmount());
	        } else if (r.getRecordType().equalsIgnoreCase("Deduction")) {
	            deduction = deduction.add(r.getAmount());
	        }
	    }
	    System.out.println("Income: "+income+"Deduction: "+deduction);
	    return income.subtract(deduction);
	}

	
	private BigDecimal calculateTax(BigDecimal taxableIncome) {
	    if (taxableIncome.compareTo(BigDecimal.valueOf(250000)) <= 0) {
	        return BigDecimal.ZERO;
	    } else if (taxableIncome.compareTo(BigDecimal.valueOf(500000)) <= 0) {
	        return taxableIncome.subtract(BigDecimal.valueOf(250000)).multiply(BigDecimal.valueOf(0.05));
	    } else if (taxableIncome.compareTo(BigDecimal.valueOf(1000000)) <= 0) {
	        BigDecimal taxOnFirst250000 = BigDecimal.valueOf(250000).multiply(BigDecimal.valueOf(0.05));
	        BigDecimal taxOnRemaining = taxableIncome.subtract(BigDecimal.valueOf(500000)).multiply(BigDecimal.valueOf(0.2));
	        return taxOnFirst250000.add(taxOnRemaining);
	    } else {
	        BigDecimal taxOnFirst250000 = BigDecimal.valueOf(250000).multiply(BigDecimal.valueOf(0.05));
	        BigDecimal taxOnNext500000 = BigDecimal.valueOf(500000).multiply(BigDecimal.valueOf(0.2));
	        BigDecimal taxOnRemaining = taxableIncome.subtract(BigDecimal.valueOf(1000000)).multiply(BigDecimal.valueOf(0.3));
	        return taxOnFirst250000.add(taxOnNext500000).add(taxOnRemaining);
	    }
	}




	


	



	

	
	

	

	
}
