package model;

public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int stockQuantity;
    private String importedDate;


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
    public String getImportedDate() {
        return importedDate;
    }
    public void setImportedDate(String importedDate) {
        this.importedDate = importedDate;
    }

}
