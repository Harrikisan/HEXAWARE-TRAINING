package com.hexaware.payexpert.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.hexaware.payexpert.dao.implementations.TaxdaoImpl;
import com.hexaware.payexpert.dao.interfaces.Taxdao;
import com.hexaware.payexpert.util.DatabaseConnection;

class TaxdaoTest {

    static Taxdao tax;
    static Connection conn;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        conn = DatabaseConnection.getConnection("Resources/db.properties");
        tax = new TaxdaoImpl(conn);
    }
    @Test
    void testVerifyTaxCalculationForHighIncomeEmployee() {
        int employeeId = 29; // ensure this ID exists and has high income financial records in DB
        String taxYear = "2025";
        assertDoesNotThrow(() -> {
            tax.CalculateTax(employeeId, taxYear);
        }, "Tax calculation should not throw exception for high income employee");
    }


}
