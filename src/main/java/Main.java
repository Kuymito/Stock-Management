import Controller.ProductController;
import Model.Product;
import Model.ProductDaoImpl;
import View.ProductView;

import java.time.LocalDate;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ProductController controller = new ProductController(new ProductDaoImpl(),new ProductView());
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("W) Write\t\tR) Read\t\tU) Update\t\tD) Delete\t\tS) Search\t\tSe) Set Row\t\tSa) Save\t\nUn) Unsave\t\tBa) Backup\t\tRe) Restore\t\tE) Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "w":
                    System.out.println("Enter product name: ");
                    String name = scanner.next();
                    System.out.println("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.println("Enter product Stock: ");
                    int stock = scanner.nextInt();
                    LocalDate date = LocalDate.now();
                    controller.addTempProductList(name, price, stock, date);
                    break;
                case "r":
                    controller.showAllProducts();
                    System.out.println("---------");
                    controller.showTempProductList();
                    break;
                case "u":

            }
        }while(true);

//        controller.showAllProducts();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter product name: ");
//        String name = scanner.nextLine();
//        System.out.println("Enter product price: ");
//        double price = scanner.nextDouble();
//        System.out.println("Enter product quantity: ");
//        int quantity = scanner.nextInt();
//        LocalDate date = LocalDate.now();
//        controller.addProduct(name, price, quantity, date);
    }
}
