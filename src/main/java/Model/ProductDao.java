package Model;

import java.util.List;

public interface ProductDao {
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(int id);
    List<Product> getAllProducts(int page, int rowsPerPage);
    int getTotalProductCount();
    Product getProductById(int id);
}