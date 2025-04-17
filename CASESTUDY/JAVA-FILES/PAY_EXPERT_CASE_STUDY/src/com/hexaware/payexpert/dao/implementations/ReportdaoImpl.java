package com.hexaware.payexpert.dao.implementations;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.hexaware.payexpert.dao.interfaces.Reportdao;
import com.hexaware.payexpert.entity.*;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.ReportGenerationException;
import com.hexaware.payexpert.util.DatabaseConnection;

public class ReportdaoImpl implements Reportdao{

	Connection conn;
	
	public ReportdaoImpl(Connection conn) {
		this.conn=conn;
	}
	@Override
	public void GenerateReportForEmployee(int employee_id, String year) throws EmployeeNotFoundException,
	SQLException {
		// TODO Auto-generated method 

		
		ArrayList<FinancialRecord> record=new ArrayList<>();
		ArrayList<Payroll> payroll=new ArrayList<>();
		String query="Select * from employee where employeeid=?";
		
		System.out.println("----------PAYEXPERT----------");
		System.out.println("______________________________");
		System.out.println("Date: "+LocalDate.now());
		
		
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, employee_id);
			ps.executeQuery();
			try(ResultSet rs=ps.executeQuery()){
				if(rs.next()) {
					Employees employee=(new Employees(
							rs.getInt("employeeid"),
							rs.getString("firstname"),
							rs.getString("lastname"),
							rs.getString("dateofbirth"),
							rs.getString("gender"),
							rs.getString("email"),
							rs.getString("phonenumber"),
							rs.getString("address"),
							rs.getString("position"),
							rs.getString("joiningdate"),
							rs.getString("terminationdate")));
					
					System.out.println("\nEmployee Details");
					System.out.println("________________");
					System.out.println(" Employee ID: "+ employee.getEmployeeId()+
							"\n FirstName: "+ employee.getfName()+
							"\n LastName: "+ employee.getlName()+
							"\n Date-Of-Birth: "+employee.getDOB()+
							"\n Gender: "+employee.getGender()+
							"\n Email: "+employee.getEmail()+
							"\n Phonenumber: "+employee.getPhone()+
							"\n Address: "+employee.getAddress()+
							"\n Position: "+employee.getPosition()+
							"\n Joining Date: "+employee.getJoiningDate()+
							"\n Termination Date: "+employee.getTerminationDate()+
							"\n Age: "+employee.getAge());
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new EmployeeNotFoundException("Error Retreving data :" + e.getMessage());
		}
		

		query="Select * from financialRecord where employeeid=? and year(recorddate)=?";
		
		try(PreparedStatement ps1=conn.prepareStatement(query)){
			ps1.setInt(1, employee_id);
			ps1.setString(2, year);
			ps1.executeQuery();
			try(ResultSet rs=ps1.executeQuery()){
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
				System.out.println("\nFinancial Report");
				System.out.println("________________");
				for(FinancialRecord r:record) {
					System.out.println("Record ID: "+ r.getRecordId()+
							"|Employee ID: "+ r.getEmployeeId()+
							"|Record date: "+r.getDate()+"|Description:"+r.getDescription()+
							"|Record type: "+r.getRecordType()+"|Amount: "+r.getAmount());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new EmployeeNotFoundException("Error Retreving data :" + e.getMessage());
		}
		
		query="Select * from tax where employeeid=? and taxyear=?";
		
		try(PreparedStatement ps2=conn.prepareStatement(query)){
			ps2.setInt(1, employee_id);
			ps2.setString(2, year);
			try(ResultSet rs=ps2.executeQuery()){
				if(rs.next()) {
					Tax tax = new Tax(
                            rs.getInt("taxid"),
                            rs.getBigDecimal("taxableincome"),
                            rs.getBigDecimal("taxamount"),
                            rs.getString("taxyear"),
                            new Employees(rs.getInt("employeeid"))
                    );
		    		System.out.println("\nTax for year");
		    		System.out.println("____________");
		    		System.out.println("Tax id: "+ tax.getTaxId()+
				    		"|EmployeeId: "+tax.getEmployeeId().getEmployeeId()+
				    		"|Taxyear: "+tax.getTaxYear()+
				    		"|Taxable income: "+tax.getTaxableIncome()+
				    		"|TaxAmount: "+tax.getTaxAmount());
	    		}

				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new EmployeeNotFoundException("Error Retreving data :" + e.getMessage());
		}

		query="select * from payroll where employeeid=?";
		try(PreparedStatement ps3=conn.prepareStatement(query)){
			ps3.setInt(1, employee_id);
			try(ResultSet rs=ps3.executeQuery()){
				while (rs.next()) {
                    payroll.add(new Payroll(
                            rs.getInt("payrollid"),
                            rs.getBigDecimal("basicsalary"),
                            rs.getBigDecimal("overtimepay"),
                            rs.getBigDecimal("deductions"),
                            rs.getBigDecimal("netsalary"),
                            new Employees(rs.getInt("employeeid")),
                            rs.getString("payperiodstartdate"),
                            rs.getString("payperiodenddate")
                    ));
	                System.out.println("\nPayroll");
	                System.out.println("________");
	                for(Payroll p:payroll) {
		                
		                System.out.println("Payroll ID: " + p.getPayrollId() +
		                        " | Employee ID: " + p.getEmployeeId().getEmployeeId() +
		                        " | PayPeriod Start Date: " + p.getPayperiodStartTime() +
		                        " | PayPeriod End Date: " + p.getPayperiodEndTime() +
		                        " | Basic Salary: " + p.getBasicSalary() +
		                        " | Overtime Pay: " + p.getOverTimePay() +
		                        " | Deductions: " + p.getDeductions() +
		                        " | Net Salary: " + p.getNetSalary());
		            }
	            }
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new EmployeeNotFoundException("Error Retreving data :" + e.getMessage());
		}
		
		
		
		
		}
	}

	

}
