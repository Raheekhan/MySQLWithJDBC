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
            myStmt = myConn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Database connection successful!\n");
    }

    @Test(enabled = true)
    public void readData() {

        getConnectionToDB();
        try {
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
            insertDataStatement("Khan", "Rahee", "rahee@gmail.com", "HR", 33000.00);

            myRs = myStmt.executeQuery("select * from employees");

            while(myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void updateData() {
        getConnectionToDB();
        try {

            System.out.println("BEFORE THE UPDATE ... ");
            displayEmployee("Rahee", "Khan");

            myStmt.executeUpdate(
                    "UPDATE employees SET email='raheek@gmail.com' WHERE first_name='Rahee' AND last_name='Khan'");

            System.out.println("AFTER THE UPDATE ... ");
            displayEmployee("Rahee", "Khan");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void deleteData() {
        getConnectionToDB();
        try {

            System.out.println("BEFORE THE UPDATE ... ");
            displayEmployee("Rahee", "Khan");

            myStmt.executeUpdate("DELETE from employees " +
            "WHERE last_name='Khan' AND first_name='Rahee'");

            System.out.println("AFTER THE UPDATE ... ");
            displayEmployee("Rahee", "Khan");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void close(ResultSet rs, Statement stmt, Connection conn) {
        if((myRs != null) && (myStmt != null) && (myConn != null)) {
            try {
                myRs.close();
                myStmt.close();
                myConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayEmployee(String firstName, String lastName) {
        try {
            myRs = myStmt.executeQuery("select * from employees where first_name='" + firstName + "' and last_name='" + lastName + "'");

            while(myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name")
                + ", " + myRs.getString("email") + ", " + myRs.getString("department") + ", " + myRs.getString("salary"));
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
