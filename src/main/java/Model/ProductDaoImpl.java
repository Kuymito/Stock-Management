package Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDaoImpl implements ProductDao {
    private static final Logger LOGGER = Logger.getLogger(ProductDaoImpl.class.getName());

    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO products (name, unit_price, stock_quantity, imported_date) VALUES (?, ?, ?, ?)";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQuantity());
            ps.setDate(4, Date.valueOf(product.getImportedDate()));
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getInt(1));
                        LOGGER.info("Product added with ID: " + product.getId());
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to add product: " + e.getMessage());
            throw new RuntimeException("Failed to add product to database: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE products SET name=?, unit_price=?, stock_quantity=?, imported_date=? WHERE id=?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQuantity());
            ps.setDate(4, Date.valueOf(product.getImportedDate()));
            ps.setInt(5, product.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.warning("No product found with ID: " + product.getId());
                throw new RuntimeException("No product found with ID: " + product.getId());
            }
            LOGGER.info("Product updated successfully: ID " + product.getId());
        } catch (SQLException e) {
            LOGGER.severe("Failed to update product: " + e.getMessage());
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                LOGGER.warning("Product with ID " + id + " not found.");
                throw new RuntimeException("Product with ID " + id + " not found.");
            }
            LOGGER.info("Product deleted successfully: ID " + id);
        } catch (SQLException e) {
            LOGGER.severe("Failed to delete product: " + e.getMessage());
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAllProducts(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * rowsPerPage;
        String query = "SELECT * FROM products ORDER BY id LIMIT ? OFFSET ?";

        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, rowsPerPage);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double unitPrice = rs.getDouble("unit_price");
                    int stockQuantity = rs.getInt("stock_quantity");
                    Date importedDate = rs.getDate("imported_date");
                    LocalDate localDate = (importedDate != null) ? importedDate.toLocalDate() : null;
                    Product product = new Product(id, name, unitPrice, stockQuantity, localDate);
                    products.add(product);
                }
            }
            LOGGER.info("Fetched " + products.size() + " products for page " + page);
        } catch (SQLException e) {
            LOGGER.severe("Error fetching products: " + e.getMessage());
            throw new RuntimeException("Failed to fetch products: " + e.getMessage(), e);
        }
        return products;
    }

    @Override
    public int getTotalProductCount() {
        String query = "SELECT COUNT(*) FROM products";
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int count = rs.getInt(1);
                LOGGER.info("Total product count: " + count);
                return count;
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.severe("Error fetching product count: " + e.getMessage());
            throw new RuntimeException("Failed to fetch product count: " + e.getMessage(), e);
        }
    }

    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    double unitPrice = rs.getDouble("unit_price");
                    int stockQuantity = rs.getInt("stock_quantity");
                    Date importedDate = rs.getDate("imported_date");
                    LocalDate localDate = (importedDate != null) ? importedDate.toLocalDate() : null;
                    return new Product(id, name, unitPrice, stockQuantity, localDate);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching product by ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch product by ID: " + e.getMessage(), e);
        }
    }
}