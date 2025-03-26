package View;

import Model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;

public class ProductView {
    public ProductView() {}
    public void displayProduct(List<Product> products) {
        Scanner scanner = new Scanner(System.in);
        int pageSize = 1; // Number of products per page
        int totalPages = (int) Math.ceil((double) products.size() / pageSize);

        int currentPage = 1;
        while (true) {
            Table display = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
            display.addCell("ID");
            display.addCell("Name");
            display.addCell("Price");
            display.addCell("Quantity");
            display.addCell("Import Date");

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
            display.addCell("Page: "+ currentPage + " of "+ totalPages,3);
            display.addCell("Total Records: "+String.valueOf(products.size()),3);
            System.out.println(display.render());

            System.out.println("Navigate: [P]revious, [N]ext, [G]oto page, [E]xit");
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (input.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else if (input.equals("g")) {
                // Goto feature
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
            } else if (input.equals("e")) {
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
