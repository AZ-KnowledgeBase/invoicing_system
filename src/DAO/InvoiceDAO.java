package DAO;

import model.Invoice;
import model.InvoiceEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InvoiceDAO {

    private Connection getConnection() throws SQLException {
        String url =
                "jdbc:mysql://localhost:3306/invoicing_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }


    public boolean add(Invoice invoice) throws SQLException {
        String invoiceSql = """
            INSERT INTO invoices (invoiceNo, customerID, customerName, invoiceDate, discount, totalPrice)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        String entrySql = """
            INSERT INTO invoice_entries (invoiceNo, productID, productName, quantitySold, unitPrice, totalPrice)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        String stockCheckSql  = "SELECT quantity FROM products WHERE productID = ?";
        String stockUpdateSql = "UPDATE products SET quantity = quantity - ? WHERE productID = ?";

        try (Connection con = getConnection()) {

            // Disable auto-commit so all inserts and updates succeed together or all are rolled back
            con.setAutoCommit(false);

            try {
                // Insert invoice header
                try (PreparedStatement ps = con.prepareStatement(invoiceSql)) {
                    ps.setInt(1, invoice.getInvoiceNo());
                    ps.setInt(2, invoice.getCustomerID());
                    ps.setString(3, invoice.getCustomerName());
                    ps.setDate(4, Date.valueOf(invoice.getInvoiceDate()));
                    ps.setFloat(5, invoice.getDiscount());
                    ps.setFloat(6, invoice.getTotalPrice());
                    ps.executeUpdate();
                }

                for (InvoiceEntry entry : invoice.getEntries()) {

                    // Verify stock is still sufficient before inserting each entry
                    try (PreparedStatement stockCheck = con.prepareStatement(stockCheckSql)) {
                        stockCheck.setInt(1, entry.getProductID());
                        try (ResultSet rs = stockCheck.executeQuery()) {
                            if (!rs.next() || rs.getInt("quantity") < entry.getQuantitySold()) {
                                con.rollback();
                                throw new SQLException("Insufficient stock for: " + entry.getProductName());
                            }
                        }
                    }

                    // Insert the invoice entry line
                    try (PreparedStatement ps = con.prepareStatement(entrySql)) {
                        ps.setInt(1, invoice.getInvoiceNo());
                        ps.setInt(2, entry.getProductID());
                        ps.setString(3, entry.getProductName());
                        ps.setInt(4, entry.getQuantitySold());
                        ps.setFloat(5, entry.getUnitPrice());
                        ps.setFloat(6, entry.getTotalPrice());
                        ps.executeUpdate();
                    }

                    // Reduce the product stock by the quantity sold
                    try (PreparedStatement stockUpdate = con.prepareStatement(stockUpdateSql)) {
                        stockUpdate.setInt(1, entry.getQuantitySold());
                        stockUpdate.setInt(2, entry.getProductID());
                        stockUpdate.executeUpdate();
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                // Undo all changes if any step fails
                con.rollback();
                throw e;
            }
        }
    }

    // Find all invoices belonging to a specific customer
    public ArrayList<Invoice> findByCustomerId(int customerID) throws SQLException {
        String sql = """
            SELECT invoiceNo, customerID, customerName, invoiceDate, discount, totalPrice
            FROM invoices
            WHERE customerID = ?
            ORDER BY invoiceDate DESC
            """;

        ArrayList<Invoice> invoices = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = buildInvoice(rs);
                    invoice.setEntries(loadEntries(con, invoice.getInvoiceNo()));
                    invoices.add(invoice);
                }
            }
        }

        return invoices;
    }

    // Find all invoices within a given date range
    public ArrayList<Invoice> findByDateRange(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT invoiceNo, customerID, customerName, invoiceDate, discount, totalPrice
            FROM invoices
            WHERE invoiceDate BETWEEN ? AND ?
            ORDER BY invoiceDate DESC
            """;

        ArrayList<Invoice> invoices = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = buildInvoice(rs);
                    invoice.setEntries(loadEntries(con, invoice.getInvoiceNo()));
                    invoices.add(invoice);
                }
            }
        }

        return invoices;
    }

    // Creates an Invoice object from the current ResultSet row to avoid repeating mapping code.
    private Invoice buildInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice(rs.getInt("invoiceNo"));
        invoice.setCustomerID(rs.getInt("customerID"));
        invoice.setCustomerName(rs.getString("customerName"));
        invoice.setInvoiceDate(rs.getDate("invoiceDate").toLocalDate());
        invoice.setDiscount(rs.getFloat("discount"));
        invoice.setTotalPrice(rs.getFloat("totalPrice"));
        return invoice;
    }

    // Loads all line items for the given invoice using the current database connection.
    private ArrayList<InvoiceEntry> loadEntries(Connection con, int invoiceNo) throws SQLException {
        String sql = """
            SELECT productID, productName, quantitySold, unitPrice, totalPrice
            FROM invoice_entries
            WHERE invoiceNo = ?
            """;

        ArrayList<InvoiceEntry> entries = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceNo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(new InvoiceEntry(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getInt("quantitySold"),
                            rs.getFloat("unitPrice"),
                            rs.getFloat("totalPrice")
                    ));
                }
            }
        }

        return entries;
    }
}