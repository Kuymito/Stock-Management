package Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public void addProduct(Product product) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = Connect.getConnection();
            con.setAutoCommit(true);
            String query = "INSERT INTO products (name, unit_price, stock_quantity, imported_date) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQuantity());
            ps.setDate(4, Date.valueOf(product.getImportedDate()));
            int rowsInserted = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product to database.", e);
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
                System.out.println("No product found with ID: " + product.getId());
            } else {
                System.out.println("Product updated successfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product.", e);
        }
    }

    @Override
    public void deleteProduct(int id) {
        Connection con = null;
        try {
            con = Connect.getConnection();
            con.setAutoCommit(true);

            String query = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Product deleted successfully.");
                } else {
                    System.out.println("Product ID not found.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product.", e);
        }
    }


    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Fetching all products...");

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double unitPrice = rs.getDouble(3);
                int stockQuantity = rs.getInt(4);
                Date importedDate = rs.getDate(5);
                LocalDate localDate = (importedDate != null) ? importedDate.toLocalDate() : null;
                Product product = new Product(id, name, unitPrice, stockQuantity, localDate);
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching products: " + e.getMessage());
            throw new RuntimeException("Failed to fetch products from the database.", e);
        }

        System.out.println("Total products fetched: " + products.size());
        return products;
    }

}