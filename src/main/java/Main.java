import Controller.ProductController;
import Model.Product;
import Model.ProductDaoImpl;
import Model.Validate;
import View.ProductView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    public static void main(String[] args) {
        ProductController controller = new ProductController(new ProductDaoImpl(), new ProductView());
        Scanner scanner = new Scanner(System.in);

        int currentPage = 1;
        int pageSize = controller.getRow();
        List<Product> products = controller.getAllProducts();

        while (true) {
            controller.getView().displayProduct(products, currentPage, pageSize);
            controller.getView().showMenuOptions();

            String input = scanner.nextLine().toLowerCase();

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
                        System.out.println(RED + "Invalid page number. Please try again." + RESET);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                }
                continue;
            } else if (input.equals("e")) {
                break;
            }

            switch (input) {
                case "w":
                    int nextId = controller.getNextAvailableId();
                    System.out.println(GREEN + "Enter Product ID: " + nextId + " (auto-generated)" + RESET);

                    String name;
                    while (true) {
                        System.out.println("Enter product name: ");
                        name = scanner.nextLine();
                        if (Validate.isValidName(name)) break;
                        System.out.println(RED + "Invalid name. Please try again." + RESET);
                    }

                    double price;
                    while (true) {
                        System.out.println("Enter product price: ");
                        try {
                            price = Double.parseDouble(scanner.nextLine());
                            if (Validate.isValidPrice(price)) break;
                            System.out.println(RED + "Invalid price. Please try again." + RESET);
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Please enter a valid number." + RESET);
                        }
                    }

                    int stock;
                    while (true) {
                        System.out.println("Enter product Stock: ");
                        try {
                            stock = Integer.parseInt(scanner.nextLine());
                            if (Validate.isValidStock(stock)) break;
                            System.out.println(RED + "Invalid stock quantity. Please try again." + RESET);
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Please enter a valid number." + RESET);
                        }
                    }

                    LocalDate date = LocalDate.now();
                    controller.addTempProductList(name, price, stock, date);
                    System.out.println(GREEN + "Product added to temporary list." + RESET);
                    break;

                case "r":
                    System.out.println("Enter product ID: ");
                    int readId;
                    while (true) {
                        try {
                            readId = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Invalid ID format. Please enter a number:" + RESET);
                        }
                    }
                    controller.getProductById(readId);
                    break;

                case "u":
                    System.out.println("Enter product ID to update: ");
                    int idToUpdate;
                    while (true) {
                        try {
                            idToUpdate = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Invalid ID format. Please enter a number:" + RESET);
                        }
                    }

                    Product existingProduct = controller.fetchProductById(idToUpdate);
                    if (existingProduct == null) {
                        System.out.println(RED + "Product with ID " + idToUpdate + " not found." + RESET);
                        break;
                    }

                    System.out.println(GREEN + "Current product details:" + RESET);
                    controller.displayProductToValidate(idToUpdate);

                    System.out.println("Enter new product name (leave blank to keep current): ");
                    String updatedName = scanner.nextLine();
                    if (updatedName.isEmpty()) {
                        updatedName = existingProduct.getName();
                    } else {
                        while (!Validate.isValidName(updatedName)) {
                            System.out.println(RED + "Invalid name. Please enter again:" + RESET);
                            updatedName = scanner.nextLine();
                        }
                    }

                    double updatedPrice;
                    while (true) {
                        System.out.println("Enter new product price (0 to keep current): ");
                        try {
                            String priceInput = scanner.nextLine();
                            if (priceInput.equals("0")) {
                                updatedPrice = existingProduct.getUnitPrice();
                                break;
                            }
                            updatedPrice = Double.parseDouble(priceInput);
                            if (Validate.isValidPrice(updatedPrice)) break;
                            System.out.println(RED + "Invalid price. Please try again." + RESET);
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Please enter a valid number." + RESET);
                        }
                    }

                    int updatedStock;
                    while (true) {
                        System.out.println("Enter new product stock (0 to keep current): ");
                        try {
                            String stockInput = scanner.nextLine();
                            if (stockInput.equals("0")) {
                                updatedStock = existingProduct.getStockQuantity();
                                break;
                            }
                            updatedStock = Integer.parseInt(stockInput);
                            if (Validate.isValidStock(updatedStock)) break;
                            System.out.println(RED + "Invalid stock quantity. Please try again." + RESET);
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Please enter a valid number." + RESET);
                        }
                    }

                    Product updatedProduct = new Product(
                            idToUpdate,
                            updatedName,
                            updatedPrice,
                            updatedStock,
                            existingProduct.getImportedDate()
                    );
                    controller.updateProduct(updatedProduct);
                    products = controller.getAllProducts();
                    System.out.println(GREEN + "Product updated in temporary list." + RESET);
                    break;

                case "d":
                    System.out.println("Enter product ID to delete: ");
                    int idToDelete;
                    try {
                        idToDelete = Integer.parseInt(scanner.nextLine());
                        controller.deleteProductById(idToDelete);
                        products = controller.getAllProducts();
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Invalid ID format. Please enter a number." + RESET);
                    }
                    break;

                case "s":
                    System.out.println("Enter product Name to search: ");
                    String nameToSearch = scanner.nextLine();
                    controller.getProductByName(nameToSearch);
                    break;

                case "se":
                    System.out.println("Enter the amount of rows you want to set: ");
                    try {
                        int rows = Integer.parseInt(scanner.nextLine());
                        controller.setRow(rows);
                        pageSize = controller.getRow();
                        System.out.println(GREEN + "Page size set to " + rows + RESET);
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                    }
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
                                System.out.println(GREEN + "Products saved successfully." + RESET);
                            } else {
                                System.out.println(RED + "No pending inserts to save." + RESET);
                            }
                            break;
                        case "uu":
                            if (!controller.getTempUpdateProductList().isEmpty()) {
                                controller.commitUpdates();
                                products = controller.getAllProducts();
                                System.out.println(GREEN + "Updates saved successfully." + RESET);
                            } else {
                                System.out.println(RED + "No pending updates to save." + RESET);
                            }
                            break;
                        default:
                            System.out.println(RED + "Invalid save option." + RESET);
                    }
                    break;

                case "un":
                    System.out.println("UI to view all products to insert, UU to view all products to update");
                    String unsaveChoice = scanner.nextLine().toLowerCase();

                    switch (unsaveChoice) {
                        case "ui":
                            if (controller.getTempProductList().isEmpty()) {
                                System.out.println(RED + "No pending items to save." + RESET);
                            } else {
                                System.out.println(GREEN + "All pending items to save:" + RESET);
                                controller.showTempProductList();
                            }
                            break;

                        case "uu":
                            if (controller.getTempUpdateProductList().isEmpty()) {
                                System.out.println(RED + "No pending updates to save." + RESET);
                            } else {
                                System.out.println(GREEN + "Pending updates to save:" + RESET);
                                controller.displayTempProduct(controller.getTempUpdateProductList());
                            }
                            break;
                        default:
                            System.out.println(RED + "Invalid option." + RESET);
                    }
                    break;

                case "ba":
                    System.out.println("Enter the CSV File name: ");
                    String fileName = scanner.nextLine();
                    controller.backUpProductToCSV(controller.getProductForCSV(), fileName);
                    break;

                case "re":
                    System.out.println("Enter File name to restore: ");
                    String restoreFileName = scanner.nextLine();
                    controller.restoreFromBackup(controller.readCSV(restoreFileName));
                    products = controller.getAllProducts();
                    System.out.println(GREEN + "Data restored successfully." + RESET);
                    break;

                default:
                    System.out.println(RED + "Invalid input. Please try again." + RESET);
            }

            products = controller.getAllProducts();
            int totalPages = (int) Math.ceil((double) products.size() / pageSize);
            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
            }
        }
    }
}