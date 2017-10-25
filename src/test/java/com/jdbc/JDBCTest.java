package com.jdbc;

import org.testng.annotations.Test;

import java.sql.*;
import java.util.Scanner;

public class JDBCTest {

    Connection myConn = null;
    PreparedStatement prepMyStmt = null;
    CallableStatement callableMyStmt = null;
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

    public void getConnectionToDBPreparedStmt() {
        try {
            myConn = DriverManager.getConnection(dbURL, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database connection successful!\n");
    }

    @Test(enabled = true)
    public void increaseSalary() {

        try {
            getConnectionToDBPreparedStmt();

            String theDepartment = "Engineering";
            int theIncreaseAmount = 10000;

            //System.out.println("Salaries BEFORE");
            //showSalaries(theDepartment);

            callableMyStmt = myConn.prepareCall("{call increase_salaries_for_department(?, ?)}");

            callableMyStmt.setString(1, theDepartment);
            callableMyStmt.setDouble(2, theIncreaseAmount);

            System.out.println("\n\nCalling stored procedure. increase_salaries_for_department('" + theDepartment + "', '" + theIncreaseAmount + "')");
            callableMyStmt.execute();
            System.out.println("Finished calling stored procedure");

            //System.out.println("\n\nSALARIES AFTER\n");
            //showSalaries(theDepartment);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void greetDepartment() {
        try {
            getConnectionToDBPreparedStmt();

            String theDepartment = "Engineering";

            callableMyStmt = myConn.prepareCall("{call greet_the_department(?)}");

            callableMyStmt.registerOutParameter(1, Types.VARCHAR);
            callableMyStmt.setString(1, theDepartment);

            System.out.println("Calling stored procedure: \t greet_the_department('" + theDepartment + "')");
            callableMyStmt.execute();
            System.out.println("Finished calling stored procedure");

            String theResult = callableMyStmt.getString(1);
            System.out.println("\nThe Result: " + theResult);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void getCountForDepartment() {
        try {
           getConnectionToDBPreparedStmt();

           String theDepartment = "Engineering";

           callableMyStmt = myConn.prepareCall("{call get_count_for_department(?, ?)}");

           callableMyStmt.setString(1, theDepartment);
           callableMyStmt.registerOutParameter(2, Types.INTEGER);

            System.out.println("Calling stored procedure: \t get_count_for_department('" + theDepartment + "')");
            callableMyStmt.execute();
            System.out.println("Finished calling stored procedure");

            int theCount = callableMyStmt.getInt(2);
            System.out.println("\nThe Count = " + theCount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void getEmployeesForDepartment() {
        try {
            getConnectionToDBPreparedStmt();

            String theDepartment = "Engineering";

            callableMyStmt = myConn.prepareCall("{call get_employees_for_department(?)}");

            callableMyStmt.setString(1, theDepartment);

            System.out.println("Calling stored procedure: \t get_employees_for_department('" + theDepartment + "')");
            callableMyStmt.execute();
            System.out.println("Finished calling stored procedure\n");

            myRs = callableMyStmt.getResultSet();
            display(myRs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    @Test(enabled = true)
    public void transactionTest() {
        try {
            getConnectionToDBPreparedStmt();
            myConn.setAutoCommit(false);

            String theDepartment = "HR";
            double salary = 30000.00;

            myStmt = myConn.createStatement();
            myStmt.executeUpdate("DELETE from employees WHERE department='" + theDepartment + "'");
            myStmt.executeUpdate("UPDATE employees SET salary='" + salary + "' WHERE department ='Engineering'");

            System.out.println("Transactions: Deleting employees from department: '" + theDepartment + "'");
            System.out.println("Transactions: Updating employees salary to " + salary + " in department: 'Engineering'");

            System.out.println("\n>> Transaction steps are ready\n");

            askUserIfOkToSave("yes");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void askUserIfOkToSave(String answer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Is it okay to save? yes/no");
        String input = scanner.nextLine();
        try {
            if (input.equalsIgnoreCase("yes")) {
                myConn.commit();
                System.out.println("\n>> Transaction COMMITED. \n");
            } else {
                myConn.rollback();
                System.out.println("\n>> Transaction ROLLED BACK.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    @Test(enabled = true)
    public void readData() {
        try {
            getConnectionToDB();
            myRs = myStmt.executeQuery("select * from employees");

            while (myRs.next()) {
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
    public void insertData() {
        try {
            getConnectionToDB();
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
        try {
            getConnectionToDB();
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
        try {
            getConnectionToDB();
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

    @Test(enabled = true)
    public void preparedStatement() {
        try {
             getConnectionToDBPreparedStmt();
             prepMyStmt = myConn.prepareStatement("select * from employees " +
            "where salary > ? and department = ?");

             prepMyStmt.setDouble(1, 8000);
             prepMyStmt.setString(2, "Legal");

             myRs = prepMyStmt.executeQuery();

             display(myRs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void showSalaries(String theDepartment) {
        try {
            myRs = prepMyStmt.executeQuery("SELECT * FROM employees WHERE department='" + theDepartment + "'");

            while(myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name")
                        + ", " + myRs.getString("email") + ", " + myRs.getString("department") + ", " + myRs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet rs, Statement stmt, Connection conn) {
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

    public void display(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getString("id") + ", " + rs.getString("last_name") + ", " + rs.getString("first_name")
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