package Controller;

import Model.Product;
import Model.ProductDao;
import Model.Validate;
import Model.ValidationException;
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

    public ProductView getView() {
        return this.productView;
    }

    public int getNextAvailableId() {
        return productDao.getMaxProductId() + 1;
    }

    public void addTempProductList(String name, double price, int quantity, LocalDate date) {
        int nextId = getNextAvailableId();
        productDao.tempProductList(new Product(nextId, name, price, quantity, date));
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
        if(productList.isEmpty()) {
            System.out.println("Product not found");
        }else{
            productView.display1Product(productList);
        }
    }

    public void backUpProductToCSV(List<Product> productList,String filename) {
        productDao.backUpProductToCSV(productList, filename);
    }

    public List<Product> getProductForCSV() {
        return productDao.getAllProducts();
    }



    public boolean restoreFromBackup(List<Product> backupData) {
        try {
            if(backupData!=null && !backupData.isEmpty()) {
                int preTruncateMaxId = productDao.getMaxProductId();

                productDao.deleteAllProducts();

                productDao.insertCSVToDB(backupData);

                int restoredMaxId = productDao.getMaxProductId();
                int newSequenceStart = Math.max(preTruncateMaxId, restoredMaxId);
                productDao.resetSequenceTo(newSequenceStart + 1);

                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            System.err.println("Restore failed: " + e.getMessage());
            return false;
        }
    }

    public List<Product> readCSV(String filename) {
        return productDao.readCSV(filename);
    }

    public boolean validateProductInputs(String name, double price, int stock) {
        try {
            Validate.validateProduct(name, price, stock);
            return true;
        } catch (ValidationException e) {
            System.out.println("Validation errors:");
            for (String error : e.getErrors()) {
                System.out.println("- " + error);
            }
            return false;
        }
    }

    public boolean validateProductInputs(int id, String name, double price, int stock) {
        try {
            Validate.validateProduct(id, name, price, stock);
            return true;
        } catch (ValidationException e) {
            System.out.println("Validation errors:");
            for (String error : e.getErrors()) {
                System.out.println("- " + error);
            }
            return false;
        }
    }

}
