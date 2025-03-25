package Model;

import java.util.List;

public interface ProductDao {
    void addProduct(Product product);
    List<Product> getProducts();
}
