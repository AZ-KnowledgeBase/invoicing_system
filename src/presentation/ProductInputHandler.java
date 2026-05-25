package presentation;

import model.Product;
import service.SystemManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductInputHandler {

    private final SystemManager systemManager = new SystemManager();
    private final Scanner scanner = new Scanner(System.in);

    // Collects fields before creating product object
    void handleAddProduct() {
        System.out.println("\n--- Add New Product ---");

        try {
            System.out.print("Enter Product ID      : ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Product Name    : ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Description     : ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter Purchase Price  : ");
            float purchasePrice = Float.parseFloat(scanner.nextLine().trim());

            System.out.print("Enter Selling Price   : ");
            float sellingPrice = Float.parseFloat(scanner.nextLine().trim());

            System.out.print("Enter Quantity        : ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            Product product = new Product(id, name, description, purchasePrice, sellingPrice, quantity);
            boolean success = systemManager.addProduct(product);

            System.out.println(success
                    ? "Product added successfully!"
                    : "Failed to add product. Please try again.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid value entered. Please enter the correct numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Ovewrites fields from existing product based on ID
    void handleUpdateProduct() {
        System.out.println("\n--- Update Product ---");

        try {
            System.out.print("Enter Product ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Product existing = systemManager.findProductById(id);
            // Stops early rather than prompting for updates on a product that doesn't exist
            if (existing == null) {
                System.out.println("No product found with ID: " + id);
                return;
            }

            System.out.println("Current Name          : " + existing.getProductName());
            System.out.print("New Name (or press Enter to keep): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) existing.setProductName(name);

            System.out.println("Current Description   : " + existing.getDescription());
            System.out.print("New Description (or press Enter to keep): ");
            String description = scanner.nextLine().trim();
            if (!description.isEmpty()) existing.setDescription(description);

            System.out.println("Current Purchase Price: " + existing.getPurchasePrice());
            System.out.print("New Purchase Price (or press Enter to keep): ");
            String purchasePriceInput = scanner.nextLine().trim();
            if (!purchasePriceInput.isEmpty()) existing.setPurchasePrice(Float.parseFloat(purchasePriceInput));

            System.out.println("Current Selling Price : " + existing.getSellingPrice());
            System.out.print("New Selling Price (or press Enter to keep): ");
            String sellingPriceInput = scanner.nextLine().trim();
            if (!sellingPriceInput.isEmpty()) existing.setSellingPrice(Float.parseFloat(sellingPriceInput));

            System.out.println("Current Quantity      : " + existing.getQuantity());
            System.out.print("New Quantity (or press Enter to keep): ");
            String quantityInput = scanner.nextLine().trim();
            if (!quantityInput.isEmpty()) existing.setQuantity(Integer.parseInt(quantityInput));

            boolean success = systemManager.updateProduct(existing);
            System.out.println(success
                    ? "Product updated successfully!"
                    : "Failed to update product. Please try again.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid value entered. Please enter the correct numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    void handleDisplayAllProducts() {
        System.out.println("\n--- Product List ---");

        try {
            ArrayList<Product> products = systemManager.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products found in the system.");
                return;
            }

            // Print text in fixed-width columns so everything lines up nicely
            System.out.printf("%-5s %-20s %-40s %-10s %-10s %-8s%n",
                    "ID", "Name", "Description", "Buy Price", "Sell Price", "Qty");
            System.out.println("-".repeat(95));

            for (Product p : products) {
                System.out.printf("%-5d %-20s %-40s %-10.2f %-10.2f %-8d%n",
                        p.getProductID(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getPurchasePrice(),
                        p.getSellingPrice(),
                        p.getQuantity());
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    void handleSearchProduct() {
        System.out.println("\n--- Search Product ---");

        try {
            System.out.print("Enter Product ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Product p = systemManager.findProductById(id);

            if (p == null) {
                System.out.println("No product found with ID: " + id);
            } else {
                System.out.println("\n--- Product Found ---");
                System.out.println("ID            : " + p.getProductID());
                System.out.println("Name          : " + p.getProductName());
                System.out.println("Description   : " + p.getDescription());
                System.out.println("Purchase Price: " + p.getPurchasePrice());
                System.out.println("Selling Price : " + p.getSellingPrice());
                System.out.println("Quantity      : " + p.getQuantity());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Looks up by ID and confirms with user whether they wish to delete the product
    void handleDeleteProduct() {
        System.out.println("\n--- Delete Product ---");

        try {
            System.out.print("Enter Product ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Product existing = systemManager.findProductById(id);
            if (existing == null) {
                System.out.println("No product found with ID: " + id);
                return;
            }

            System.out.print("Are you sure you want to delete: "
                    + existing.getProductName() + "? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            // Uses equalsIgnoreCase so both "y" and "Y" are accepted
            if (confirm.equalsIgnoreCase("Y")) {
                boolean success = systemManager.deleteProduct(id);
                System.out.println(success
                        ? "Product deleted successfully!"
                        : "Failed to delete product. Please try again.");
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