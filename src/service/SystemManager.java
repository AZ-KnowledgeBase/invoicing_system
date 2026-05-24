package service;

import DAO.CustomerDAO;
import model.Customer;

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
}