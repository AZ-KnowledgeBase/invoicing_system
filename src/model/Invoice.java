package model;
import java.time.LocalDate;
import java.util.ArrayList;


public class Invoice {
    private int invoiceNo;
    private LocalDate invoiceDate;
    private ArrayList<InvoiceEntry> entries; // List containing invoice entries
    private Customer customer; // Reference to customer object

    // Contructors=
    public Invoice(int invoiceNo) {
        this.invoiceNo = invoiceNo;
        this.entries = new ArrayList<>(); // Always initialize the entries list
    }

    public Invoice(int invoiceNo, LocalDate invoiceDate, ArrayList<InvoiceEntry> entries, Customer customer) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.entries = entries;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
