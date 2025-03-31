package model;

import model.Product;

import java.util.List;

public interface ProductDAO {
    boolean insertProduct(Product product);
    boolean deleteProduct(int id);
    boolean updateProduct(Product product);
    Product getProductById(int id);
    List<Product> getProducts(int page, int rowsPerPage);
    List<Product> searchProductByName(String name);
    int getProductCount();
}
