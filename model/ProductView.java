package model;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ProductView {
    private static final Scanner scanner = new Scanner(System.in);

    public void displayProductList(List<Product> products, int page, int totalPages, int totalRecords) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("Quantity");
        table.addCell("Imported Date");
        for (Product product : products) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getUnitPrice()));
            table.addCell(String.valueOf(product.getStockQuantity()));
            table.addCell(String.valueOf(product.getImportedDate()));
        }
        System.out.println(table.render());
        System.out.printf("Page: %d of %d    Total Records: %d\n", page, totalPages, totalRecords);
        System.out.println("\nN. Next Page   P. Previous Page   F. First Page   L. Last Page   G. Goto Page \n" +
                "");
        System.out.println("W) Write   R) Read   U) Update    D) Delete    S) Search   SR) Set Rows");
        System.out.println("Sa) Save    Un) Unsave    Ba) Backup    Re) Restore     E) Exit");
    }


    public Product inputProductDetails() {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Unit Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt();
        scanner.nextLine();


        String importDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Product product = new Product();
        product.setName(name);
        product.setUnitPrice(price);
        product.setStockQuantity(stock);
        product.setImportedDate(importDate);

        return product;
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getSearchName() {
        System.out.print("Enter product name to search:");
        return scanner.nextLine();
    }
}
