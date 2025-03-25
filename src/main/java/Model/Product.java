package Model;

import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int stockQuantity;
    private LocalDate importedDate;

    public Product(int id, String name, double unitPrice, int stockQuantity, LocalDate importedDate) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.importedDate = importedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDate getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(LocalDate importedDate) {
        this.importedDate = importedDate;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-15s | %-10.2f | %-5d | %-12s |",
                id, name, unitPrice, stockQuantity, importedDate);
    }
}
