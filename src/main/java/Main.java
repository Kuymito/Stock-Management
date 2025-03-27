import Controller.ProductController;
import Model.Product;
import Model.ProductDao;
import Model.ProductDaoImpl;
import View.ProductView;
import Model.Validate;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductController controller = new ProductController(new ProductDaoImpl(),new ProductView());
        controller.showAllProducts();
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("\tN. Next Page \t\tP. Previous Page \t\tF. Final Page \t\tL. Last Page \t\tG. Goto");
            System.out.println("W) Write\t\t R) Read\t\t U) Update\t\t D)Delete \t\t S) Search\t\t Se) Set rows ");
            System.out.println("Sa) Save\t\t U) Unsaved\t\tBa) Backup\t\t Re) Restore\t\t E) Exit");
            System.out.print("=> Chooses an option: ");
            String option = scanner.nextLine().trim().toLowerCase();
            switch (option) {
                case "w":
                    String name;
                    do {
                        System.out.print("Input product name: ");
                        name = scanner.nextLine().trim();
                    } while (!Validate.isValidName(name));
                    double price;
                    do {
                        System.out.print("Enter price: ");
                        while (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input! Please enter a valid price.");
                            scanner.next();
                        }
                        price = scanner.nextDouble();
                        scanner.nextLine();
                    } while (!Validate.isValidPrice(price));
                    int quantity;
                    do {
                        System.out.print("Enter quantity: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input! Please enter a valid quantity.");
                            scanner.next();
                        }
                        quantity = scanner.nextInt();
                        scanner.nextLine();
                    } while (!Validate.isValidStock(quantity));
                    System.out.print("Enter to continue...");
                    String dateInput = scanner.nextLine().trim();
                    LocalDate date;
                    if (dateInput.isEmpty()) {
                        date = LocalDate.now();
                    } else {
                        date = LocalDate.parse(dateInput);
                    }
                    controller.addProduct(name, price, quantity, date);
                    System.out.println("Product added successfully");
                    break;
                case "r":
                    controller.showAllProducts();
                    break;
                case "u":
                    System.out.print("Enter Product ID to update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("New product name: ");
                    String newName = scanner.nextLine();
                    System.out.print("New price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("New quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("New imported date (YYYY-MM-DD): ");
                    LocalDate newDate = LocalDate.parse(scanner.nextLine());
                    controller.updateProduct(id, newName, newPrice, newQuantity, newDate);
                    break;
                case "d":
                    System.out.print("Enter Product ID to delete: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    controller.deleteProduct(id);
                    break;
                case "e":
                    System.out.println("Goodbye...");
                    return;
            }
        }
    }
}



