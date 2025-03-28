package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductDaoImpl implements ProductDao {
    private List<Product> tempProducts = new ArrayList<>();
    private List<Product> tempUpdateProducts = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    @Override
    public void addProduct(List<Product> products) {
        String query = "insert into products (name, unit_price, stock_quantity, imported_date) values(?,?,?,?)";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            con.setAutoCommit(false);

            try {
                for (Product p : products) {
                    ps.setString(1, p.getName());
                    ps.setDouble(2, p.getUnitPrice());
                    ps.setInt(3, p.getStockQuantity());
                    ps.setDate(4, Date.valueOf(p.getImportedDate()));
                    ps.addBatch();  // Add each product to the batch
                }

                int[] results = ps.executeBatch();
                con.commit();

                System.out.println("Inserted " + results.length + " products successfully.");

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Failed to add products to database. Transaction rolled back.", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product to database.", e);
        }
    }

    public void tempProductList(Product product) {
        tempProducts.add(product);
        System.out.println("Product temporarily added: " + product.getName() + " " + product.getUnitPrice() + " " + product.getStockQuantity() + " " + product.getImportedDate());
    }

    public List<Product> gettempProductList(){
        return tempProducts;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products ORDER BY id";

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

    public int getSetRow(){
        String query = "select * from setrow";
        int row = 0;
        try(Connection con = Connect.getConnection()){
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                row = rs.getInt(1);
                break;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to get row in database.", e);
        }
        return row;
    }

    public void setRow(int row){
        String query = "update setrow set row_set = ?";
        try(Connection con = Connect.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, row);
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Failed to update row in database.", e);
        }
    }



    public List<Product> getProductByName(String name) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name ILIKE ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + name + "%"); // Add wildcards
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name1 = rs.getString(2);
                double unitPrice = rs.getDouble(3);
                int stockQuantity = rs.getInt(4);
                Date importedDate = rs.getDate(5);

                LocalDate localDate = (importedDate != null) ? importedDate.toLocalDate() : null;

                Product product = new Product(id, name1, unitPrice, stockQuantity, localDate);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search products by name.", e);
        }
        return products;
    }
    @Override
    public void updateProduct(List<Product> products) {
        String query = "UPDATE products SET name = ?, unit_price = ?, stock_quantity = ?, imported_date = ? WHERE id = ?";

        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            con.setAutoCommit(false);
            try {
                for (Product product : products) {
                    ps.setString(1, product.getName());
                    ps.setDouble(2, product.getUnitPrice());
                    ps.setInt(3, product.getStockQuantity());
                    ps.setDate(4, Date.valueOf(product.getImportedDate()));
                    ps.setInt(5, product.getId());
                    ps.addBatch();
                }

                int[] updateCounts = ps.executeBatch();
                con.commit();

                System.out.println("Updated " + updateCounts.length + " product(s) successfully.");

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Failed to update products. Transaction rolled back.", e);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update products in database.", e);
        }
    }

    @Override
    public void addTempUpdateProduct(Product product) {
        // Check if product already exists in temp updates
        boolean existsInTemp = false;
        for (Product p : tempUpdateProducts) {
            if (p.getId() == product.getId()) {
                // Update existing temp product
                p.setName(product.getName());
                p.setUnitPrice(product.getUnitPrice());
                p.setStockQuantity(product.getStockQuantity());
                p.setImportedDate(product.getImportedDate());
                existsInTemp = true;
                break;
            }
        }

        // If not in temp updates, check if it exists in database
        if (!existsInTemp) {
            Product dbProduct = getProductById(product.getId());
            if (dbProduct != null) {
                // Add to temp updates if it exists in DB
                tempUpdateProducts.add(product);
            } else {
                System.out.println("Product with ID " + product.getId() + " does not exist in database.");
                return;
            }
        }

        System.out.println("Product update temporarily stored: " +
                product.getId() + " - " + product.getName());
    }

    @Override
    public List<Product> getTempUpdateProductList() {
        return new ArrayList<>(tempUpdateProducts);
    }

    @Override
    public void clearTempUpdateProducts() {
        tempUpdateProducts.clear();
        System.out.println("Temporary update list cleared.");
    }

    @Override
    public void clearTempProducts() {
        tempProducts.clear();
        System.out.println("Pending inserts cleared.");
    }

    @Override
    public void deleteProductById(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        try(Connection con = Connect.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            System.out.println("Are you sure to delete product with ID: " + getProductById(id));
            String answer = sc.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                ps.executeUpdate();
                System.out.println("Product deleted with ID: " + id);
            }else{
                System.out.println("Delete product failed.");
                return;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to delete product by ID.", e);
        }
    }

    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id1 = rs.getInt(1);
                String name1 = rs.getString(2);
                double unitPrice = rs.getDouble(3);
                int stockQuantity = rs.getInt(4);
                Date importedDate = rs.getDate(5);

                LocalDate localDate = (importedDate != null) ? importedDate.toLocalDate() : null;

                return new Product(id1, name1, unitPrice, stockQuantity, localDate);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search product by ID.", e);  // Fixed error message
        }
    }

    @Override
    public void backUpProductToCSV(List<Product> products,String filename) {
        String defaultPath = "D:\\CSV\\";
        LocalDate localDate = LocalDate.now();
        String filePath = defaultPath + filename +"-"+ localDate +".csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Name,Price,Quantity,ImportDate\n");
            for (Product product : products) {
                writer.write(product.getId() + "," +
                        product.getName() + "," +
                        product.getUnitPrice() + "," +
                        product.getStockQuantity() + "," +
                        product.getImportedDate() + "\n");
            }
            System.out.println("Backup completed successfully. File saved as: " + filePath);
        } catch (IOException e) {
            System.err.println("Error occurred during backup: " + e.getMessage());
        }
    }

    public void deleteAllProducts() {
        String query = "TRUNCATE TABLE products RESTART IDENTITY;";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            int rowsDeleted = ps.executeUpdate();
            System.out.println(rowsDeleted + " row(s) deleted.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete existing data.", e);
        }

    }


    public List<Product> readCSV(String filePath) {
        List<Product> products = new ArrayList<>();
        String defaultPath = "D:\\CSV\\";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(defaultPath + filePath + ".csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(","); // Split by comma
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                double price = Double.parseDouble(fields[2]);
                int quantity = Integer.parseInt(fields[3]);
                LocalDate importedDate = LocalDate.parse(fields[4], formatter); // Use formatter here

                products.add(new Product(id, name, price, quantity, importedDate));
            }
            System.out.println("Data successfully read from CSV: " + products.size() + " products.");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
        }
        return products;
    }

    public void insertCSVToDB(List<Product> products) {
        String query = "INSERT INTO products VALUES (?,?,?,?,?)";
        try(Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            con.setAutoCommit(false);

            try {
                for (Product p : products) {
                    ps.setInt(1, p.getId());
                    ps.setString(2, p.getName());
                    ps.setDouble(3, p.getUnitPrice());
                    ps.setInt(4, p.getStockQuantity());
                    ps.setDate(5, Date.valueOf(p.getImportedDate()));
                    ps.addBatch();
                }
                int[] results = ps.executeBatch();
                con.commit();
                System.out.println("Inserted " + results.length + " products successfully.");

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Failed to add products to database. Transaction rolled back.", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product to database.", e);
        }

    }
    public int getMaxProductId() {
        String query = "SELECT MAX(id) FROM products";
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get max product ID", e);
        }
    }


    @Override
    public void truncateTable(boolean restartIdentity) {
        String query = restartIdentity ?
                "TRUNCATE TABLE products RESTART IDENTITY" :
                "TRUNCATE TABLE products";

        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to truncate table", e);
        }
    }

    @Override
    public void resetSequenceTo(int value) {
        String query = "ALTER SEQUENCE products_id_seq RESTART WITH " + value;
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset sequence", e);
        }
    }

    @Override
    public int getCurrentSequenceValue() {
        String query = "SELECT last_value FROM products_id_seq";
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get sequence value", e);
        }
    }

}