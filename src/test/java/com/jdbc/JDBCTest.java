package com.jdbc;

import org.testng.annotations.Test;

import java.sql.*;

public class JDBCTest {

    Connection myConn = null;
    Statement myStmt = null;
    ResultSet myRs = null;

    String dbURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    String user = "student";
    String pass = "student";

    public void getConnectionToDB() {
        try {
            myConn = DriverManager.getConnection(dbURL, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Database connection successful!\n");
    }

    @Test(enabled = true)
    public void readData() {

        getConnectionToDB();
        try {
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("select * from employees");

            while (myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = true)
    public void insertData() {
        getConnectionToDB();
        try {
            myStmt = myConn.createStatement();
            insertDataStatement("Khan", "Isac", "Isac@gmail.com", "HR", 33000.00);

            myRs = myStmt.executeQuery("select * from employees");

            while(myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(myRs != null) {
                try {
                    myRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertDataStatement(String lastName, String firstName, String email, String department, double salary) {

        try {
            myStmt.executeUpdate("insert into employees " +
                    "(last_name, first_name, email, department, salary)" +
                    "values " +
                    "('" + lastName + "', '" + firstName + "', '" + email + "', '" + department + "', " + salary + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
