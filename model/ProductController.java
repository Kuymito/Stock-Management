package model;

import java.util.List;

public class ProductController {
    private ProductDAO productDAO;

    public ProductController() {
        productDAO = new ProductDaoImpl();
    }

    public List<Product> displayProducts(int page, int rowsPerPage) {
        return productDAO.getProducts(page, rowsPerPage);
    }


    public boolean insertProduct(Product product) {
        return productDAO.insertProduct(product);
    }


    public Product viewProduct(int id) {
        return productDAO.getProductById(id);
    }


    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }


    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }


    public List<Product> searchByName(String name) {
        return productDAO.searchProductByName(name);
    }


    public int getTotalProductCount() {
        return productDAO.getProductCount();
    }

}
