package View;

import Model.Product;

import java.util.List;

public class ProductView {
    public ProductView() {}
    public void displayProduct(List<Product> products) {
        System.out.println("Product List:");
        for(Product product: products) {
            System.out.println(product.getId() + "-" + product.getName() + "-" + product.getUnitPrice() + "-" + product.getStockQuantity() + "-" + product.getImportedDate());
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
