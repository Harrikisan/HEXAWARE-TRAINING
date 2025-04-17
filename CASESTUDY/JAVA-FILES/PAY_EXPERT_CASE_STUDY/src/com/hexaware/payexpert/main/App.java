package com.hexaware.payexpert.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.hexaware.payexpert.util.DatabaseConnection;
import com.hexaware.payexpert.dao.implementations.UserdaoImpl;
import com.hexaware.payexpert.exception.*;

public class App {

    public static void main(String[] args) throws DatabaseConnectionException {
        Scanner sc = new Scanner(System.in);

        try {
            String fileName = "Resources/db.properties";

            Connection conn = DatabaseConnection.getConnection(fileName);

            UserdaoImpl user = new UserdaoImpl(conn);

            while (true) {
                System.out.println("\nPAY-EXPERT");
                System.out.println("----------");
                System.out.println("Enter your choice:");
                System.out.println(""
    					+ "1.Manage Employee \n"
    					+ "2.Manage Payroll \n"
    					+ "3.Manage Tax \n"
    					+ "4.Manage Financial records \n"
    					+ "5.Generate Report \n"
    					+ "6.Exit ");

                int n;
                try {
                    n = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                    continue;
                }

                switch (n) {
                    case 1 -> user.manageEmployee();
                    case 2 -> user.managePayroll();
                    case 3 -> user.manageTax();
                    case 4 -> user.manageFinancialRecord();
                    case 5-> user.ReportGeneration();
                    case 6 -> {
                        System.out.println("Exiting");
                        return;
                    }
                    default -> System.out.println("Enter a valid choice.");
                }
            }

        } catch (SQLException e) {

        	throw new DatabaseConnectionException("An error occurred: " + e.getMessage());
            

        } catch (Exception e) {
        	throw new DatabaseConnectionException("An error occurred: " + e.getMessage());
            
        } finally {
            sc.close();
        }
    }
}
