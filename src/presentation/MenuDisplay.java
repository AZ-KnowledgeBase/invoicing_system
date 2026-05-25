package presentation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class MenuDisplay {

    private final Scanner scanner = new Scanner(System.in);
    private final CustomerInputHandler inputHandler = new CustomerInputHandler();

    // Load and Display Menu file
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

    // Get user inputs for main menu
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
                case "1" -> inputHandler.handleAddCustomer();
                case "2" -> inputHandler.handleUpdateCustomer();
                case "3" -> inputHandler.handleDisplayAllCustomers();
                case "4" -> inputHandler.handleSearchCustomer();
                case "5" -> inputHandler.handleDeleteCustomer();
                case "6" -> inCustomerMenu = false;
                default  -> System.out.println("Invalid option. Please enter a number between 1 and 6.");
            }
        }
    }
}
