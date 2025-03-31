package View;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import Model.Product;

import java.util.List;

public class ProductView {
    public ProductView() {}
    public void displayProduct(List<Product> products) {
        System.out.println("Product List:");
        System.out.println("+----+--------------+------------+-----+-------------+");
        System.out.printf("| %-2s | %-12s | %-10s | %-3s | %-10s |\n", "ID", "Name", "Unit Price", "Qty", "Import Date");
        System.out.println("+----+--------------+------------+-----+-------------+");
        for (Product p : products) {
            System.out.printf("| %-2d | %-12s | %-10.2f | %-3d | %-10s  |\n",
                    p.getId(), p.getName(), p.getUnitPrice(), p.getStockQuantity(), p.getImportedDate());
        }
        System.out.println("+----+--------------+------------+-----+-------------+");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
