package presentation;

import model.Customer;
import service.SystemManager;
import utility.ValidationUtili;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class HandleMenu {

    private final SystemManager systemManager = new SystemManager();
    private final Scanner scanner = new Scanner(System.in);

    // Load and display menu file
    private void displayMenu(String fileName) {
        String path = "/resources/" + fileName;

        try (InputStream menuStream = getClass().getResourceAsStream(path)) {
            if (menuStream == null) {
                System.out.println("Error: Menu file not found -> " + path);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(menuStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }

    
    public void showMainMenu() {
        boolean running = true;

        while (running) {
            displayMenu("main_menu.txt");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> System.out.println("[Manage Products - Coming Soon]");
                case "2" -> showCustomerMenu();
                case "3" -> System.out.println("[Invoice Generation - Coming Soon]");
                case "4" -> System.out.println("[Admin Tasks - Coming Soon]");
                case "5" -> {
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
    }

    private void showCustomerMenu() {
        boolean inCustomerMenu = true;

        while (inCustomerMenu) {
            displayMenu("customer_menu.txt");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleAddCustomer();
                case "2" -> handleUpdateCustomer();
                case "3" -> handleDisplayAllCustomers();
                case "4" -> handleSearchCustomer();
                case "5" -> handleDeleteCustomer();
                case "6" -> inCustomerMenu = false;
                default  -> System.out.println("Invalid option. Please enter a number between 1 and 6.");
            }
        }
    }


    private void handleAddCustomer() {
        System.out.println("\n--- Add New Customer ---");

        try {
            System.out.print("Enter Customer ID   : ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Customer Name : ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Email         : ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter Contact No.   : ");
            String contact = scanner.nextLine().trim();

            System.out.print("Enter Address       : ");
            String address = scanner.nextLine().trim();

            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            LocalDate dob = ValidationUtili.parseDate(scanner);

            System.out.print("Enter Gender (M/F/Other): ");
            String gender = scanner.nextLine().trim();

            Customer customer = new Customer(id, name, email, contact, address, dob, gender);
            boolean success = systemManager.addCustomer(customer);

            System.out.println(success
                    ? "Customer added successfully!"
                    : "Failed to add customer. Please try again.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void handleUpdateCustomer() {
        System.out.println("\n--- Update Customer ---");

        try {
            System.out.print("Enter Customer ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Customer existing = systemManager.findCustomerById(id);
            if (existing == null) {
                System.out.println("No customer found with ID: " + id);
                return;
            }

            System.out.println("Current Name    : " + existing.getCustomerName());
            System.out.print("New Name (or press Enter to keep): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) existing.setCustomerName(name);

            System.out.println("Current Email   : " + existing.getEmail());
            System.out.print("New Email (or press Enter to keep): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) existing.setEmail(email);

            System.out.println("Current Contact : " + existing.getContact());
            System.out.print("New Contact (or press Enter to keep): ");
            String contact = scanner.nextLine().trim();
            if (!contact.isEmpty()) existing.setContact(contact);

            System.out.println("Current Address : " + existing.getAddress());
            System.out.print("New Address (or press Enter to keep): ");
            String address = scanner.nextLine().trim();
            if (!address.isEmpty()) existing.setAddress(address);

            System.out.println("Current DOB     : " + existing.getDob());
            System.out.print("New DOB YYYY-MM-DD (or press Enter to keep): ");
            LocalDate dob = ValidationUtili.parseDate(scanner);  // issue, also figure out how to connect the update customer function with parse.

            System.out.println("Current Gender  : " + existing.getGender());
            System.out.print("New Gender (or press Enter to keep): ");
            String gender = scanner.nextLine().trim();
            if (!gender.isEmpty()) existing.setGender(gender);

            boolean success = systemManager.updateCustomer(existing);
            System.out.println(success
                    ? "Customer updated successfully!"
                    : "Failed to update customer. Please try again.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void handleDisplayAllCustomers() {
        System.out.println("\n--- Customer List ---");

        try {
            ArrayList<Customer> customers = systemManager.getAllCustomers();

            if (customers.isEmpty()) {
                System.out.println("No customers found in the system.");
                return;
            }

            System.out.printf("%-5s %-20s %-25s %-15s %-12s %-8s%n",
                    "ID", "Name", "Email", "Contact", "DOB", "Gender");
            System.out.println("-".repeat(90));

            for (Customer c : customers) {
                System.out.printf("%-5d %-20s %-25s %-15s %-12s %-8s%n",
                        c.getCustomerID(),
                        c.getCustomerName(),
                        c.getEmail(),
                        c.getContact(),
                        c.getDob() != null ? c.getDob().toString() : "N/A",
                        c.getGender());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void handleSearchCustomer() {
        System.out.println("\n--- Search Customer ---");

        try {
            System.out.print("Enter Customer ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Customer c = systemManager.findCustomerById(id);

            if (c == null) {
                System.out.println("No customer found with ID: " + id);
            } else {
                System.out.println("\n--- Customer Found ---");
                System.out.println("ID      : " + c.getCustomerID());
                System.out.println("Name    : " + c.getCustomerName());
                System.out.println("Email   : " + c.getEmail());
                System.out.println("Contact : " + c.getContact());
                System.out.println("DOB     : " + (c.getDob() != null ? c.getDob() : "N/A"));
                System.out.println("Gender  : " + c.getGender());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void handleDeleteCustomer() {
        System.out.println("\n--- Delete Customer ---");

        try {
            System.out.print("Enter Customer ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Customer existing = systemManager.findCustomerById(id);
            if (existing == null) {
                System.out.println("No customer found with ID: " + id);
                return;
            }

            System.out.print("Are you sure you want to delete: "
                    + existing.getCustomerName() + "? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("Y")) {
                boolean success = systemManager.deleteCustomer(id);
                System.out.println(success
                        ? "Customer deleted successfully!"
                        : "Failed to delete customer. Please try again.");
            } else {
                System.out.println("Delete cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}