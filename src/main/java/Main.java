import Controller.ProductController;
import Model.Product;
import Model.ProductDaoImpl;
import View.ProductView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ProductController controller = new ProductController(new ProductDaoImpl(), new ProductView());
        Scanner scanner = new Scanner(System.in);

        int currentPage = 1;
        int pageSize = controller.getRow();
        List<Product> products = controller.getAllProducts();

        while (true) {
            // Display products and menu
            controller.getView().displayProduct(products, currentPage, pageSize);
            controller.getView().showMenuOptions();

            String input = scanner.nextLine().toLowerCase();

            // Handle pagination commands
            if (input.equals("p")) {
                if (currentPage > 1) currentPage--;
                continue;
            } else if (input.equals("n")) {
                int totalPages = (int) Math.ceil((double) products.size() / pageSize);
                if (currentPage < totalPages) currentPage++;
                continue;
            } else if (input.equals("g")) {
                int totalPages = (int) Math.ceil((double) products.size() / pageSize);
                System.out.print("Enter page number (1-" + totalPages + "): ");
                try {
                    int gotoPage = Integer.parseInt(scanner.nextLine());
                    if (gotoPage >= 1 && gotoPage <= totalPages) {
                        currentPage = gotoPage;
                    } else {
                        System.out.println("Invalid page number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
                continue;
            } else if (input.equals("e")) {
                break;
            }

            // Handle menu commands
            switch (input) {
                case "w":
                    System.out.println("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.println("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.println("Enter product Stock: ");
                    int stock = scanner.nextInt();
                    LocalDate date = LocalDate.now();
                    controller.addTempProductList(name, price, stock, date);
                    scanner.nextLine();
                    break;
                case "r":
                    controller.displayTempProduct(controller.getTempProductList());
                    break;
                case "u":
                    System.out.println("Enter product ID to update: ");
                    int idToUpdate;
                    try {
                        idToUpdate = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format. Please enter a number.");
                        break;
                    }

                    Product existingProduct = controller.fetchProductById(idToUpdate);
                    if (existingProduct == null) {
                        System.out.println("Product with ID " + idToUpdate + " not found.");
                        break;
                    }

                    System.out.println("Current product details:");
                    controller.displayProductToValidate(idToUpdate);

                    System.out.println("Enter new product name (leave blank to keep current): ");
                    String updatedName = scanner.nextLine();
                    if (updatedName.isEmpty()) {
                        updatedName = existingProduct.getName();
                    }

                    System.out.println("Enter new product price (0 to keep current): ");
                    double updatedPrice;
                    try {
                        updatedPrice = Double.parseDouble(scanner.nextLine());
                        if (updatedPrice == 0) {
                            updatedPrice = existingProduct.getUnitPrice();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price. Keeping current value.");
                        updatedPrice = existingProduct.getUnitPrice();
                    }

                    System.out.println("Enter new product stock (0 to keep current): ");
                    int updatedStock;
                    try {
                        updatedStock = Integer.parseInt(scanner.nextLine());
                        if (updatedStock == 0) {
                            updatedStock = existingProduct.getStockQuantity();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid stock quantity. Keeping current value.");
                        updatedStock = existingProduct.getStockQuantity();
                    }
                    LocalDate updatedDate = existingProduct.getImportedDate();

                    Product updatedProduct = new Product(
                            idToUpdate,
                            updatedName,
                            updatedPrice,
                            updatedStock,
                            updatedDate
                    );

                    controller.updateProduct(updatedProduct);
                    products = controller.getAllProducts();

                    System.out.println("Updated product (not saved yet):");
                    controller.displayProductToValidate(idToUpdate);
                    System.out.println("Remember to save with 'Sa' -> 'UU' to commit changes.");
                    break;
                case "d":
                    System.out.println("Enter product ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    controller.deleteProductById(idToDelete);
                    break;
                case "s":
                    System.out.println("Enter product Name to search: ");
                    String nameToSearch = scanner.nextLine();
                    controller.getProductByName(nameToSearch);
                    break;
                case "se":
                    System.out.println("Enter the amount of rows you want to set: ");
                    int rows = scanner.nextInt();
                    scanner.nextLine();
                    controller.setRow(rows);
                    break;
                case "sa":
                    System.out.println("UI to save Insert, UU to save Update");
                    String choice2 = scanner.nextLine().toLowerCase();
                    switch (choice2) {
                        case "ui":
                            if (!controller.getTempProductList().isEmpty()) {
                                controller.addProductToDB(controller.getTempProductList());
                                products = controller.getAllProducts();
                                controller.clearPendingInserts();
                            } else {
                                System.out.println("No pending inserts to save.");
                            }
                            break;
                        case "uu":
                            if (!controller.getTempUpdateProductList().isEmpty()) {
                                controller.commitUpdates();
                                products = controller.getAllProducts();
                            } else {
                                System.out.println("No pending updates to save.");
                            }
                            break;
                        default:
                            System.out.println("Invalid save option.");
                    }
                    break;
                case "un":
                    System.out.println("UI to unsave Insert, UU to unsave Update, A to unsave all");
                    String unsaveChoice = scanner.nextLine().toLowerCase();

                    switch (unsaveChoice) {
                        case "ui":
                            if (controller.getTempProductList().isEmpty()) {
                                System.out.println("No pending inserts to discard.");
                            } else {
                                System.out.println("Pending inserts to be discarded:");
                                controller.showTempProductList();
                                System.out.println("Are you sure? (Y/N)");
                                if (scanner.nextLine().equalsIgnoreCase("y")) {
                                    controller.clearPendingInserts();
                                    System.out.println("Pending inserts discarded.");
                                }
                            }
                            break;

                        case "uu":
                            if (controller.getTempProductList().isEmpty()) {
                                System.out.println("No pending updates to discard.");
                            } else {
                                System.out.println("Pending updates to be discarded:");
                                controller.displayTempProduct(controller.getTempProductList());
                                System.out.println("Are you sure? (Y/N)");
                                if (scanner.nextLine().equalsIgnoreCase("y")) {
                                    controller.clearPendingUpdates();
                                    System.out.println("Pending updates discarded.");
                                }
                            }
                            break;

                        case "a":
                            boolean hasInserts = !controller.getTempProductList().isEmpty();
                            boolean hasUpdates = !controller.getTempUpdateProductList().isEmpty();

                            if (!hasInserts && !hasUpdates) {
                                System.out.println("No pending changes to discard.");
                                break;
                            }

                            if (hasInserts) {
                                System.out.println("Pending inserts to be discarded:");
                                controller.showTempProductList();
                            }
                            if (hasUpdates) {
                                System.out.println("Pending updates to be discarded:");
                                controller.displayTempProduct(controller.getTempUpdateProductList());
                            }

                            System.out.println("Discard ALL pending changes? (Y/N)");
                            if (scanner.nextLine().equalsIgnoreCase("y")) {
                                controller.clearPendingInserts();
                                controller.clearPendingUpdates();
                                System.out.println("All pending changes discarded.");
                            }
                            break;
                    }
                case "ba":
                    System.out.println("Enter the CSV File name: ");
                    String fileName = scanner.nextLine();
                    controller.backUpProductToCSV(controller.getProductForCSV(), fileName);
                    break;
                case "re":
                    System.out.println("Enter File name to restore: ");
                    String restoreFileName = scanner.nextLine();
                    controller.restoreFromBackup(controller.readCSV(restoreFileName));
                default:
                    System.out.println("Invalid input. Please try again.");

                    products = controller.getAllProducts();
                    int totalPages = (int) Math.ceil((double) products.size() / pageSize);
                    if (currentPage > totalPages && totalPages > 0) {
                        currentPage = totalPages;
                    }
            }
        }
    }
}
