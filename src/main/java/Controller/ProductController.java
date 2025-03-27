package Controller;

import Model.Product;
import Model.ProductDao;
import Model.ProductDaoImpl;
import View.ProductView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProductController {
    private final ProductDao productDao;
    private final ProductView productView;

    public ProductController(ProductDao productDao, ProductView productView) {
        this.productDao = productDao;
        this.productView = productView;
    }

    public void addProduct(String name, double price, int quantity, LocalDate date) {
        productDao.addProduct(new Product(name,price,quantity,date));
    }

    public void updateProduct(int id, String name, double price, int quantity, LocalDate date) {
        productDao.updateProduct(new Product(id, name, price, quantity, date));
    }

    public void deleteProduct(int id) {
        productDao.deleteProduct(id);
    }

    public void showAllProducts() {
        List<Product> products = productDao.getAllProducts();
        productView.displayProduct(products);
    }
}
