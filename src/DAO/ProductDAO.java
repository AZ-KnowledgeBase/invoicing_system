package DAO;

import model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {

    // Reuses the same DB connection details as CustomerDAO
    private Connection getConnection() throws SQLException {
        String url =
                "jdbc:mysql://localhost:3306/invoicing_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    // Add new product record to the table
    public boolean add(Product product) throws SQLException {
        String sql = """
            INSERT INTO products (productID, productName, description, purchasePrice, sellingPrice, quantity)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getProductID());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setFloat(4, product.getPurchasePrice());
            ps.setFloat(5, product.getSellingPrice());
            ps.setInt(6, product.getQuantity());

            return ps.executeUpdate() == 1;
        }
    }

    // Update product information on table
    public boolean update(Product product) throws SQLException {
        String sql = """
            UPDATE products
            SET productName = ?, description = ?, purchasePrice = ?, sellingPrice = ?, quantity = ?
            WHERE productID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setFloat(3, product.getPurchasePrice());
            ps.setFloat(4, product.getSellingPrice());
            ps.setInt(5, product.getQuantity());
            ps.setInt(6, product.getProductID());

            return ps.executeUpdate() == 1;
        }
    }

    // Delete product from table
    public boolean delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE productID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() == 1;
        }
    }

    // Find specific product by ID
    public Product findById(int productId) throws SQLException {
        String sql = """
            SELECT productID, productName, description, purchasePrice, sellingPrice, quantity
            FROM products
            WHERE productID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Product p = new Product(rs.getInt("productID"), rs.getString("productName"));
                p.setDescription(rs.getString("description"));
                p.setPurchasePrice(rs.getFloat("purchasePrice"));
                p.setSellingPrice(rs.getFloat("sellingPrice"));
                p.setQuantity(rs.getInt("quantity"));
                return p;
            }
        }
    }

    // Retrieve all products ordered by ID
    public ArrayList<Product> getAll() throws SQLException {
        String sql = """
            SELECT productID, productName, description, purchasePrice, sellingPrice, quantity
            FROM products
            ORDER BY productID
            """;

        ArrayList<Product> products = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(rs.getInt("productID"), rs.getString("productName"));
                p.setDescription(rs.getString("description"));
                p.setPurchasePrice(rs.getFloat("purchasePrice"));
                p.setSellingPrice(rs.getFloat("sellingPrice"));
                p.setQuantity(rs.getInt("quantity"));
                products.add(p);
            }
        }

        return products;
    }
}