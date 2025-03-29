package model;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductController controller = new ProductController();
        ProductView view = new ProductView();

        int rowsPerPage = 5;
        int currentPage = 1;
        int totalRecords = controller.getTotalProductCount();
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);

        while (true) {
            List<Product> products = controller.displayProducts(currentPage, rowsPerPage);
            view.displayProductList(products, currentPage, totalPages, totalRecords);

            System.out.print("\nChoose an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("You are on the last page.");
                    }
                    break;

                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You are on the first page.");
                    }
                    break;

                case "F":
                    currentPage = 1;
                    break;

                case "L":
                    currentPage = totalPages;
                    break;

                case "G":
                    System.out.print("Enter page number: ");
                    int gotoPage = scanner.nextInt();
                    scanner.nextLine();
                    if (gotoPage >= 1 && gotoPage <= totalPages) {
                        currentPage = gotoPage;
                    } else {
                        System.out.println("Invalid page number.");
                    }
                    break;

                case "W":
                    Product newProduct = view.inputProductDetails();
                    if (controller.insertProduct(newProduct)) {
                        totalRecords++;
                        totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
                        System.out.println("Product added successfully.");
                    } else {
                        System.out.println("Failed to add product.");
                    }
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
                            System.out.println("Product updated successfully.");
                        } else {
                            System.out.println("Failed to update product.");
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case "D":
                    System.out.print("Enter Product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    if (controller.deleteProduct(deleteId)) {
                        totalRecords--;
                        totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
                        System.out.println("Product deleted successfully.");
                    } else {
                        System.out.println("Failed to delete product.");
                    }
                    break;

                case "S":
                    String searchName = view.getSearchName();
                    List<Product> searchResults = controller.searchByName(searchName);
                    view.displayProductList(searchResults, 1, 1, searchResults.size());
                    break;

                case "SR":
                    System.out.print("Enter number of rows per page: ");
                    rowsPerPage = scanner.nextInt();
                    scanner.nextLine();
                    totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
                    System.out.println("Rows per page set to " + rowsPerPage);
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
