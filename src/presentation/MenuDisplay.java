package presentation;

import java.io.BufferedReader;      //Wraps InputStreamReader to allow reading one full line at a time
import java.io.InputStream;         //Reads the menu file as raw bytes from the classpath
import java.io.InputStreamReader;   //Converts those raw bytes into readable characters
import java.io.IOException;         //Handles any failure that occurs while reading the menu file
import java.util.Scanner;

public class MenuDisplay {

    private final Scanner scanner = new Scanner(System.in); //Captures the user's menu choice from the terminal
     private final CustomerInputHandler customerInputHandler = new CustomerInputHandler();
    private final ProductInputHandler productInputHandler = new ProductInputHandler();

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
                case "1" -> showProductMenu();
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

    // Shows product menu, returns to main menu upon exit
    private void showProductMenu() {
        boolean inProductMenu = true;

        while (inProductMenu) {
            displayMenu("product_menu.txt");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> productInputHandler.handleAddProduct();
                case "2" -> productInputHandler.handleUpdateProduct();
                case "3" -> productInputHandler.handleDisplayAllProducts();
                case "4" -> productInputHandler.handleSearchProduct();
                case "5" -> productInputHandler.handleDeleteProduct();
                // 6 exits the loop and returns to the main menu
                case "6" -> inProductMenu = false;
                default  -> System.out.println("Invalid option. Please enter a number between 1 and 6.");
            }
        }
    }

    // Shows customer menu, returns to main menu apon exit
    private void showCustomerMenu() {
        boolean inCustomerMenu = true;

        while (inCustomerMenu) {
            displayMenu("customer_menu.txt");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> customerInputHandler.handleAddCustomer();
                case "2" -> customerInputHandler.handleUpdateCustomer();
                case "3" -> customerInputHandler.handleDisplayAllCustomers();
                case "4" -> customerInputHandler.handleSearchCustomer();
                case "5" -> customerInputHandler.handleDeleteCustomer();
                case "6" -> inCustomerMenu = false;
                default  -> System.out.println("Invalid option. Please enter a number between 1 and 6.");
            }
        }
    }
}
