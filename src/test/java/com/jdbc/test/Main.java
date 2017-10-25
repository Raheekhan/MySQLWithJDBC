package com.jdbc.test;

import org.testng.annotations.Test;

public class Main extends Common {

    @Test(enabled = true)
    public void testReadData() {
        readData();
    }

    @Test(enabled = true)
    public void testInsertData() {
        insertData("Khan", "Rahee", "rahee@gmail.com", "DevOPS", 120000.00);
    }

    @Test(enabled = true)
    public void testUpdateData() {
        updateData("raheekh@gmail.com", "Khan", "Rahee");
    }

    @Test(enabled = true)
    public void testDeleteData() {
        removeData("Khan", "Isac");
    }

    @Test(enabled = true)
    public void testSalaryComparison() {
        comparisonSalary("<", 90000.00);
    }

    @Test(enabled = true)
    public void testIncreaseSalary() {
        increaseSalary("Engineering", 10000.00);
    }
}
