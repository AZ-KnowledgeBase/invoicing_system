package DAO;

import model.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    // DB connection helper method
    private Connection getConnection() throws SQLException {
        String url =
                "jdbc:mysql://localhost:3306/invoicing_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = ""; // put your MySQL password here
        return DriverManager.getConnection(url, user, password);
    }

    // Add new customer record to the table
    public boolean add(Customer customer) throws SQLException {
        String sql = """
            INSERT INTO customers (customerID, customerName, email, contact, address, dob, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customer.getCustomerID());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getContact()); 
            ps.setString(5, customer.getAddress());

            if (customer.getDob() != null) {
                ps.setDate(6, Date.valueOf(customer.getDob()));
            } else {
                ps.setDate(6, null);
            }

            ps.setString(7, customer.getGender());

            return ps.executeUpdate() == 1;
        }
    }

    // Update customer information on table
    public boolean update(Customer customer) throws SQLException {
        String sql = """
            UPDATE customers
            SET customerName = ?, email = ?, contact = ?, address = ?, dob = ?, gender = ?
            WHERE customerID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getContact());
            ps.setString(4, customer.getAddress());

            if (customer.getDob() != null) {
                ps.setDate(5, Date.valueOf(customer.getDob()));
            } else {
                ps.setDate(5, null);
            }

            ps.setString(6, customer.getGender());
            ps.setInt(7, customer.getCustomerID());

            return ps.executeUpdate() == 1;
        }
    }

    // Delete customer from table
    public boolean delete(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customerID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            return ps.executeUpdate() == 1;
        }
    }

    // Find specific customers by ID
    public Customer findById(int customerId) throws SQLException {
        String sql = """
            SELECT customerID, customerName, email, contact, address, dob, gender
            FROM customers
            WHERE customerID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Customer c = new Customer(rs.getInt("customerID"));
                c.setCustomerName(rs.getString("customerName"));
                c.setEmail(rs.getString("email"));
                c.setContact(rs.getString("contact"));
                c.setAddress(rs.getString("address")); 

                Date dobSql = rs.getDate("dob");
                if (dobSql != null) c.setDob(dobSql.toLocalDate());

                c.setGender(rs.getString("gender"));
                return c;
            }
        }
    }

  // Display all customers
    public ArrayList<Customer> getAll() throws SQLException {
        String sql = """
            SELECT customerID, customerName, email, contact, address, dob, gender
            FROM customers
            ORDER BY customerID
            """;

        ArrayList<Customer> customerList = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(rs.getInt("customerID"));
                c.setCustomerName(rs.getString("customerName"));
                c.setEmail(rs.getString("email"));
                c.setContact(rs.getString("contact"));
                c.setAddress(rs.getString("address")); 

                Date dobSql = rs.getDate("dob");
                if (dobSql != null) c.setDob(dobSql.toLocalDate());

                c.setGender(rs.getString("gender"));

                customerList.add(c);
            }
        }

        return customerList;
    }
}