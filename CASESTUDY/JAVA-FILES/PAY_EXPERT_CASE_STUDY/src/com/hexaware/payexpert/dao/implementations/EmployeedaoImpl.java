package com.hexaware.payexpert.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.hexaware.payexpert.dao.interfaces.Employeedao;

import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.InputValidateionException;
import com.hexaware.payexpert.util.DatabaseConnection;

public class EmployeedaoImpl implements Employeedao {

	Connection conn;
	public EmployeedaoImpl(Connection conn) {
		
		this.conn = conn;
	}

	@Override
	public void GetEmployeeById(int id) throws DatabaseConnectionException, EmployeeNotFoundException,
	InputValidateionException {
	    
		if (id <= 0) {
		    throw new InputValidateionException("Employee ID must be a positive integer.");
		}
		
		String query = "SELECT * FROM employee WHERE employeeid = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, id);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Employees emp = new Employees(
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
	                    rs.getString("terminationdate")
	                );

	                System.out.println("ID: "+id+"| FirstName: "+emp.getfName()+ "| LastName: "+emp.getlName()
					+"| DOB: "+emp.getDOB()+"| email: "+emp.getEmail()+"| phonenumber: "+emp.getPhone()
					+"| Address: "+emp.getAddress()+"| Position: "+emp.getPosition() +"|Age: "+emp.getAge()
					+"|Joining date: "+emp.getJoiningDate()+ "|Termination date: "+emp.getTerminationDate()+
					" |Age: "+emp.getAge());
	            } else {
	                throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
	            }
	        }

	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Database error: " + e.getMessage());
	    }
	}

	@Override
	public void GetAllEmployees() throws DatabaseConnectionException, EmployeeNotFoundException {
		// TODO Auto-generated method stub
		ArrayList<Employees> employee=new ArrayList<>();
		String query="Select * from employee";
		
		try(Statement s=conn.createStatement();ResultSet rs=s.executeQuery(query);) {
			while(rs.next()) {
				employee.add(new Employees(
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
			}
			
			for(Employees emp:employee) {
				System.out.println("ID: "+emp.getEmployeeId()+"| FirstName: "+emp.getfName()+ "| LastName: "+emp.getlName()
				+"| DOB: "+emp.getDOB()+"| email: "+emp.getEmail()+"| phonenumber: "+emp.getPhone()
				+"| Address: "+emp.getAddress()+"| Position: "+emp.getPosition() +"|Age: "+emp.getAge()
				+"|Joining date: "+emp.getJoiningDate()+ "|Termination date: "+emp.getTerminationDate()+
				" |Age: "+emp.getAge());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DatabaseConnectionException(e.getMessage());
		}
	}

	@Override
	public void AddEmployee(Employees employee) throws DatabaseConnectionException, InputValidateionException {
		// TODO Auto-generated method stub
		if(isValid(employee)){
			String query="insert into employee(firstname,lastname,dateofbirth,gender,"
					+ "email,phonenumber,address,position,joiningdate,terminationdate,age) \r\n"
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			
			try(PreparedStatement ps=conn.prepareStatement(query)){
				ps.setString(1, employee.getfName());
				ps.setString(2, employee.getlName());
				ps.setString(3, employee.getDOB());
				ps.setString(4, employee.getGender());
				ps.setString(5, employee.getEmail());
				ps.setString(6, employee.getPhone());
				ps.setString(7, employee.getAddress());
				ps.setString(8, employee.getPosition());
				ps.setString(9, employee.getJoiningDate());
				ps.setString(10, employee.getTerminationDate());
				ps.setInt(11,employee.getAge());
				ps.executeUpdate();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new DatabaseConnectionException(e.getMessage());
			}
		}	
		
	}

	

	@Override
	public void UpdateEmployee(Employees employee)
			throws DatabaseConnectionException, InputValidateionException, EmployeeNotFoundException {
		if (employee.getEmployeeId() <= 0) {
		    throw new InputValidateionException("Employee ID must be a positive integer.");
		}
		
		if(isValid(employee)) {
			String query="Update employee set firstname=?,"
					+ "lastname=?,"
					+ "dateofbirth=?,"
					+ "gender=?,"
					+ "email=?,"
					+ "phonenumber=?,"
					+ "address=?,"
					+ "position=?,"
					+ "joiningdate=?,"
					+ "terminationdate=?,"
					+ "age=? where employeeid=?";
			
			try(PreparedStatement ps=conn.prepareStatement(query)){
				ps.setString(1, employee.getfName());
				ps.setString(2, employee.getlName());
				ps.setString(3, employee.getDOB());
				ps.setString(4, employee.getGender());
				ps.setString(5, employee.getEmail());
				ps.setString(6, employee.getPhone());
				ps.setString(7, employee.getAddress());
				ps.setString(8, employee.getPosition());
				ps.setString(9, employee.getJoiningDate());
				ps.setString(10, employee.getTerminationDate());
				ps.setInt(11,employee.getAge());
				ps.setInt(12, employee.getEmployeeId());
				ps.executeUpdate();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new EmployeeNotFoundException("Invalid Id: "+e.getMessage() );
			}
		}
	}

	@Override
	public void RemoveEmployee(int id) throws DatabaseConnectionException, EmployeeNotFoundException, InputValidateionException {
	    if (id <= 0) {
	        throw new InputValidateionException("Employee ID must be a positive integer.");
	    }

	    String query1 = "Delete from payroll where employeeid=?";
	    String query2 = "Delete from tax where employeeid=?";
	    String query3 = "Delete from financialrecord where employeeid=?";
	    String query4 = "Delete from employee where employeeid=?";

	    try {
	        try (PreparedStatement ps1 = conn.prepareStatement(query1)) {
	            ps1.setInt(1, id);
	            ps1.executeUpdate();
	        } catch (Exception e) {
	        }

	        try (PreparedStatement ps2 = conn.prepareStatement(query2)) {
	            ps2.setInt(1, id);
	            ps2.executeUpdate();
	        } catch (Exception e) {
	        }

	        try (PreparedStatement ps3 = conn.prepareStatement(query3)) {
	            ps3.setInt(1, id);
	            ps3.executeUpdate();
	        } catch (Exception e) {
	        }

	        try (PreparedStatement ps4 = conn.prepareStatement(query4)) {
	            ps4.setInt(1, id);
	            ps4.executeUpdate();
	        }

	    } catch (Exception e) {
	        throw new DatabaseConnectionException("Error occurred while deleting employee.");
	    }
	}


	
	private boolean isValid(Employees employee) throws InputValidateionException {
	    
	    if (employee.getfName() == null || employee.getfName().trim().isEmpty()) {
	        throw new InputValidateionException("First name cannot be empty.");
	    }
	    
	    if (employee.getlName() == null || employee.getlName().trim().isEmpty()) {
	        throw new InputValidateionException("Last name cannot be empty.");
	    }
	    
	    try {
	        LocalDate dob = LocalDate.parse(employee.getDOB());
	        if (dob.isAfter(LocalDate.now())) {
	            throw new InputValidateionException("Date of birth cannot be in the future.");
	        }
	    } catch (Exception e) {
	        throw new InputValidateionException("Invalid date of birth format.");
	    }
	    if (employee.getEmail() == null || !employee.getEmail().matches("^[A-Za-z0-9]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
	        throw new InputValidateionException("Invalid email format.");
	    }
	    
	    if (employee.getPhone() == null || !employee.getPhone().matches("\\d{10}")) {
	        throw new InputValidateionException("Phone number must be 10 digits.");
	    }
	    if (employee.getGender() == null || 
	       !(employee.getGender().equalsIgnoreCase("Male") || 
	         employee.getGender().equalsIgnoreCase("Female"))) {
	        throw new InputValidateionException("Gender must be Male or Female.");
	    }
	    ArrayList<String> al=new ArrayList<>();
	    al.add("ProjectManager");
	    al.add("ProductManager");
	    al.add("ScrumMaster");
	    al.add("AgileCoach");
	    al.add("Developer");
	    if(!al.contains(employee.getPosition())){
	    	throw new InputValidateionException("Position should be (ProjectManager|ProductManager|ScrumMaster|AgileCoach|Developer)");
	    }
	    
	    
	    try {
	        LocalDate.parse(employee.getJoiningDate());
	    } catch (Exception e) {
	        throw new InputValidateionException("Invalid joining date format. Expected yyyy-MM-dd.");
	    }
	    
	    if(employee.getTerminationDate()!=null) {
	    	try {
		        LocalDate.parse(employee.getTerminationDate());
		    } catch (Exception e) {
		        throw new InputValidateionException("Invalid joining date format. Expected yyyy-MM-dd.");
		    }
	    }

	    return true;
	}


	

}
