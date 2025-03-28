package View;

import Model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;


public class ProductView {
    public ProductView() {}
    public void displayProduct(List<Product> products, int currentPage, int pageSize) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        Table display = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        display.setColumnWidth(0, 20, 100);
        display.setColumnWidth(1, 20, 100);
        display.setColumnWidth(2, 20, 100);
        display.setColumnWidth(3, 20, 100);
        display.setColumnWidth(4, 20, 100);

        display.addCell("ID");
        display.addCell("Name");
        display.addCell("Price");
        display.addCell("Quantity");
        display.addCell("Import Date");


        int totalPages = (int) Math.ceil((double) products.size() / pageSize);
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, products.size());
        for (int i = start; i < end; i++) {
            Product product = products.get(i);
            display.addCell(String.valueOf(product.getId()));
            display.addCell(product.getName());
            display.addCell(String.valueOf(product.getUnitPrice()));
            display.addCell(String.valueOf(product.getStockQuantity()));
            display.addCell(String.valueOf(product.getImportedDate()));
        }

        display.addCell("Page: " + currentPage + " of " + totalPages, 3);
        display.addCell("Total Records: " + products.size(), 3);
        System.out.println(display.render());
    }

    public void showMenuOptions() {
        System.out.println("\nMAIN MENU:");
        System.out.println("W) Write  R) Read  U) Update  D) Delete  S) Search");
        System.out.println("Se) Set Rows  Sa) Save  Un) Unsave  Ba) Backup  Re) Restore");
        System.out.println("P) Previous Page  N) Next Page  G) Go to Page  E) Exit");
        System.out.print("Enter your choice: ");
    }
    public void displayTempProducts(List<Product> products) {
        Table displayTemp = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        displayTemp.setColumnWidth(0,20,100);
        displayTemp.setColumnWidth(1,20,100);
        displayTemp.setColumnWidth(2,20,100);
        displayTemp.setColumnWidth(3,20,100);
        displayTemp.setColumnWidth(4,20,100);
        displayTemp.addCell("ID");
        displayTemp.addCell("Name");
        displayTemp.addCell("Price");
        displayTemp.addCell("Quantity");
        displayTemp.addCell("Import Date");
        for (Product product : products) {
            displayTemp.addCell(String.valueOf(product.getId()));
            displayTemp.addCell(product.getName());
            displayTemp.addCell(String.valueOf(product.getUnitPrice()));
            displayTemp.addCell(String.valueOf(product.getStockQuantity()));
            displayTemp.addCell(String.valueOf(product.getImportedDate()));
        }
        System.out.println(displayTemp.render());
    }

    public void display1Product(List<Product> product) {
        Table displayTemp = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        displayTemp.setColumnWidth(0,20,100);
        displayTemp.setColumnWidth(1,20,100);
        displayTemp.setColumnWidth(2,20,100);
        displayTemp.setColumnWidth(3,20,100);
        displayTemp.setColumnWidth(4,20,100);
        displayTemp.addCell("ID");
        displayTemp.addCell("Name");
        displayTemp.addCell("Price");
        displayTemp.addCell("Quantity");
        displayTemp.addCell("Import Date");
        for (Product product1 : product) {
            displayTemp.addCell(String.valueOf(product1.getId()));
            displayTemp.addCell(product1.getName());
            displayTemp.addCell(String.valueOf(product1.getUnitPrice()));
            displayTemp.addCell(String.valueOf(product1.getStockQuantity()));
            displayTemp.addCell(String.valueOf(product1.getImportedDate()));
        }

        System.out.println(displayTemp.render());
    }
    public void showMessage(String message) {
        System.out.println(message);
    }
}
