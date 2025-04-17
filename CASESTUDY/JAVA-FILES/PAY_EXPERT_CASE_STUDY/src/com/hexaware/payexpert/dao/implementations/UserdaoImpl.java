package com.hexaware.payexpert.dao.implementations;

import com.hexaware.payexpert.dao.interfaces.Userdao;
import com.hexaware.payexpert.entity.Employees;
import com.hexaware.payexpert.entity.FinancialRecord;
import com.hexaware.payexpert.exception.DatabaseConnectionException;
import com.hexaware.payexpert.exception.EmployeeNotFoundException;
import com.hexaware.payexpert.exception.FinancialRecordException;
import com.hexaware.payexpert.exception.InputValidateionException;
import com.hexaware.payexpert.exception.PayrollGenerationException;
import com.hexaware.payexpert.exception.TaxCalculationException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserdaoImpl implements Userdao {

    private EmployeedaoImpl employee;
    private PayrolldaoImpl payroll;
    private TaxdaoImpl tax;
    private FinancialRecorddaoImpl record;
    private ReportdaoImpl report;
    private Scanner sc = new Scanner(System.in);

    public UserdaoImpl(Connection conn) {
        this.employee = new EmployeedaoImpl(conn);
        this.payroll = new PayrolldaoImpl(conn);
        this.tax = new TaxdaoImpl(conn);
        this.record = new FinancialRecorddaoImpl(conn);
        this.report = new ReportdaoImpl(conn);
    }

    public void manageEmployee() throws DatabaseConnectionException, EmployeeNotFoundException, InputValidateionException {
        System.out.println("Enter your choice");
        System.out.println(
            "1.Get employee by Id \n" +
            "2.Get all employees \n" +
            "3.Add employee \n" +
            "4.Update employee \n" +
            "5.Remove employee"
        );
        int n = sc.nextInt();
        sc.nextLine();
        switch (n) {
            case 1 -> {
                System.out.println("Enter ID");
                int id = sc.nextInt();
                sc.nextLine();
                employee.GetEmployeeById(id);
            }
            case 2 -> employee.GetAllEmployees();
            case 3 -> {
                System.out.println("Enter FirstName");
                String fName = sc.nextLine();
                System.out.println("Enter LastName");
                String lName = sc.nextLine();
                System.out.println("Enter DOB (YYYY-MM-DD)");
                String DOB = sc.nextLine();
                System.out.println("Enter Gender (Male/Female)");
                String gender = sc.nextLine();
                System.out.println("Enter email");
                String email = sc.nextLine();
                System.out.println("Enter phone number");
                String phone = sc.nextLine();
                System.out.println("Enter address");
                String address = sc.nextLine();
                System.out.println("Enter position (ProjectManager|ProductManager|ScrumMaster|AgileCoach|Developer)");
                String position = sc.nextLine();
                System.out.println("Enter joining date (YYYY-MM-DD)");
                String sDate = sc.nextLine();
                System.out.println("Whether the employee has a termination date? (Y/N)");
                String s = sc.nextLine();
                String tDate = null;
                if (s.equalsIgnoreCase("Y")) {
                    System.out.println("Enter termination date (YYYY-MM-DD)");
                    tDate = sc.nextLine();
                }
                Employees emp = new Employees(fName, lName, DOB, gender, email, phone, address, position, sDate, tDate);
                employee.AddEmployee(emp);
            }
            case 4 -> {
                System.out.println("Enter employeeid");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter new FirstName");
                String fName = sc.nextLine();
                System.out.println("Enter new LastName");
                String lName = sc.nextLine();
                System.out.println("Enter new DOB (YYYY-MM-DD)");
                String DOB = sc.nextLine();
                System.out.println("Enter new Gender (Male/Female)");
                String gender = sc.nextLine();
                System.out.println("Enter new email");
                String email = sc.nextLine();
                System.out.println("Enter new phone number");
                String phone = sc.nextLine();
                System.out.println("Enter new address");
                String address = sc.nextLine();
                System.out.println("Enter new position (ProjectManager|ProductManager|ScrumMaster|AgileCoach|Developer)");
                String position = sc.nextLine();
                System.out.println("Enter new joing date (YYYY-MM-DD)");
                String sDate = sc.nextLine();
                System.out.println("Whether the employee have a termination date?(Y/N)");
                String s = sc.nextLine();
                String tDate = null;
                if (s.equalsIgnoreCase("Y")) {
                    System.out.println("Enter termination date (YYYY-MM-DD)");
                    tDate = sc.nextLine();
                }
                Employees emp = new Employees(id, fName, lName, DOB, gender, email, phone, address, position, sDate, tDate);
                employee.UpdateEmployee(emp);
            }
            case 5 -> {
                System.out.println("Enter employee id");
                int id = sc.nextInt();
                sc.nextLine();
                employee.RemoveEmployee(id);
            }
        }
    }

    public void managePayroll() throws DatabaseConnectionException, PayrollGenerationException, EmployeeNotFoundException {
        System.out.println("Enter your choice");
        System.out.println(
            "1.Generate payroll \n" +
            "2.Get payroll by ID \n" +
            "3.Get payrolls for employee \n" +
            "4.Get payrolls for period \n" +
            "5.Get all payrolls \n" +
            "6.Delete payroll \n" +
            "7.Delete payroll for employee"
        );
        int n = sc.nextInt();
        sc.nextLine();
        switch (n) {
            case 1 -> {
                System.out.println("Enter employee ID:");
                int empId = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter pay period start date(YYYY-MM-DD)");
                String sDate = sc.nextLine();
                System.out.println("Enter pay period end date(YYYY-MM-DD)");
                String eDate = sc.nextLine();
                payroll.GeneratePayroll(empId, sDate, eDate);
            }
            case 2 -> {
                System.out.println("Enter payroll ID");
                int id = sc.nextInt();
                sc.nextLine();
                payroll.GetPayrollById(id);
            }
            case 3 -> {
                System.out.println("Enter employee id:");
                int empId = sc.nextInt();
                sc.nextLine();
                payroll.GetPayrollsForEmployee(empId);
            }
            case 4 -> {
                System.out.println("Enter pay period start date(YYYY-MM-DD)");
                String sDate = sc.nextLine();
                System.out.println("Enter pay period end date(YYYY-MM-DD)");
                String eDate = sc.nextLine();
                payroll.GetPayrollsForPeriod(sDate, eDate);
            }
            case 5 -> payroll.GetAllPayrolls();
            case 6 -> {
                System.out.println("Enter employee ID:");
                int empId = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter pay period start date(YYYY-MM-DD)");
                String sDate = sc.nextLine();
                System.out.println("Enter pay period end date(YYYY-MM-DD)");
                String eDate = sc.nextLine();
                payroll.DeletePayroll(empId, sDate, eDate);
            }
            case 7 -> {
                System.out.println("Enter employee id:");
                int empId = sc.nextInt();
                sc.nextLine();
                payroll.DeletePayrollforEmployee(empId);
            }
        }
    }

    public void manageTax() throws DatabaseConnectionException, EmployeeNotFoundException, TaxCalculationException {
        System.out.println("Enter your choice");
        System.out.println(
            "1.Calculate Tax \n" +
            "2.Get Tax by Id \n" +
            "3.Get Tax for employee \n" +
            "4.Get Tax for year \n" +
            "5.Delete tax record \n" +
            "6.Get all tax data"
        );
        int n = sc.nextInt();
        sc.nextLine();
        switch (n) {
            case 1 -> {
                System.out.println("Enter employeeid");
                int empId = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter tax year");
                String year = sc.nextLine();
                tax.CalculateTax(empId, year);
            }
            case 2 -> {
                System.out.println("Enter tax id");
                int taxId = sc.nextInt();
                sc.nextLine();
                tax.GetTaxById(taxId);
            }
            case 3 -> {
                System.out.println("Enter employee id");
                int empId = sc.nextInt();
                sc.nextLine();
                tax.GetTaxForEmployee(empId);
            }
            case 4 -> {
                System.out.println("Enter year");
                String year = sc.nextLine();
                tax.GetTaxesForYear(year);
            }
            case 5 -> {
                System.out.println("Enter employeeId");
                int empId = sc.nextInt();
                sc.nextLine();
                tax.DeleteTaxRecord(empId);
            }
            case 6 -> tax.GetAllTaxData();
        }
    }

    public void manageFinancialRecord() throws DatabaseConnectionException, FinancialRecordException, EmployeeNotFoundException {
        System.out.println("Enter your choice");
        System.out.println(
            "1.Add Financial record \n" +
            "2.Update Financial record \n" +
            "3.Get Financial record by id \n" +
            "4.Get Financial record for employee \n" +
            "5.Get Financial records for date \n" +
            "6.Delete Financial record"
        );
        int n = sc.nextInt();
        sc.nextLine();
        switch (n) {
            case 1 -> {
                System.out.println("Enter employee id");
                int id = sc.nextInt();
                sc.nextLine();
                Employees emp = new Employees(id);
                System.out.println("Enter record date (YYYY-MM-DD)");
                String rDate = sc.nextLine();
                System.out.println("Enter description");
                String desc = sc.nextLine();
                System.out.println("Enter amount");
                BigDecimal amount = sc.nextBigDecimal();
                sc.nextLine();
                System.out.println("Enter record type (Income|Deduction)");
                String recordType = sc.nextLine();
                FinancialRecord r = new FinancialRecord(emp, rDate, desc, recordType, amount);
                record.AddFinancialRecord(r);
            }
            case 2 -> {
                System.out.println("Enter record Id");
                int recordid = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter new employee id");
                int id = sc.nextInt();
                sc.nextLine();
                Employees emp = new Employees(id);
                System.out.println("Enter new record date (YYYY-MM-DD)");
                String rDate = sc.nextLine();
                System.out.println("Enter new description");
                String desc = sc.nextLine();
                System.out.println("Enter new amount");
                BigDecimal amount = sc.nextBigDecimal();
                sc.nextLine();
                System.out.println("Enter new record type (Income|Expense|Tax payment|Leave)");
                String recordType = sc.nextLine();
                FinancialRecord r = new FinancialRecord(recordid, emp, rDate, desc, recordType, amount);
                record.UpdateFinancialRecord(r);
            }
            case 3 -> {
                System.out.println("Enter record ID");
                int recordId = sc.nextInt();
                sc.nextLine();
                record.GetFinancialRecordById(recordId);
            }
            case 4 -> {
                System.out.println("Enter employee id");
                int empId = sc.nextInt();
                sc.nextLine();
                record.GetFinancialRecordsForEmployee(empId);
            }
            case 5 -> {
                System.out.println("Enter date");
                String date = sc.nextLine();
                record.GetFinancialRecordsForDate(date);
            }
            case 6 -> {
                System.out.println("Enter financial record id");
                int recordId = sc.nextInt();
                sc.nextLine();
                record.DeleteFinancialRecord(recordId);
            }
        }
    }

    
	@Override
	public void ReportGeneration() throws EmployeeNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("Enter id");
		int id=sc.nextInt();
		sc.nextLine();
		System.out.println("Enter year");
		String year=sc.nextLine();
		report.GenerateReportForEmployee(id, year);
	}
}
