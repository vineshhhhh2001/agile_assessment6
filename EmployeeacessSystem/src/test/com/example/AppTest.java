package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void eligibleEmployeeTest() {

        App.Employee employee = new App.Employee(
                "EMP001",
                "John",
                25,
                "IT",
                "Active",
                "Secret",
                true,
                "Confidential"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Eligible", result.status);
        assertTrue(result.reasons.isEmpty());
    }

    @Test
    void minimumAgeBoundaryTest() {

        App.Employee employee = new App.Employee(
                "EMP002",
                "David",
                21,
                "HR",
                "Active",
                "Basic",
                true,
                "Basic"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Eligible", result.status);
    }

    @Test
    void belowMinimumAgeTest() {

        App.Employee employee = new App.Employee(
                "EMP003",
                "Alex",
                20,
                "IT",
                "Active",
                "Secret",
                true,
                "Basic"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Not Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Employee must be at least 21 years old"
        ));
    }

    @Test
    void unauthorizedDepartmentTest() {

        App.Employee employee = new App.Employee(
                "EMP004",
                "Mark",
                30,
                "Sales",
                "Active",
                "Basic",
                true,
                "Basic"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Not Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Department is not authorized"
        ));
    }

    @Test
    void inactiveEmployeeTest() {

        App.Employee employee = new App.Employee(
                "EMP005",
                "Peter",
                30,
                "Finance",
                "Inactive",
                "Basic",
                true,
                "Basic"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Not Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Employee does not have active employment status"
        ));
    }

    @Test
    void invalidIdTest() {

        App.Employee employee = new App.Employee(
                "EMP006",
                "Sam",
                30,
                "IT",
                "Active",
                "Basic",
                false,
                "Basic"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Not Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Employee ID is invalid"
        ));
    }

    @Test
    void insufficientSecurityClearanceTest() {

        App.Employee employee = new App.Employee(
                "EMP007",
                "Robert",
                30,
                "IT",
                "Active",
                "Basic",
                true,
                "Secret"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Conditionally Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Insufficient security clearance for requested access"
        ));
    }

    @Test
    void multipleFailureTest() {

        App.Employee employee = new App.Employee(
                "",
                "",
                19,
                "Sales",
                "Inactive",
                "None",
                false,
                "Secret"
        );

        App.EligibilityResult result =
                App.checkEligibility(employee);

        assertEquals("Not Eligible", result.status);

        assertTrue(result.reasons.contains(
                "Employee ID is missing"
        ));

        assertTrue(result.reasons.contains(
                "Employee name is missing"
        ));

        assertTrue(result.reasons.contains(
                "Employee must be at least 21 years old"
        ));

        assertTrue(result.reasons.contains(
                "Department is not authorized"
        ));

        assertTrue(result.reasons.contains(
                "Employee does not have active employment status"
        ));

        assertTrue(result.reasons.contains(
                "Employee ID is invalid"
        ));
    }

    @Test
    void nullEmployeeTest() {

        assertThrows(
                IllegalArgumentException.class,
                () -> App.checkEligibility(null)
        );
    }
}