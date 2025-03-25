import Controller.ProductController;
import Model.ProductDaoImpl;
import View.ProductView;


public class Main {
    public static void main(String[] args) {
        ProductController controller = new ProductController(new ProductDaoImpl(),new ProductView());
        controller.showAllProducts();
    }
}
