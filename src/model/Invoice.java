package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Invoice {

    private int invoiceNo;
    private LocalDate invoiceDate;
    private ArrayList<InvoiceEntry> entries;
    private int customerID;
    private String customerName;                                                         
    private float discount;                   
    private float totalPrice;                 

    // Used when only the invoice number is known (e.g. when loading from DB)
    public Invoice(int invoiceNo) {
        this.invoiceNo = invoiceNo;
        this.entries = new ArrayList<>();     // Always initialise so entries is never null
    }

    // Used when generating a new invoice with all details known upfront
    public Invoice(int invoiceNo, LocalDate invoiceDate, ArrayList<InvoiceEntry> entries,
               String customerName, int customerID, float discount, float totalPrice) {
    this.invoiceNo    = invoiceNo;
    this.invoiceDate  = invoiceDate;
    this.entries      = entries;
    this.customerName = customerName;
    this.customerID   = customerID;
    this.discount     = discount;
    this.totalPrice   = totalPrice;
    }

    // Getters and Setters
    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public ArrayList<InvoiceEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<InvoiceEntry> entries) {
        this.entries = entries;
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

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}