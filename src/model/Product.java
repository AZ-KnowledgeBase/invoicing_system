package model;

public class Product {
    private int productID; 
    private String productName;
    private String description;
    private float purchasePrice;
    private float sellingPrice; 
    private int quantity;

    // Constructor 1: minimal (e.g., when you only know the ID)
    public Product(int productID, String productName) {
        
        this.productID = productID;
        this.productName = productName;
    }

     // Constructor 2: full parameterized constructor
    public Product(int productID, String productName, String description,
                   float purchasePrice, float sellingPrice, int quantity) { 
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
