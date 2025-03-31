package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDAO {

    @Override
    public boolean insertProduct(Product product) {
        String query = "INSERT INTO products (name, unit_price, stock_quantity, imported_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getUnitPrice());
            statement.setInt(3, product.getStockQuantity());
            statement.setDate(4, Date.valueOf(product.getImportedDate()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, unit_price = ?, stock_quantity = ?, imported_date = ? WHERE id = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getUnitPrice());
            statement.setInt(3, product.getStockQuantity());
            statement.setDate(4, Date.valueOf(product.getImportedDate()));
            statement.setInt(5, product.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getProducts(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products LIMIT ? OFFSET ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rowsPerPage);
            statement.setInt(2, (page - 1) * rowsPerPage);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> searchProductByName(String name) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + name + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public int getProductCount() {
        String query = "SELECT COUNT(*) FROM products";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setUnitPrice(rs.getDouble("unit_price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setImportedDate(rs.getDate("imported_date").toString());
        return product;
    }
}
