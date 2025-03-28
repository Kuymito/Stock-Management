package View;

import Model.Product;

import java.util.List;


public class ProductView {
    private int rowsPerPage = 5;

    public void displayProducts(List<Product> products, int page, int totalRecords) {
        System.out.println("\n===== Product List =====");
        if (products.isEmpty()) {
            System.out.println("No products found for this page.");
        } else {
            System.out.println("| ID    | Name            | Unit Price | Qty   | Import Date  |");
            System.out.println("|-------|-----------------|------------|-------|--------------|");
            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
        System.out.println("|-------|-----------------|------------|-------|--------------|");
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
        System.out.printf("Page: %d of %d                    TOTAL_RECORDS: %d%n", page, totalPages, totalRecords);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        if (rowsPerPage <= 0) {
            throw new IllegalArgumentException("Rows per page must be positive: " + rowsPerPage);
        }
        this.rowsPerPage = rowsPerPage;
    }

    public void displayProduct(Product product) {
        System.out.println("| ID    | Name            | Unit Price | Qty   | Import Date  |");
        System.out.println("|-------|-----------------|------------|-------|--------------|");
        System.out.println(product.toString());
        System.out.println("|-------|-----------------|------------|-------|--------------|");
    }
}