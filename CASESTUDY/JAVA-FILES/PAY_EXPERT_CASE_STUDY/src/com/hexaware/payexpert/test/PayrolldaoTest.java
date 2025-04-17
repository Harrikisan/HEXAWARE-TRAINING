package com.hexaware.payexpert.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.hexaware.payexpert.dao.implementations.PayrolldaoImpl;
import com.hexaware.payexpert.dao.interfaces.Payrolldao;
import com.hexaware.payexpert.entity.Payroll;
import com.hexaware.payexpert.util.DatabaseConnection;

class PayrolldaoTest {

	static Payrolldao payroll;
	static Connection conn;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		 conn = DatabaseConnection.getConnection("Resources/db.properties");
		 payroll=new PayrolldaoImpl(conn);
		 
	}
	
	@Test
    void testGeneratePayroll() {
        assertDoesNotThrow(() -> payroll.GeneratePayroll(29, "2024-04-01", "2024-04-30"));
        assertDoesNotThrow(() -> payroll.GeneratePayroll(30, "2024-04-01", "2024-04-30"));
        assertDoesNotThrow(() -> payroll.GeneratePayroll(31, "2024-04-01", "2024-04-30"));
        
    }
	@Test
    void testCalculateGrossSalary() {
        BigDecimal basicSalary = new BigDecimal("50000.00");
        BigDecimal overtimePay = new BigDecimal("10000.00");
        BigDecimal expectedGrossSalary = basicSalary.add(overtimePay);
        BigDecimal actualGrossSalary = payroll.calculateGrossSalary(basicSalary, overtimePay);
        assertEquals(expectedGrossSalary, actualGrossSalary);
    }
	@Test
	void testCalculateNetSalary() {
	    BigDecimal grossSalary = new BigDecimal("60000.00");
	    BigDecimal overtimePay = new BigDecimal("10000.00");
	    BigDecimal deductions = new BigDecimal("5000.00");
	    
	    BigDecimal expectedNetSalary = grossSalary.add(overtimePay).subtract(deductions);
	    BigDecimal actualNetSalary = payroll.calculateNetSalary(grossSalary, overtimePay, deductions);
	    
	    assertEquals(expectedNetSalary, actualNetSalary);
	}
	@AfterAll
    static void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

}
