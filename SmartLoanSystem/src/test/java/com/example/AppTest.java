package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void lowRiskCustomerTest() throws Exception {

        App.Customer customer = new App.Customer(
                "John",
                30,
                "GOV12345",
                50000,
                800,
                10000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 500000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Approved", result.status);
        assertEquals("Low Risk", result.riskLevel);
        assertEquals(20.0, result.dti, 0.01);
        assertEquals(1000000, result.maximumLoanAmount, 0.01);
    }

    @Test
    void minimumAgeBoundaryTest() throws Exception {

        App.Customer customer = new App.Customer(
                "David",
                21,
                "GOV123",
                50000,
                750,
                10000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 300000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Approved", result.status);
    }

    @Test
    void minimumCreditScoreBoundaryTest()
            throws Exception {

        App.Customer customer = new App.Customer(
                "Alex",
                25,
                "GOV456",
                50000,
                650,
                10000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 300000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Medium Risk", result.riskLevel);
        assertEquals(
                "Conditionally Approved",
                result.status
        );
    }

    @Test
    void maximumDTIBoundaryTest()
            throws Exception {

        App.Customer customer = new App.Customer(
                "Robert",
                30,
                "GOV789",
                50000,
                700,
                25000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 500000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals(50.0, result.dti, 0.01);
        assertEquals("Medium Risk", result.riskLevel);
        assertEquals(
                "Conditionally Approved",
                result.status
        );
    }

    @Test
    void lowIncomeTest() throws Exception {

        App.Customer customer = new App.Customer(
                "Sam",
                30,
                "GOV111",
                20000,
                750,
                5000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 200000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Rejected", result.status);

        assertTrue(result.reasons.contains(
                "Monthly income must be at least Rs.25000"
        ));
    }

    @Test
    void lowCreditScoreTest() throws Exception {

        App.Customer customer = new App.Customer(
                "Peter",
                30,
                "GOV222",
                50000,
                600,
                10000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 300000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Rejected", result.status);

        assertTrue(result.reasons.contains(
                "Credit score must be at least 650"
        ));
    }

    @Test
    void loanAmountExceedsLimitTest()
            throws Exception {

        App.Customer customer = new App.Customer(
                "Mark",
                30,
                "GOV333",
                50000,
                750,
                10000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 1100000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Rejected", result.status);

        assertTrue(result.reasons.contains(
                "Requested loan exceeds maximum permissible amount"
        ));
    }

    @Test
    void highDTITest() throws Exception {

        App.Customer customer = new App.Customer(
                "James",
                30,
                "GOV444",
                50000,
                750,
                30000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 500000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Rejected", result.status);

        assertTrue(result.reasons.contains(
                "Debt-to-income ratio exceeds 50%"
        ));
    }

    @Test
    void multipleFailureTest()
            throws Exception {

        App.Customer customer = new App.Customer(
                "Invalid Customer",
                20,
                "",
                15000,
                500,
                30000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 500000);

        App.CreditAssessment result =
                App.assessLoan(application);

        assertEquals("Rejected", result.status);

        assertTrue(result.reasons.contains(
                "Customer must be at least 21 years old"
        ));

        assertTrue(result.reasons.contains(
                "Government ID is invalid"
        ));

        assertTrue(result.reasons.contains(
                "Monthly income must be at least Rs.25000"
        ));

        assertTrue(result.reasons.contains(
                "Credit score must be at least 650"
        ));

        assertTrue(result.reasons.contains(
                "Requested loan exceeds maximum permissible amount"
        ));

        assertTrue(result.reasons.contains(
                "Debt-to-income ratio exceeds 50%"
        ));
    }

    @Test
    void nullApplicationTest() {

        assertThrows(
                App.InvalidLoanApplicationException.class,
                () -> App.assessLoan(null)
        );
    }

    @Test
    void negativeInputTest() {

        App.Customer customer = new App.Customer(
                "Test",
                25,
                "GOV555",
                -1000,
                700,
                5000
        );

        App.LoanApplication application =
                new App.LoanApplication(customer, 100000);

        assertThrows(
                App.InvalidLoanApplicationException.class,
                () -> App.assessLoan(application)
        );
    }
}