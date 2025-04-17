package com.hexaware.payexpert.entity;

import java.time.LocalDate;
import java.time.Period;

public class Employees {
	
	private int employeeId;
	private String fName,lName,gender,email,phone,address,position;
	private String DOB,joiningDate,terminationDate;
	private int age;
	//Constructor creation
	
	public Employees(int empId) {
		super();
		this.employeeId=empId;
	}
	public Employees(String fName, String lName, String dOB, String gender, String email, String phone,
			String address, String position, String joiningDate, String terminationDate) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.DOB = dOB;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.position = position;
		this.joiningDate = joiningDate;
		this.terminationDate = terminationDate;
		this.age=calculateAge(LocalDate.parse(this.DOB));
	}
	
	public Employees(int employeeId,String fName, String lName, String dOB, String gender, String email, String phone,
			String address, String position, String joiningDate, String terminationDate) {
		super();
		this.employeeId=employeeId;
		this.fName = fName;
		this.lName = lName;
		this.DOB = dOB;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.position = position;
		this.joiningDate = joiningDate;
		this.terminationDate = terminationDate;
		this.age=calculateAge(LocalDate.parse(this.DOB));
	}
	
	
	
	
	
	//Getters and Setters
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	
	
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	
	
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		this.DOB = dOB;
	}
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	public String getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}
	
	
	public String getTerminationDate() {
		return terminationDate;
	}
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}
	
	
	public int getAge() {
		return age;
	}
	/*public void setAge(int age) {
		this.age = age;
	}*/



	//calculate age
	public static int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }
	
	
}
