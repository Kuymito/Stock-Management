package Model;

import java.util.List;

public interface ProductDao {
    void addProduct(Product product);
    List<Product> getAllProducts();
    void tempProductList(Product product);
    List<Product> gettempProductList();
    void updateProduct(Product product);
}
