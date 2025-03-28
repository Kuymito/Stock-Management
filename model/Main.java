package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductController controller = new ProductController();
        ProductView view = new ProductView();
        List<Product> products1 = new ArrayList<>();
        view.displayProductList(products1);
        
        int rowsPerPage = 5;

        while (true) {
            System.out.println("\nW) Write   R) Read   U) Update    D) Delete    S) Search    SR) Set Rows   ");
            System.out.println("Sa) Save    Un) Unsave    Ba) Backup    Re) Restore     E) Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "W":
                    Product newProduct = view.inputProductDetails();
                    if (controller.insertProduct(newProduct)) {
                        view.displayMessage("Product added successfully.");
                    } else {
                        view.displayMessage("Failed to add product.");
                    }
                    break;

                case "R":
                    System.out.print("Enter page number: ");
                    int page = scanner.nextInt();
                    scanner.nextLine();
                    List<Product> products = controller.displayProducts(page, rowsPerPage);
                    view.displayProductList(products);
                    break;

                case "U":
                    System.out.print("Enter Product ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    Product existingProduct = controller.viewProduct(updateId);
                    if (existingProduct != null) {
                        Product updatedProduct = view.inputProductDetails();
                        updatedProduct.setId(updateId);
                        if (controller.updateProduct(updatedProduct)) {
                            view.displayMessage("Product updated successfully.");
                        } else {
                            view.displayMessage("Failed to update product.");
                        }
                    } else {
                        view.displayMessage("Product not found.");
                    }
                    break;

                case "D":
                    System.out.print("Enter Product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    if (controller.deleteProduct(deleteId)) {
                        view.displayMessage("Product deleted successfully.");
                    } else {
                        view.displayMessage("Failed to delete product.");
                    }
                    break;

                case "S":
                    String searchName = view.getSearchName();
                    List<Product> searchResults = controller.searchByName(searchName);
                    view.displayProductList(searchResults);
                    break;

                case "SR":
                    System.out.print("Enter number of rows per page: ");
                    rowsPerPage = scanner.nextInt();
                    scanner.nextLine();
                    view.displayMessage("Rows per page set to " + rowsPerPage);
                    break;

                case "E":
                    System.out.println("Exiting the program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
