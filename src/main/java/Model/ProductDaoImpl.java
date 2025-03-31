package Model;

import View.ProductView;

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

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    ProductView productView = new ProductView();
    private List<Product> tempProducts = new ArrayList<>();
    private List<Product> tempUpdateProducts = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    @Override
    public void addProduct(List<Product> products) {
        String query = "INSERT INTO products (id, name, unit_price, stock_quantity, imported_date) VALUES (?,?,?,?,?)";

        try (Connection con = Connect.getConnection()) {
            con.setAutoCommit(false);

            try {

                int maxId;
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) FROM products")) {
                    rs.next();
                    maxId = rs.getInt(1);
                }

                int nextId = maxId + 1;
                for (Product p : products) {
                    p.setId(nextId++);
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
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
                    System.out.println(GREEN + "Inserted " + results.length + " products. New IDs: "
                            + (maxId + 1) + "-" + (maxId + products.size()) + RESET);
                }
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(RED + "Transaction failed" + RESET, e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Connection failed" + RESET, e);
        }
    }

    public void tempProductList(Product product) {
        tempProducts.add(product);
        System.out.println(GREEN + "Product temporarily added: " + product.getName() + " " + product.getUnitPrice() + " "
                + product.getStockQuantity() + " " + product.getImportedDate() + RESET);
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

            System.out.println(GREEN + "Fetching all products..." + RESET);

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
            System.err.println(RED + "Error while fetching products: " + e.getMessage() + RESET);
            throw new RuntimeException(RED + "Failed to fetch products from the database." + RESET, e);
        }

        System.out.println(GREEN + "Total products fetched: " + products.size() + RESET);
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
            throw new RuntimeException(RED + "Failed to get row in database." + RESET, e);
        }
        return row;
    }

    public void setRow(int row){
        String query = "update setrow set row_set = ?";
        try(Connection con = Connect.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, row);
            ps.executeUpdate();
            System.out.println(GREEN + "Row setting updated successfully." + RESET);
        }catch (SQLException e){
            throw new RuntimeException(RED + "Failed to update row in database." + RESET, e);
        }
    }

    public List<Product> getProductByName(String name) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name ILIKE ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + name + "%");
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
            throw new RuntimeException(RED + "Failed to search products by name." + RESET, e);
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

                System.out.println(GREEN + "Updated " + updateCounts.length + " product(s) successfully." + RESET);

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(RED + "Failed to update products. Transaction rolled back." + RESET, e);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to update products in database." + RESET, e);
        }
    }

    @Override
    public void addTempUpdateProduct(Product product) {
        boolean existsInTemp = false;
        for (Product p : tempUpdateProducts) {
            if (p.getId() == product.getId()) {
                p.setId(product.getId());
                p.setName(product.getName());
                p.setUnitPrice(product.getUnitPrice());
                p.setStockQuantity(product.getStockQuantity());
                p.setImportedDate(product.getImportedDate());
                existsInTemp = true;
                break;
            }
        }
        if (!existsInTemp) {
            Product dbProduct = getProductById(product.getId());
            if (dbProduct != null) {
                tempUpdateProducts.add(product);
            } else {
                System.out.println(RED + "Product with ID " + product.getId() + " does not exist in database." + RESET);
                return;
            }
        }

        System.out.println(GREEN + "Product update temporarily stored: " +
                product.getId() + " - " + product.getName() + RESET);
    }

    @Override
    public List<Product> getTempUpdateProductList() {
        return new ArrayList<>(tempUpdateProducts);
    }

    @Override
    public void clearTempUpdateProducts() {
        tempUpdateProducts.clear();
    }

    @Override
    public void clearTempProducts() {
        tempProducts.clear();
    }

    @Override
    public void deleteProductById(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        List<Product> products = new ArrayList<>();
        Product productToDelete = getProductById(id);
        products.add(productToDelete);
        if (productToDelete == null) {
            System.out.println(RED + "Product with ID " + id + " not found." + RESET);
            return;
        }
        System.out.println("Are you sure you want to delete this product?");
        productView.display1Product(products);
        System.out.print("Type 'YES' to confirm: ");
        String answer = sc.nextLine().trim();

        if (!answer.equalsIgnoreCase("YES")) {
            System.out.println(RED + "Delete operation cancelled." + RESET);
            return;
        }

        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            con.setAutoCommit(false);
            try {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();
                con.commit();

                if (rowsAffected > 0) {
                    System.out.println(GREEN + "Successfully deleted product with ID: " + id + RESET);
                } else {
                    System.out.println(RED + "No product was deleted." + RESET);
                }
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(RED + "Failed to delete product. Transaction rolled back." + RESET, e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to delete product from database." + RESET, e);
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
            throw new RuntimeException(RED + "Failed to search product by ID." + RESET, e);
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
            System.out.println(GREEN + "Backup completed successfully. File saved as: " + filePath + RESET);
        } catch (IOException e) {
            System.err.println(RED + "Error occurred during backup: " + e.getMessage() + RESET);
        }
    }

    public void deleteAllProducts() {
        String query = "TRUNCATE TABLE products RESTART IDENTITY;";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            int rowsDeleted = ps.executeUpdate();
            System.out.println(GREEN + rowsDeleted + " row(s) deleted." + RESET);
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to delete existing data." + RESET, e);
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
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                double price = Double.parseDouble(fields[2]);
                int quantity = Integer.parseInt(fields[3]);
                LocalDate importedDate = LocalDate.parse(fields[4], formatter);

                products.add(new Product(id, name, price, quantity, importedDate));
            }
            System.out.println(GREEN + "Data successfully read from CSV: " + products.size() + " products." + RESET);
        } catch (IOException e) {
            System.err.println(RED + "Error reading CSV file: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.err.println(RED + "Error processing data: " + e.getMessage() + RESET);
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
                System.out.println(GREEN + "Inserted " + results.length + " products successfully." + RESET);

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(RED + "Failed to add products to database. Transaction rolled back." + RESET, e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to add product to database." + RESET, e);
        }
    }

    public int getMaxProductId() {
        String query = "SELECT MAX(id) FROM products";
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to get max product ID" + RESET, e);
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
            System.out.println(GREEN + "Table truncated successfully." + RESET);
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to truncate table" + RESET, e);
        }
    }

    @Override
    public void resetSequenceTo(int value) {
        String query = "ALTER SEQUENCE products_id_seq RESTART WITH " + value;
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute(query);
            System.out.println(GREEN + "Sequence reset to " + value + " successfully." + RESET);
        } catch (SQLException e) {
            throw new RuntimeException(RED + "Failed to reset sequence" + RESET, e);
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
            throw new RuntimeException(RED + "Failed to get sequence value" + RESET, e);
        }
    }
}