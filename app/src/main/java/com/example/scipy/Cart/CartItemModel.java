package com.example.scipy.Cart;

public class CartItemModel {
    private int productId;
    private String productName;
    private int productPrice;
    private int productImageId;
    private int quantity;

    public CartItemModel(int productId, String productName, int productPrice, int productImageId, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageId = productImageId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getProductPrice() { return productPrice; }
    public int getProductImageId() { return productImageId; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
