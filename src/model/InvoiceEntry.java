package model;

public class InvoiceEntry {

    private int productID;
    private String productName;   
    private int quantitySold;   
    private float unitPrice;    
    private float totalPrice;   

    // When building an invoice entry from user input
    public InvoiceEntry(int productID, String productName, int quantitySold, float unitPrice, float totalPrice) {
        this.productID    = productID;
        this.productName  = productName;
        this.quantitySold = quantitySold;
        this.unitPrice    = unitPrice;
        this.totalPrice   = totalPrice;
    }

    // Getters and Setters
    public int getProductID() {
    return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}