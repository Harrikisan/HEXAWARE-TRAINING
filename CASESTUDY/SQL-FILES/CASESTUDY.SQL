create database payroll;

use payroll;

create table employee(
	employeeid int primary key auto_increment,
    firstname varchar(50),
    lastname varchar(50),
    dateofbirth date,
    gender varchar(50),
    email varchar(50),
    phonenumber varchar(50),
    address varchar(100),
    position varchar(50),
    joiningdate date,
    terminationdate date
);

create table payroll(
	payrollid int primary key auto_increment,
    employeeid int,
    payperiodstartdate date,
    payperiodenddate date,
    basicsalary int,
    overtimepay int,
    deductions int,
    netsalary int,
    foreign key(employeeid) references employee(employeeid) 
);

create table tax (
	taxid int primary key auto_increment,
    employeeid int,
    taxyear year,
    taxableincome double,
    taxamount double,
    foreign key(employeeid) references employee(employeeid)
);

create table financialrecord(
	recordid int primary key auto_increment,
    employeeid int,
    recorddate date,
    description varchar(100),
    amount double,
    recordtype varchar(50),
    foreign key(employeeid) references employee(employeeid)
);


alter table payroll modify basicsalary DECIMAL(18,2),modify overtimepay DECIMAL(18,2),modify deductions DECIMAL(18,2),modify netsalary DECIMAL(18,2);
alter table tax modify taxableincome DECIMAL(18,2),modify taxamount DECIMAL(18,2);
alter table financialrecord modify amount DECIMAL(18,2);
alter table payroll add grosssalary Decimal(18,2)
ALTER TABLE payroll
modify grosssalary DECIMAL(18,2) 
GENERATED ALWAYS AS (basicsalary + overtimepay) STORED;