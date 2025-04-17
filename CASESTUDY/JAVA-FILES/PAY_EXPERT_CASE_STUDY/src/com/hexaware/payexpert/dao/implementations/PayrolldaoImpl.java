package com.hexaware.payexpert.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.hexaware.payexpert.dao.interfaces.Payrolldao;
import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.entity.FinancialRecord;
import com.hexaware.payexpert.entity.Payroll;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.PayrollGenerationException;
import com.hexaware.payexpert.util.DatabaseConnection;

public class PayrolldaoImpl implements Payrolldao {

	Connection conn;
	
	
	public PayrolldaoImpl(Connection conn) {
		
		this.conn = conn;
	}



	@Override
	public void GeneratePayroll(int employee_id, String startDate, String endDate)
	        throws DatabaseConnectionException, PayrollGenerationException {
	    
		if (employee_id <= 0) {
            throw new IllegalArgumentException("Invalid employee ID. It must be greater than 0.");
        }

        if (startDate == null || startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Start date cannot be null or empty.");
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("End date cannot be null or empty.");
        }
        
	    String query = "SELECT count(*) FROM payroll WHERE employeeid = ? AND payperiodstartdate = ? AND payperiodenddate = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, employee_id);
	        ps.setString(2, startDate);
	        ps.setString(3, endDate);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                throw new PayrollGenerationException("Payroll already exists for employeeId: " + employee_id +
	                        " | Start date: " + startDate + " | End date: " + endDate);
	            }
	        }
	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Database error: " + e.getMessage());
	    }

	    ArrayList<FinancialRecord> records = new ArrayList<>();
	    query = "SELECT * FROM financialrecord WHERE employeeid = ? AND recorddate BETWEEN ? AND ?";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, employee_id);
	        ps.setString(2, startDate);
	        ps.setString(3, endDate);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                records.add(new FinancialRecord(
	                        rs.getInt("recordid"),
	                        new Employees(rs.getInt("employeeid")),
	                        rs.getString("recorddate"),
	                        rs.getString("description"),
	                        rs.getString("recordtype"),
	                        rs.getBigDecimal("amount")
	                ));
	            }
	        }

	        BigDecimal basicSalary = calculateBasicSalary(records);
	        BigDecimal overtimePay = calculateOvertimepay(records);
	        BigDecimal deductions = calculateDeductions(records);
	        BigDecimal grossSalary=calculateGrossSalary(basicSalary,overtimePay);
	        BigDecimal netSalary = calculateNetSalary(basicSalary, overtimePay, deductions);
	        

	        String insertQuery = "INSERT INTO payroll (employeeid, payperiodstartdate, payperiodenddate, basicsalary, overtimepay, deductions, netsalary) "
	                           + "VALUES (?, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement ps1 = conn.prepareStatement(insertQuery)) {
	            ps1.setInt(1, employee_id);
	            ps1.setString(2, startDate);
	            ps1.setString(3, endDate);
	            ps1.setBigDecimal(4, basicSalary);
	            ps1.setBigDecimal(5, overtimePay);
	            ps1.setBigDecimal(6, deductions);
	            ps1.setBigDecimal(7, netSalary);
	            ps1.executeUpdate();
	        }

	    } catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving or inserting payroll data: " + e.getMessage());
	    }
	}
	

	public BigDecimal calculateGrossSalary(BigDecimal basicSalary, BigDecimal overtimePay) {
		// TODO Auto-generated method stub
		return basicSalary.add(overtimePay);
	}



	public BigDecimal calculateNetSalary(BigDecimal basicSalary, BigDecimal overtimePay, BigDecimal deductions) {
	    return basicSalary.add(overtimePay).subtract(deductions);
	}


	

	private BigDecimal calculateBasicSalary(ArrayList<FinancialRecord> records) {
	    BigDecimal basicSalary = BigDecimal.ZERO;
	    for(FinancialRecord r : records) {
	        if(r.getDescription().equalsIgnoreCase("basicSalary")) {
	            basicSalary = basicSalary.add(r.getAmount());
	        }
	    }
	    return basicSalary;
	}

	private BigDecimal calculateOvertimepay(ArrayList<FinancialRecord> records) {
	    BigDecimal overtimepay = BigDecimal.ZERO;
	    for(FinancialRecord r : records) {
	        if(r.getDescription().equalsIgnoreCase("overtimepay")) {
	            overtimepay = overtimepay.add(r.getAmount());
	        }
	    }
	    return overtimepay;
	}

	private BigDecimal calculateDeductions(ArrayList<FinancialRecord> records) {
	    BigDecimal deductions = BigDecimal.ZERO;
	    for(FinancialRecord r : records) {
	        if(r.getRecordType().equalsIgnoreCase("Deduction")) {
	            deductions = deductions.add(r.getAmount());
	        }
	    }
	    return deductions;
	}




	


	@Override
	public void GetPayrollById(int id) throws DatabaseConnectionException, PayrollGenerationException {
		if (id <= 0) {
		    throw new IllegalArgumentException("Payroll ID must be a positive integer.");
		}
		String query = "SELECT * FROM payroll WHERE payrollid = ?";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, id);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	            	Payroll payroll = new Payroll(
                            rs.getInt("payrollid"),
                            rs.getBigDecimal("basicsalary"),
                            rs.getBigDecimal("overtimepay"),
                            rs.getBigDecimal("deductions"),
                            rs.getBigDecimal("netsalary"),
                            new Employees(rs.getInt("employeeid")),
                            rs.getString("payperiodstartdate"),
                            rs.getString("payperiodenddate")
                    );

	                System.out.println("Payroll ID: " + payroll.getPayrollId() +
	                        " | Employee ID: " + payroll.getEmployeeId().getEmployeeId() +
	                        " | PayPeriod Start Date: " + payroll.getPayperiodStartTime() +
	                        " | PayPeriod End Date: " + payroll.getPayperiodEndTime() +
	                        " | Basic Salary: " + payroll.getBasicSalary() +
	                        " | Overtime Pay: " + payroll.getOverTimePay() +
	                        " | Deductions: " + payroll.getDeductions() +
	                        " | Net Salary: " + payroll.getNetSalary()+
	                        " | Gross Salary: "+payroll.getGrossSalary());
	            } else {
	                throw new PayrollGenerationException("Payroll ID " + id + " not found.");
	            }
	        }
	    } catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}


	@Override
	public void GetPayrollsForEmployee(int employee_id) throws DatabaseConnectionException, EmployeeNotFoundException,
	PayrollGenerationException {
		// TODO Auto-generated method stub
		if (employee_id <= 0) {
		    throw new IllegalArgumentException("Employee ID must be a positive integer.");
		}
		ArrayList<Payroll>payroll=new ArrayList<>();
		String query = "SELECT * FROM payroll WHERE employeeid = ?";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, employee_id);
	        try (ResultSet rs = ps.executeQuery()) {
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
	            }
	            for(Payroll p:payroll) {
	                
	                System.out.println("Payroll ID: " + p.getPayrollId() +
	                        " | Employee ID: " + p.getEmployeeId().getEmployeeId() +
	                        " | PayPeriod Start Date: " + p.getPayperiodStartTime() +
	                        " | PayPeriod End Date: " + p.getPayperiodEndTime() +
	                        " | Basic Salary: " + p.getBasicSalary() +
	                        " | Overtime Pay: " + p.getOverTimePay() +
	                        " | Deductions: " + p.getDeductions() +
	                        " | Net Salary: " + p.getNetSalary()+
	                        " | Gross Salary: "+p.getGrossSalary());
	            }
	        }
	    } catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}

	@Override
	public void GetPayrollsForPeriod(String startDate, String endDate)
			throws DatabaseConnectionException, PayrollGenerationException {
		// TODO Auto-generated method stub
		if (startDate == null || startDate.trim().isEmpty()) {
		    throw new IllegalArgumentException("Start date must not be null or empty.");
		}
		if (endDate == null || endDate.trim().isEmpty()) {
		    throw new IllegalArgumentException("End date must not be null or empty.");
		}

		String query = "SELECT * FROM payroll WHERE payperiodstartdate=? and payperiodenddate=?";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, startDate);
	        ps.setString(2, endDate);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	            	Payroll payroll = new Payroll(
                            rs.getInt("payrollid"),
                            rs.getBigDecimal("basicsalary"),
                            rs.getBigDecimal("overtimepay"),
                            rs.getBigDecimal("deductions"),
                            rs.getBigDecimal("netsalary"),
                            new Employees(rs.getInt("employeeid")),
                            rs.getString("payperiodstartdate"),
                            rs.getString("payperiodenddate")
                    );

	                System.out.println("Payroll ID: " + payroll.getPayrollId() +
	                        " | Employee ID: " + payroll.getEmployeeId().getEmployeeId() +
	                        " | PayPeriod Start Date: " + payroll.getPayperiodStartTime() +
	                        " | PayPeriod End Date: " + payroll.getPayperiodEndTime() +
	                        " | Basic Salary: " + payroll.getBasicSalary() +
	                        " | Overtime Pay: " + payroll.getOverTimePay() +
	                        " | Deductions: " + payroll.getDeductions() +
	                        " | Net Salary: " + payroll.getNetSalary()+
	                        " | Gross Salary: "+payroll.getGrossSalary());
	            } else {
	                throw new PayrollGenerationException("Error retrieving payroll");
	            }
	        }
	    } catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}

	@Override
	public void GetAllPayrolls() throws PayrollGenerationException {
		// TODO Auto-generated method stub
		ArrayList<Payroll> p=new ArrayList<>();
		String query="Select * from payroll";
		try(Statement s=conn.createStatement();ResultSet rs=s.executeQuery(query)){
			while (rs.next()) {
                p.add(new Payroll(
                        rs.getInt("payrollid"),
                        rs.getBigDecimal("basicsalary"),
                        rs.getBigDecimal("overtimepay"),
                        rs.getBigDecimal("deductions"),
                        rs.getBigDecimal("netsalary"),
                        new Employees(rs.getInt("employeeid")),
                        rs.getString("payperiodstartdate"),
                        rs.getString("payperiodenddate")
                ));
			}
			
			for(Payroll payroll:p) {
				System.out.println("Payroll ID: " + payroll.getPayrollId() +
                        " | Employee ID: " + payroll.getEmployeeId().getEmployeeId() +
                        " | PayPeriod Start Date: " + payroll.getPayperiodStartTime() +
                        " | PayPeriod End Date: " + payroll.getPayperiodEndTime() +
                        " | Basic Salary: " + payroll.getBasicSalary() +
                        " | Overtime Pay: " + payroll.getOverTimePay() +
                        " | Deductions: " + payroll.getDeductions() +
                        " | Net Salary: " + payroll.getNetSalary()+
                        " | Gross Salary: "+payroll.getGrossSalary());
			}
		} catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}



	@Override
	public void DeletePayroll(int employee_id, String startDate, String endDate) 
			throws PayrollGenerationException {
		// TODO Auto-generated method stub
		if (employee_id <= 0) {
		    throw new IllegalArgumentException("Employee ID must be a positive integer.");
		}
		if (startDate == null || startDate.trim().isEmpty()) {
		    throw new IllegalArgumentException("Start date must not be null or empty.");
		}
		if (endDate == null || endDate.trim().isEmpty()) {
		    throw new IllegalArgumentException("End date must not be null or empty.");
		}
		String query="delete from payroll where employeeid=? and "
				+ "payperiodstartdate=? and payperiodenddate=?";
		try(PreparedStatement ps=conn.prepareStatement(query)){
			ps.setInt(1, employee_id);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ps.executeUpdate();
		} catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}



	@Override
	public void DeletePayrollforEmployee(int employee_id) throws PayrollGenerationException{
		// TODO Auto-generated method stub
		if (employee_id <= 0) {
		    throw new IllegalArgumentException("Employee ID must be a positive integer.");
		}
		String query="delete from payroll where employeeid=?";
		try {
			try(PreparedStatement ps=conn.prepareStatement(query)){
				ps.setInt(1, employee_id);
				ps.executeUpdate();
			} 
		} catch (SQLException e) {
	        throw new PayrollGenerationException("Error retrieving payroll: " + e.getMessage());
	    }
	}

	

	

	
}
