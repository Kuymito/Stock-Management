package Model;

import java.util.List;

public interface ProductDao {
    void addProduct(List<Product> product);
    List<Product> getAllProducts();
    void tempProductList(Product product);
    List<Product> gettempProductList();
    void setRow(int row);
    int getSetRow();
    List<Product> getProductByName(String name);
    Product getProductById(int id);
    void updateProduct(List<Product> products);
    void addTempUpdateProduct(Product product);
    List<Product> getTempUpdateProductList();
    void clearTempUpdateProducts();
    void clearTempProducts();
    void deleteProductById(int id);
    void backUpProductToCSV(List<Product> products,String filename);
    void deleteAllProducts();
    List<Product> readCSV(String filename);
    void insertCSVToDB(List<Product> products);
    public int getMaxProductId();
    public void truncateTable(boolean restartIdentity);
    public void resetSequenceTo(int value);
    public int getCurrentSequenceValue();

}
