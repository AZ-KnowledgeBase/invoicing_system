package model;

public class InvoiceEntry {

    private Product product; // Reference to product object
    private float totalPrice;
    private float unitPrice; 
    private float discount;
    private float quantitySold;

    // Constructor Invoice Entry
    public InvoiceEntry(Product product, float totalPrice, float unitPrice, float discount, float quantitySold) {
    this.product = product;
    this.totalPrice = totalPrice;
    this.unitPrice = unitPrice;
    this.discount = discount;
    this.quantitySold = quantitySold;
}

    // Getters and Setters
    public float getTotalPrice() {
    return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(float quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Product getProduct() {
    return product;
}

    public void setProduct(Product product) {
        this.product = product;
    }
}
