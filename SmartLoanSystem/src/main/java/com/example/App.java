package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    static class Customer {
        String name;
        int age;
        String governmentId;
        double monthlyIncome;
        int creditScore;
        double existingObligations;

        Customer(String name, int age, String governmentId,
                 double monthlyIncome, int creditScore,
                 double existingObligations) {

            this.name = name;
            this.age = age;
            this.governmentId = governmentId;
            this.monthlyIncome = monthlyIncome;
            this.creditScore = creditScore;
            this.existingObligations = existingObligations;
        }
    }

    static class LoanApplication {
        Customer customer;
        double requestedAmount;

        LoanApplication(Customer customer, double requestedAmount) {
            this.customer = customer;
            this.requestedAmount = requestedAmount;
        }
    }

    static class CreditAssessment {
        String status;
        String riskLevel;
        double dti;
        double maximumLoanAmount;
        List<String> reasons;

        CreditAssessment(String status, String riskLevel,
                         double dti, double maximumLoanAmount,
                         List<String> reasons) {

            this.status = status;
            this.riskLevel = riskLevel;
            this.dti = dti;
            this.maximumLoanAmount = maximumLoanAmount;
            this.reasons = reasons;
        }
    }

    static class InvalidLoanApplicationException extends Exception {

        InvalidLoanApplicationException(String message) {
            super(message);
        }
    }

    public static CreditAssessment assessLoan(
            LoanApplication application)
            throws InvalidLoanApplicationException {

        if (application == null || application.customer == null) {
            throw new InvalidLoanApplicationException(
                    "Loan application cannot be null");
        }

        Customer c = application.customer;

        if (c.age < 0 ||
                c.monthlyIncome < 0 ||
                c.existingObligations < 0 ||
                c.creditScore < 0 ||
                application.requestedAmount < 0) {

            throw new InvalidLoanApplicationException(
                    "Invalid negative input values");
        }

        List<String> reasons = new ArrayList<>();

        if (c.age < 21) {
            reasons.add(
                    "Customer must be at least 21 years old");
        }

        if (c.governmentId == null ||
                c.governmentId.trim().isEmpty()) {

            reasons.add("Government ID is invalid");
        }

        double minimumIncome = 25000;

        if (c.monthlyIncome < minimumIncome) {
            reasons.add(
                    "Monthly income must be at least Rs.25000");
        }

        int minimumCreditScore = 650;

        if (c.creditScore < minimumCreditScore) {
            reasons.add(
                    "Credit score must be at least 650");
        }

        double maximumDTI = 50.0;

        double dti;

        if (c.monthlyIncome == 0) {
            dti = Double.POSITIVE_INFINITY;
        } else {
            dti = (c.existingObligations /
                    c.monthlyIncome) * 100;
        }

        double maximumLoanAmount =
                c.monthlyIncome * 20;

        if (application.requestedAmount >
                maximumLoanAmount) {

            reasons.add(
                    "Requested loan exceeds maximum permissible amount");
        }

        if (dti > maximumDTI) {
            reasons.add(
                    "Debt-to-income ratio exceeds 50%");
        }

        if (!reasons.isEmpty()) {

            return new CreditAssessment(
                    "Rejected",
                    "High Risk",
                    dti,
                    maximumLoanAmount,
                    reasons
            );
        }

        String riskLevel;

        if (c.creditScore >= 750 && dti < 30) {
            riskLevel = "Low Risk";
        } else if (c.creditScore >= 650 && dti <= 50) {
            riskLevel = "Medium Risk";
        } else {
            riskLevel = "High Risk";
        }

        String status;

        if (riskLevel.equals("Low Risk")) {
            status = "Approved";
        } else if (riskLevel.equals("Medium Risk")) {
            status = "Conditionally Approved";
        } else {
            status = "Rejected";
            reasons.add("Financial risk is too high");
        }

        return new CreditAssessment(
                status,
                riskLevel,
                dti,
                maximumLoanAmount,
                reasons
        );
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter number of customers: ");

            int count = Integer.parseInt(scanner.nextLine());

            if (count <= 0) {
                throw new InvalidLoanApplicationException(
                        "Number of customers must be greater than zero");
            }

            for (int i = 1; i <= count; i++) {

                System.out.println("\nCustomer " + i);

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine());

                System.out.print("Government ID: ");
                String governmentId = scanner.nextLine();

                System.out.print("Monthly Income: ");
                double income =
                        Double.parseDouble(scanner.nextLine());

                System.out.print("Credit Score: ");
                int creditScore =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Existing Monthly Obligations: ");
                double obligations =
                        Double.parseDouble(scanner.nextLine());

                System.out.print("Requested Loan Amount: ");
                double requestedAmount =
                        Double.parseDouble(scanner.nextLine());

                Customer customer = new Customer(
                        name,
                        age,
                        governmentId,
                        income,
                        creditScore,
                        obligations
                );

                LoanApplication application =
                        new LoanApplication(
                                customer,
                                requestedAmount
                        );

                CreditAssessment assessment =
                        assessLoan(application);

                System.out.println("\nLoan Status: "
                        + assessment.status);

                System.out.println("Risk Level: "
                        + assessment.riskLevel);

                System.out.printf(
                        "DTI: %.2f%%%n",
                        assessment.dti
                );

                System.out.printf(
                        "Maximum Permissible Loan: Rs.%.2f%n",
                        assessment.maximumLoanAmount
                );

                if (!assessment.reasons.isEmpty()) {

                    System.out.println("Reasons:");

                    for (String reason : assessment.reasons) {
                        System.out.println("- " + reason);
                    }
                }
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid numeric input.");

        } catch (InvalidLoanApplicationException e) {

            System.out.println(
                    "Application error: " + e.getMessage());

        } finally {

            scanner.close();
        }
    }
}