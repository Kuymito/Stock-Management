import model.Product;
import model.ProductDAO;

import java.util.List;

public class ProductController {
    private ProductDAO productDAO;

    public ProductController() {
        productDAO = new ProductDAOImpl();
    }

    // Display all products
    public List<Product> displayProducts(int page, int rowsPerPage) {
        return productDAO.getProducts(page, rowsPerPage);
    }

    // Add new product
    public boolean insertProduct(Product product) {
        return productDAO.insertProduct(product);
    }

    // Get a product by ID
    public Product viewProduct(int id) {
        return productDAO.getProductById(id);
    }

    // Delete product by ID
    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }

    // Update product
    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    // Search products by name
    public List<Product> searchByName(String name) {
        return productDAO.searchProductByName(name);
    }

    // Get total number of products
    public int getTotalProductCount() {
        return productDAO.getProductCount();
    }
}
