package Controller;

import Model.Product;
import Model.ProductDao;
import View.ProductView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private final ProductDao productDao;
    private final ProductView productView;

    public ProductController(ProductDao productDao, ProductView productView) {
        this.productDao = productDao;
        this.productView = productView;
    }

    public void addProductToDB(List<Product> productList) {
        productDao.addProduct(productList);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }
//    public void showAllProducts() {
//        List<Product> products = productDao.getAllProducts();
//        productView.displayProduct(products);
//    }
public ProductView getView() {
    return this.productView;
}

    public void addTempProductList(String name, double price, int quantity, LocalDate date) {
        productDao.tempProductList(new Product(name,price,quantity,date));
    }

    public void showTempProductList() {
        List<Product> tempProducts = productDao.gettempProductList();
        productView.displayTempProducts(tempProducts);
    }
    public List<Product> getTempProductList() {
        return productDao.gettempProductList();
    }

    public void setRow(int row){
        productDao.setRow(row);
    }

    public int getRow(){
        return productDao.getSetRow();
    }

    public void getProductById(int id) {
        List<Product> productList = new ArrayList<>();
        Product product = productDao.getProductById(id);
        productList.add(product);
        if(product == null) {
            System.out.println("Product not found");
            return;
        }
        productView.display1Product(productList);
    }

    public List<Product> fetchProductByName(String name) {
        List<Product> product = productDao.getProductByName(name);
        if (product == null) {
            return null;
        }
        return product;
    }

    public Product fetchProductById(int id) {
        Product product = productDao.getProductById(id);
        if (product == null) {
            return null;
        }
        return product;
    }

    public void displayProductToValidate(int id) {
        List<Product> productlist = new ArrayList<>();
        Product product = fetchProductById(id);
        productlist.add(product);
        if (product != null) {
            productView.display1Product(productlist);
            System.out.println("You can now edit this product.");
        } else {
            System.out.println("No product found. Please check the ID and try again.");
        }
    }

    public void updateProduct(Product product) {
        productDao.addTempUpdateProduct(product);

        List<Product> tempUpdates = productDao.getTempUpdateProductList();
        productView.displayTempProducts(tempUpdates);
    }

    public void commitUpdates() {
        List<Product> updates = productDao.getTempUpdateProductList();
        if (!updates.isEmpty()) {
            productDao.updateProduct(updates);
            productDao.clearTempUpdateProducts();
        } else {
            System.out.println("No updates to commit.");
        }
    }

    public void clearPendingUpdates() {
        productDao.clearTempUpdateProducts();
    }

    public void clearPendingInserts() {
        productDao.clearTempProducts();
    }

    public void displayTempProduct(List<Product> productList) {
        productView.displayTempProducts(productList);
    }

    public List<Product> getTempUpdateProductList() {
        return productDao.getTempUpdateProductList();
    }

    public void deleteProductById(int id) {
        productDao.deleteProductById(id);
    }

    public void getProductByName(String name) {
        List<Product> productList = productDao.getProductByName(name);
        productView.display1Product(productList);
    }

    public void backUpProductToCSV(List<Product> productList,String filename) {
        productDao.backUpProductToCSV(productList, filename);
    }

    public List<Product> getProductForCSV() {
        return productDao.getAllProducts();
    }

//    public void insertCSVDataIntoDatabase(List<Product> products) {
//        if (!products.isEmpty()) {
//            try {
//                productDao.deleteAllProducts();
//                System.out.println("All existing data deleted.");
//
//                    try {
//                        productDao.insertCSVToDB(products);
//                    } catch (Exception e) {
//                        for (Product product : products) {
//                            System.err.println("Failed to insert product: " + product.getName() + " - " + e.getMessage());
//                        }
//                    }
//
//                System.out.println("CSV data inserted into the database.");
//            } catch (Exception e) {
//                System.err.println("Error during deletion or insertion: " + e.getMessage());
//            }
//        } else {
//            System.out.println("No products available to insert.");
//        }
//    }

    public boolean restoreFromBackup(List<Product> backupData) {
        try {
            // Get current max ID before truncate
            int preTruncateMaxId = productDao.getMaxProductId();

            // Truncate table and reset sequence
            productDao.deleteAllProducts();

            // Insert backup data
            productDao.insertCSVToDB(backupData);

            // Get new max ID from restored data
            int restoredMaxId = productDao.getMaxProductId();

            // Set sequence to the higher of the two values
            int newSequenceStart = Math.max(preTruncateMaxId, restoredMaxId);
            productDao.resetSequenceTo(newSequenceStart + 1);

            return true;
        } catch (Exception e) {
            System.err.println("Restore failed: " + e.getMessage());
            return false;
        }
    }

    public List<Product> readCSV(String filename) {
        return productDao.readCSV(filename);
    }

}
