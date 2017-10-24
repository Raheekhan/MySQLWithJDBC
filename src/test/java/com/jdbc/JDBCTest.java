package com.jdbc;

import java.sql.*;

public class JDBCTest {

    public static void main(String[] args) {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "student", "student");

            System.out.println("Database connection successful!\n");

            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("select * from employees");

            while(myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
