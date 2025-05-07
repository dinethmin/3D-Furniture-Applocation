package com.example.javafx.model;  // change this if your package is different

public class FurnitureItem {
    private String type;
    private double x;
    private double y;
    private double width;
    private double height;

    public FurnitureItem(String type, double x, double y, double width, double height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters
    public String getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // Setters (if needed)
    public void setType(String type) { this.type = type; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }

    @Override
    public String toString() {
        return type + " at (" + x + "," + y + ") size: " + width + "x" + height;
    }
}
