package com.jdbc.test;

import org.testng.annotations.Test;

import java.sql.*;
import java.util.Properties;

public class Common {

    Connection myConn = null;
    PreparedStatement prepMyStmt = null;
    Statement myStmt = null;
    ResultSet myRs = null;

    Properties prop = null;

    String URL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    String USER = "student";
    String PASS = "student";

    private void getConnectionToDB() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            myStmt = myConn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\n===============================");
        System.out.println("Database connection successful!");
        System.out.println("===============================\n");
    }

    private void getConnectionToDBPreparedStmt() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\n===============================");
        System.out.println("Database connection successful!");
        System.out.println("===============================\n");
    }

    @Test
    public void test() {
        //comparisonSalary("!=", 90000.00);
        //insertData("Khan", "Isac", "isac@gmail.com", "QA", 70000.00);
        readData();
    }

    public void comparisonSalary(String operator,double salary) {
        try {
            getConnectionToDBPreparedStmt();

            if((operator.equals("<")) || (operator.equals(">")) || (operator.equals("<="))
                    || (operator.equals(">=")) || (operator.equals("!=")) || (operator.equals("=="))) {
                prepMyStmt = myConn.prepareStatement("SELECT * FROM employees WHERE salary " + operator + " ?");
                prepMyStmt.setDouble(1, salary);
                myRs = prepMyStmt.executeQuery();
                display(myRs);
            } else {
                System.out.println("Provide Valid Operator: '<' OR '>'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void insertData(String lastName, String firstName, String email, String department, double salary) {
        try {
            getConnectionToDB();
            insertDataQuery(lastName, firstName, email, department, salary);
            myRs = myStmt.executeQuery("select * from employees");
            while(myRs.next()) {
                display(myRs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void readData() {
        try {
            getConnectionToDB();
            myRs = myStmt.executeQuery("select * from employees");
            while (myRs.next()) {
                display(myRs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readData(String firstName, String lastName) {
        try {
            getConnectionToDBPreparedStmt();
            prepMyStmt = myConn.prepareStatement("SELECT * FROM employees " +
                    "WHERE first_name = ? AND last_name = ?");
            prepMyStmt.setString(1, firstName);
            prepMyStmt.setString(2, lastName);
            myRs = prepMyStmt.executeQuery();
            display(myRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void readData(int id, String firstName, String lastName) {
        try {
            getConnectionToDBPreparedStmt();
            prepMyStmt = myConn.prepareStatement("SELECT * FROM employees " +
                    "WHERE id = ? AND first_name = ? AND last_name = ?");
            prepMyStmt.setInt(1,id);
            prepMyStmt.setString(2, firstName);
            prepMyStmt.setString(3, lastName);
            myRs = prepMyStmt.executeQuery();
            display(myRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    private void close(ResultSet rs, Statement stmt, Connection conn) {
        if((rs != null) && (stmt != null) && (conn != null)) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void display(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getString("last_name") + ", " + rs.getString("first_name")
                        + ", " + rs.getString("email") + ", " + rs.getString("department") + ", " + rs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    private void insertDataQuery(String lastName, String firstName, String email, String department, double salary) {
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