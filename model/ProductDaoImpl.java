package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDAO {

    @Override
    public void addProduct(Product product) {
        String query = "insert into products (name, unit_price, stock_quantity, imported_date) values(?,?,?,?)";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQuantity());
            ps.setDate(4, Date.valueOf(product.getImportedDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product to database.", e);
        }
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
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