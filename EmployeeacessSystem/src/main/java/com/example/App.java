package com.example;

import java.util.*;

public class App {

    static class Employee {
        String employeeId;
        String name;
        int age;
        String department;
        String employmentType;
        String securityClearance;
        boolean idValid;
        String accessLevel;

        Employee(String employeeId, String name, int age, String department,
                 String employmentType, String securityClearance,
                 boolean idValid, String accessLevel) {

            this.employeeId = employeeId;
            this.name = name;
            this.age = age;
            this.department = department;
            this.employmentType = employmentType;
            this.securityClearance = securityClearance;
            this.idValid = idValid;
            this.accessLevel = accessLevel;
        }
    }

    static class EligibilityResult {
        String status;
        List<String> reasons;

        EligibilityResult(String status, List<String> reasons) {
            this.status = status;
            this.reasons = reasons;
        }
    }

    public static EligibilityResult checkEligibility(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("Employee details cannot be null");
        }

        List<String> reasons = new ArrayList<>();

        if (employee.employeeId == null ||
                employee.employeeId.trim().isEmpty()) {
            reasons.add("Employee ID is missing");
        }

        if (employee.name == null ||
                employee.name.trim().isEmpty()) {
            reasons.add("Employee name is missing");
        }

        if (employee.age < 21) {
            reasons.add("Employee must be at least 21 years old");
        }

        Set<String> authorizedDepartments = new HashSet<>(
                Arrays.asList("IT", "HR", "Finance", "Administration")
        );

        if (employee.department == null ||
                !authorizedDepartments.contains(employee.department)) {
            reasons.add("Department is not authorized");
        }

        if (employee.employmentType == null ||
                !employee.employmentType.equalsIgnoreCase("Active")) {
            reasons.add("Employee does not have active employment status");
        }

        if (!employee.idValid) {
            reasons.add("Employee ID is invalid");
        }

        Map<String, Integer> clearanceLevel = new HashMap<>();
        clearanceLevel.put("None", 0);
        clearanceLevel.put("Basic", 1);
        clearanceLevel.put("Confidential", 2);
        clearanceLevel.put("Secret", 3);

        int employeeClearance =
                clearanceLevel.getOrDefault(employee.securityClearance, -1);

        int requiredClearance =
                clearanceLevel.getOrDefault(employee.accessLevel, -1);

        if (employee.accessLevel != null &&
                !employee.accessLevel.equalsIgnoreCase("Public")) {

            if (employeeClearance < requiredClearance) {
                reasons.add(
                    "Insufficient security clearance for requested access"
                );
            }
        }

        if (!reasons.isEmpty()) {

            boolean onlySecurityFailure =
                    reasons.size() == 1 &&
                    reasons.get(0).contains("security clearance");

            if (onlySecurityFailure) {
                return new EligibilityResult(
                        "Conditionally Eligible", reasons
                );
            }

            return new EligibilityResult(
                    "Not Eligible", reasons
            );
        }

        return new EligibilityResult(
                "Eligible", reasons
        );
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter number of employees: ");
            int count = Integer.parseInt(scanner.nextLine());

            if (count <= 0) {
                throw new IllegalArgumentException(
                        "Number of employees must be greater than zero"
                );
            }

            for (int i = 1; i <= count; i++) {

                System.out.println("\nEmployee " + i);

                System.out.print("Employee ID: ");
                String id = scanner.nextLine();

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine());

                System.out.print("Department: ");
                String department = scanner.nextLine();

                System.out.print("Employment Type (Active/Inactive): ");
                String employment = scanner.nextLine();

                System.out.print(
                        "Security Clearance (None/Basic/Confidential/Secret): "
                );
                String clearance = scanner.nextLine();

                System.out.print("Is Employee ID valid? (true/false): ");
                boolean idValid = Boolean.parseBoolean(scanner.nextLine());

                System.out.print(
                        "Requested Access (Public/Basic/Confidential/Secret): "
                );
                String access = scanner.nextLine();

                Employee employee = new Employee(
                        id,
                        name,
                        age,
                        department,
                        employment,
                        clearance,
                        idValid,
                        access
                );

                EligibilityResult result =
                        checkEligibility(employee);

                System.out.println("\nResult: " + result.status);

                if (!result.reasons.isEmpty()) {
                    System.out.println("Reasons:");

                    for (String reason : result.reasons) {
                        System.out.println("- " + reason);
                    }
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}