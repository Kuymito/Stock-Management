import java.util.List;
import java.util.Scanner;

public class ProductView {
    private static final Scanner scanner = new Scanner(System.in);

    public void displayProductList(List<Product> products) {
        System.out.println("ID | Name | Price | Stock | Imported Date");
        for (Product product : products) {
            System.out.println(product.getId() + " | " + product.getName() + " | " + product.getUnitPrice() + " | " + product.getStockQuantity() + " | " + product.getImportedDate());
        }
    }

    public Product inputProductDetails() {
        System.out.println("Enter Product Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Unit Price:");
        double price = scanner.nextDouble();
        System.out.println("Enter Stock Quantity:");
        int stock = scanner.nextInt();
        scanner.nextLine(); // consume newline
        Product product = new Product();
        product.setName(name);
        product.setUnitPrice(price);
        product.setStockQuantity(stock);
        product.setImportedDate(new java.util.Date()); // current date
        return product;
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getSearchName() {
        System.out.println("Enter product name to search:");
        return scanner.nextLine();
    }
}
