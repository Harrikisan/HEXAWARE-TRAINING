package com.hexaware.payexpert.test;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.hexaware.payexpert.dao.implementations.EmployeedaoImpl;
import com.hexaware.payexpert.dao.interfaces.Employeedao;
import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.util.DatabaseConnection;


class EmployeedaoTest {

	static Employeedao employee;
	static Connection conn;
	static int empid;
	static String email;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		 conn = DatabaseConnection.getConnection("Resources/db.properties");
		 employee=new EmployeedaoImpl(conn);
		 
	}
	@BeforeEach
	void logEmpId() {
	    System.out.println("Current empid: " + empid);
	}
	
	@Test
    @Order(1)
    public void testAddEmployee() throws Exception {
        Employees emp = new Employees("fName", "lName", "1995-01-01", "Male",
                "test@example.com", "9876543210", "Test Address", "Developer",
                "2022-01-01", null);

        employee.AddEmployee(emp);
        
        String query="Select * from employee where email=?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, emp.getEmail());

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	            	empid = rs.getInt("employeeid");
	            	email=rs.getString("email");
	            }
	        }
        }

        
        System.out.println(" Employee added with ID: " + empid);
    }
	
	@Test
    @Order(2)
    public void testGetAllEmployees() {
        assertDoesNotThrow(() -> {
            employee.GetAllEmployees();
        });
    }
	
	@Test
    @Order(3)
    public void testGetEmployeeById() {
        assertDoesNotThrow(() -> {
            employee.GetEmployeeById(empid);
        });
    }
	
	@Test
    @Order(4)
    public void testUpdateEmployee() {
        Employees emp = new Employees(empid, "UfName", "UlName", "1995-01-01", "Female",
                "updateduser@example.com", "9876543210", "Updated Address", "Developer",
                "2022-01-01", null);
        
        assertDoesNotThrow(() -> employee.UpdateEmployee(emp));
    }
	
	
	@AfterAll
    static void tearDownAfterClass() throws Exception {
		if (empid > 0) {
            String query = "DELETE FROM employee WHERE employeeid=?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, empid);
                ps.executeUpdate();
            }
        }
		if (conn != null) {
            conn.close();
        }
    }
	
	
	

}
