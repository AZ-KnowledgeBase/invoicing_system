package presentation;

import model.Customer;
import model.Invoice;
import model.InvoiceEntry;
import model.Product;
import service.SystemManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class InvoiceInputHandler {

    private final SystemManager systemManager = new SystemManager();
    private final Scanner scanner = new Scanner(System.in);

   // Guides the user through creating an invoice and checks stock before adding each product.
    void handleGenerateInvoice() {
        System.out.println("\n--- Generate Invoice ---");

        try {
            System.out.print("Enter Invoice Number : ");
            int invoiceNo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Customer ID    : ");
            int customerID = Integer.parseInt(scanner.nextLine().trim());

            Customer customer = systemManager.findCustomerById(customerID);
            // Stops early rather than generating an invoice with no valid owner
            if (customer == null) {
                System.out.println("No customer found with ID: " + customerID);
                return;
            }

            System.out.println("Customer : " + customer.getCustomerName());

            ArrayList<InvoiceEntry> entries = new ArrayList<>();
            boolean addingProducts = true;

            // Keeps looping so the user can add as many products as the invoice needs
            while (addingProducts) {
                System.out.print("\nEnter Product ID (or 0 to finish): ");
                int productID = Integer.parseInt(scanner.nextLine().trim());

                if (productID == 0) {
                    // Prevents submitting an empty invoice with no products
                    if (entries.isEmpty()) {
                        System.out.println("At least one product is required.");
                        continue;
                    }
                    addingProducts = false;
                    continue;
                }

                Product product = systemManager.findProductById(productID);
                if (product == null) {
                    System.out.println("No product found with ID: " + productID);
                    continue;
                }

                System.out.println("Product  : " + product.getProductName());
                System.out.println("In Stock : " + product.getQuantity());
                System.out.println("Price    : " + product.getSellingPrice());

                System.out.print("Enter Quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());

                // Checks stock before adding so the user is informed immediately rather than at confirmation
                if (quantity > product.getQuantity()) {
                    System.out.println("Insufficient stock. Available: " + product.getQuantity());
                    continue;
                }

                float unitPrice  = product.getSellingPrice();
                float lineTotal  = unitPrice * quantity;

                entries.add(new InvoiceEntry(productID, product.getProductName(), quantity, unitPrice, lineTotal));
                System.out.printf("Added: %s x%d = %.2f%n", product.getProductName(), quantity, lineTotal);
            }

            // Calculate subtotal by summing all entry line totals
            float subtotal = 0;
            for (InvoiceEntry e : entries) subtotal += e.getTotalPrice();

            System.out.print("\nEnter Discount % (or 0 for none): ");
            float discount = Float.parseFloat(scanner.nextLine().trim());

            // Applies discount as a percentage reduction on the subtotal
            float discountAmount = subtotal * (discount / 100);
            float totalPrice     = subtotal - discountAmount;

            printInvoiceSummary(entries, subtotal, discount, totalPrice);

            System.out.print("\nConfirm Invoice? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            // Uses equalsIgnoreCase so both "y" and "Y" are accepted
            if (confirm.equalsIgnoreCase("Y")) {
                Invoice invoice = new Invoice(
                        invoiceNo,
                        LocalDate.now(),
                        entries,
                        customer.getCustomerName(),
                        customerID,
                        discount,
                        totalPrice
                );

                boolean success = systemManager.addInvoice(invoice);
                System.out.println(success
                        ? "Invoice generated successfully!"
                        : "Failed to generate invoice. Please try again.");
            } else {
                System.out.println("Invoice cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid value entered. Please enter the correct numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    void handleViewByCustomer() {
        System.out.println("\n--- View Invoices by Customer ---");

        try {
            System.out.print("Enter Customer ID: ");
            int customerID = Integer.parseInt(scanner.nextLine().trim());

            ArrayList<Invoice> invoices = systemManager.getInvoicesByCustomer(customerID);

            if (invoices.isEmpty()) {
                System.out.println("No invoices found for Customer ID: " + customerID);
                return;
            }

            for (Invoice inv : invoices) printInvoice(inv);

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered. Please enter a numeric value.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Validates that the from date is not after the to date before querying
    void handleViewByDateRange() {
        System.out.println("\n--- View Invoices by Date Range ---");

        try {
            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            LocalDate from = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter End Date   (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(scanner.nextLine().trim());

            if (from.isAfter(to)) {
                System.out.println("Start date cannot be after end date.");
                return;
            }

            ArrayList<Invoice> invoices = systemManager.getInvoicesByDateRange(from, to);

            if (invoices.isEmpty()) {
                System.out.println("No invoices found between " + from + " and " + to);
                return;
            }

            for (Invoice inv : invoices) printInvoice(inv);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Reused at confirmation stage so the user sees a full breakdown before committing
    private void printInvoiceSummary(ArrayList<InvoiceEntry> entries, float subtotal,
                                     float discount, float totalPrice) {
        System.out.println("\n--- Invoice Summary ---");
        System.out.printf("%-20s %-8s %-12s %-10s%n", "Product", "Qty", "Unit Price", "Total");
        System.out.println("-".repeat(55));
        for (InvoiceEntry e : entries) {
            System.out.printf("%-20s %-8d %-12.2f %-10.2f%n",
                    e.getProductName(), e.getQuantitySold(), e.getUnitPrice(), e.getTotalPrice());
        }
        System.out.println("-".repeat(55));
        System.out.printf("Subtotal  : %.2f%n", subtotal);
        System.out.printf("Discount  : %.2f%%%n", discount);
        System.out.printf("Total     : %.2f%n", totalPrice);
    }

    // Reused across all view methods so invoice formatting stays consistent everywhere
    private void printInvoice(Invoice inv) {
        System.out.println("\n========================================");
        System.out.println("Invoice No   : " + inv.getInvoiceNo());
        System.out.println("Customer     : " + inv.getCustomerName());
        System.out.println("Date         : " + inv.getInvoiceDate());
        System.out.println("----------------------------------------");
        System.out.printf("%-20s %-8s %-12s %-10s%n", "Product", "Qty", "Unit Price", "Total");
        System.out.println("----------------------------------------");
        for (InvoiceEntry e : inv.getEntries()) {
            System.out.printf("%-20s %-8d %-12.2f %-10.2f%n",
                    e.getProductName(), e.getQuantitySold(), e.getUnitPrice(), e.getTotalPrice());
        }
        System.out.println("----------------------------------------");
        System.out.printf("Discount     : %.2f%%%n", inv.getDiscount());
        System.out.printf("Total Price  : %.2f%n", inv.getTotalPrice());
        System.out.println("========================================");
    }
}