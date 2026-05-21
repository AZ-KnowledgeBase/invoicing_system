package model;
import java.time.LocalDate;

public class Customer {
    private int customerID;
    private String customerName;
    private String email;
    private String contact;
    private LocalDate dob;
    private String gender;


     // Constructor 1: minimal (e.g., when you only know the ID)
    public Customer(int customerID) {
        this.customerID = customerID;
    }

    // Constructor 2: full parameterized constructor
    public Customer(int customerID, String customerName, String email,
                    String contact, LocalDate dob, String gender) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.email = email;
        this.contact = contact;
        this.dob = dob;
        this.gender = gender;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
