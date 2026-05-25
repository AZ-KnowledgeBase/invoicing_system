package service;

import DAO.CustomerDAO;
import DAO.ProductDAO;
import model.Customer;
import model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class SystemManager {

    // Customer CRUD operations
    private final CustomerDAO customerDAO = new CustomerDAO();

    public boolean addCustomer(Customer customer) throws SQLException {
        return customerDAO.add(customer);
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        return customerDAO.update(customer);
    }

    public boolean deleteCustomer(int customerId) throws SQLException {
        return customerDAO.delete(customerId);
    }

    public Customer findCustomerById(int customerId) throws SQLException {
        return customerDAO.findById(customerId);
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAll();
    }

    // Product CRUD operations
    private final ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product product) throws SQLException {
        return productDAO.add(product);
    }

    public boolean updateProduct(Product product) throws SQLException {
        return productDAO.update(product);
    }

    public boolean deleteProduct(int productId) throws SQLException {
        return productDAO.delete(productId);
    }

    public Product findProductById(int productId) throws SQLException {
        return productDAO.findById(productId);
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        return productDAO.getAll();
    }
}